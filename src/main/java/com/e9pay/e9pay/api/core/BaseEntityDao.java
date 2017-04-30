package com.e9pay.e9pay.api.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.e9pay.e9pay.api.utils.CriteriaUtils;
import com.e9pay.e9pay.api.utils.QueryCriteria;
import com.e9pay.e9pay.api.utils.SortDirection;
import com.e9pay.e9pay.api.utils.SortField;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.sql.JoinType;
import org.hibernate.type.Type;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
@Transactional
public class BaseEntityDao<E extends Identifiable> implements EntityDao<E> {

    @Autowired
    @Qualifier(value = "sessionFactory")
    private SessionFactory sessionFactory;

    private final Class<E> persistentClass;

    protected BaseEntityDao(Class<E> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @Override
    public Class<E> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public void lock(LockOptions lockOptions, E e) {
        getSession()
            .buildLockRequest(lockOptions)
            .lock(e);
    }

    @Override
    public E merge(E e) {
        return (E) getSession().merge(e);
    }

    @Override
    public Long mergeAndGetId(E e) {
        final E entity = merge(e);

        if (entity != null) {
            return safeCast((Serializable) entity.getId());
        }

        return null;
    }

    @Override
    public List<E> getEnabled() {
        Criteria criteria = getCurrentSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("enabled", Boolean.TRUE));

        applyDefaultSort(criteria);
        return criteria.list();
    }

    @Override
    public Number getTotalCount() {
        Criteria criteria = getCurrentSession().createCriteria(getPersistentClass());
        criteria.setProjection(Projections.rowCount());
        return (Number) criteria.uniqueResult();
    }

    @Override
    public Long save(E e) {
        return safeCast(getSession().save(e));
    }

    @Override
    public void saveOrUpdate(E e) {
        getSession().saveOrUpdate(e);
    }

    @Override
    public E load(Long id) {
        return (E) getSession().load(getPersistentClass(), identityType(id));
    }

    @Override
    public E load(Integer id) {
        return (E) getSession().load(getPersistentClass(), id);
    }

    @Override
    public E get(Long id) {
        return (E) getSession().get(getPersistentClass(), identityType(id));
    }

    @Override
    public E get(Integer id) {
        return (E) getSession().get(getPersistentClass(), id);
    }

    private Serializable identityType(Long value) {
        final ClassMetadata classMetadata = getSessionFactory().getClassMetadata(getPersistentClass());

        final Type identifierType = classMetadata.getIdentifierType();
        final Class<?> returnedClass = identifierType.getReturnedClass();

        if (returnedClass.isAssignableFrom(Integer.class)) {
            return value.intValue();
        }
        return value;
    }

    @Override
    public void update(E e) {
        getSession().update(e);
    }

    @Override
    public void delete(E e) {
        getSession().delete(e);
    }

    @Override
    public void delete(Long id) {
        getSession().delete(load(id));
    }

    @Override
    public void delete(Integer id) {
        getSession().delete(load(id));
    }

    @Override
    public void refresh(E e) {
        getCurrentSession().refresh(e);
    }

    @Override
    public List<E> findAll() {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        applyDefaultSort(criteria);
        return criteria.list();
    }

    /**
     * Finds all entities in the data store.
     *
     * @param criteria
     *     The criteria for the query.
     *
     * @return a list of all entities in the data store, or an empty list if there are no entities
     */
    @Override
    @Transactional
    public List<E> findAll(QueryCriteria criteria) {

        final Session session = getCurrentSession();

        final Criteria sessionCriteria = session.createCriteria(getPersistentClass());

        sessionCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        applyFindAllQueryAliases(sessionCriteria, criteria);
        applyFindAllFilterCriteria(sessionCriteria, criteria);
        applyFindAllSortCriteria(sessionCriteria, criteria);
        applyFindAllPagination(sessionCriteria, criteria);

        //noinspection unchecked
        return sessionCriteria.list();
    }

    @Override
    public Long getCount(QueryCriteria criteria) {

        final Session session = getCurrentSession();

        final Criteria sessionCriteria = session.createCriteria(getPersistentClass());

        sessionCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        applyFindAllQueryAliases(sessionCriteria, criteria);
        applyFindAllFilterCriteria(sessionCriteria, criteria);

        sessionCriteria.setProjection(Projections.rowCount());
        //noinspection unchecked
        return (Long) sessionCriteria.uniqueResult();
    }

    protected void applyFindAllQueryAliases(Criteria criteria, QueryCriteria queryCriteria) {
       CriteriaUtils.applyQueryAliases(criteria, JoinType.LEFT_OUTER_JOIN, queryCriteria.getFieldOperations().getFieldNames());
    }

    protected void applyFindAllFilterCriteria(Criteria criteria, QueryCriteria queryCriteria) {
        CriteriaUtils.applyFilterCriteria(criteria, getPersistentClass(), queryCriteria.getFieldOperations().getFilterOperations());
    }

    protected void applyFindAllPagination(Criteria criteria, QueryCriteria queryCriteria) {
        CriteriaUtils.applyPagination(criteria, queryCriteria.getPage(), queryCriteria.getPageSize());
    }

    protected void applyFindAllSortCriteria(Criteria criteria, QueryCriteria queryCriteria) {
        CriteriaUtils.applySortCriteria(criteria, queryCriteria.getFieldOperations().getSortOperations());
    }

    @Override
    public void flush() {
        getCurrentSession().flush();
    }

    @Override
    public void clear() {
        getCurrentSession().clear();
    }

    @Override
    public void evict(Object o) {
        getCurrentSession().evict(o);
    }

    protected void applyDefaultSort(Criteria criteria) {
        final Class<E> pClass = getPersistentClass();
        final Field[] declaredFields = pClass.getDeclaredFields();

        TreeSet<Field> list = new TreeSet<>(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return Integer.valueOf(o1.getAnnotation(SortField.class).order()).compareTo(o2.getAnnotation(SortField.class).order());
            }
        });

        for (Field declaredField : declaredFields) {
            final SortField annotation = declaredField.getAnnotation(SortField.class);
            if (annotation != null) {
                list.add(declaredField);
            }
        }

        for (Field field : list) {
            final SortField annotation = field.getAnnotation(SortField.class);
            if (annotation.direction().equals(SortDirection.DESC)) {
                criteria.addOrder(Order.desc(field.getName()));
            } else {
                criteria.addOrder(Order.asc(field.getName()));
            }
        }
    }

    /*
     * Implementation note: some of the application entities may expose their ID as an Integer and
     * some expose them as a Long.  The public API of this class accepts both types, however only
     * produces the wider (Long) type, so we use this method internally to ensure no loss of precision.
     */
    private Long safeCast(Serializable value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (long) (int) value;
        }

        return (Long) value;
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    protected Session getSession() {
        return getCurrentSession();
    }

    @Override
    public void setFlushMode(FlushMode flushMode) {
        getSessionFactory().getCurrentSession().setFlushMode(flushMode);
    }

    @Override
    public FlushMode getFlushMode() {
        return getSessionFactory().getCurrentSession().getFlushMode();
    }
}

