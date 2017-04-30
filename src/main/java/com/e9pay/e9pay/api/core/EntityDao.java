package com.e9pay.e9pay.api.core;

import java.util.List;

import com.e9pay.e9pay.api.utils.QueryCriteria;
import org.hibernate.FlushMode;
import org.hibernate.LockOptions;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
/**
 * This is the base interface for all DAO classes that are based specifically on a single entity.  This interface implies a large number of Hibernate
 * / JPA semantics so it shouldn't be exposed outside of the data access layer.
 *
 * @param <E>
 *     The entity type.
 */
public interface EntityDao<E extends Identifiable> {

    /**
     * Save an entity and return its identifier.
     *
     * @param e
     *     The entity to save.
     *
     * @return The {@link Long} representation of the entity identifier.
     */
    Long save(E e);

    /**
     * Save or update the supplied entity.  If the entity has an identifier, it will be updated.  Otherwise it will be inserted.
     *
     * @param e
     *     The entity to save or update.
     */
    void saveOrUpdate(E e);
hsdhahfhahdfhasfhahfhahfhhahfhahfhahfdhahfhahfhahfhahfhahfhahfhahfhahfhahfhahfhhafhhahfhfh
    void lock(LockOptions lockOptions, E e);

    /**
     * Load an entity based on ID.  This method follows Hibernate load semantics and may return a proxied object.  Prefer calling get over this
     * method in most cases as it behaves with the most predictable behavior.
     *
     * @param id
     *     The identifier of the entity to load.
     *
     * @return A proxy object representing the object with the given identifier.
     */
    E load(Long id);

    /**
     * Load an entity based on ID.  This method follows Hibernate load semantics and may return a proxied object.  Prefer calling get over this
     * method in most cases as it behaves with the most predictable behavior.
     *
     * @param id
     *     The identifier of the entity to load.
     *
     * @return A proxy object representing the object with the given identifier.
     */
    E load(Integer id);

    /**
     * Retrieve an object based on ID.  This method follows Hibernate get semantics and must not return a proxy.
     *
     * @param id
     *     The identifier of the entity to load.
     *
     * @return The entity, or null if not found.
     */
    E get(Long id);

    /**
     * Retrieve an object based on ID.  This method follows Hibernate get semantics and must not return a proxy.
     *
     * @param id
     *     The identifier of the entity to load.
     *
     * @return The entity, or null if not found.
     */
    E get(Integer id);

    /**
     * Update the supplied entity.
     *
     * @param e
     *     The entity to update.
     */
    void update(E e);

    /**
     * Delete the supplied entity.
     *
     * @param e
     *     The entity to delete.
     */
    void delete(E e);

    /**
     * Delete the supplied entity by ID.
     *
     * @param id
     *     The ID of the entity to delete.
     */
    void delete(Long id);

    /**
     * Delete the supplied entity by ID.
     *
     * @param id
     *     The ID of the entity to delete.
     */
    void delete(Integer id);

    /**
     * Refresh the supplied entity from the data store.
     *
     * @param e
     *     The entity to refresh.
     */
    void refresh(E e);

    /**
     * @return Every known instance of this entity type.  Prefer calling {@link #findAll(QueryCriteria)} in preference to this method when querying.
     */
    List<E> findAll();

    /**
     * Find an entity based on a set of criteria.
     *
     * @param criteria
     *     The criteria for the query.
     *
     * @return A {@link List} containing the search results.
     */
    List<E> findAll(QueryCriteria criteria);

    /**
     * Find the number of records matching the given criteria
     *
     * @param criteria
     *     The criteria for the query.
     *
     * @return A count of records matching the given criteria.
     */
    Long getCount(QueryCriteria criteria);

    /**
     * Flush any persistent entities to the database.
     */
    void flush();

    /**
     * Clear the underlying session.
     */
    void clear();

    /**
     * Evict an entity from the underlying session.
     *
     * @param o
     *     The entity to evict.
     */
    void evict(Object o);

    /**
     * Merge a transient instance of an entity with the persistent instance of that entity.
     *
     * @param e
     *     The transient entity to merge.
     *
     * @return The persistent instance.
     */
    E merge(E e);

    /**
     * Merge a transient instance of an entity with the persistent instance of that entity and return the identifier.
     *
     * @param e
     *     The transient entity to merge.
     *
     * @return The Long identifier.
     */
    Long mergeAndGetId(E e);

    /**
     * @return All of the entities with an enabled flag set to true. This method assumes the entity has an enabled property and will fail for entities
     * that do not.
     */
    List<E> getEnabled();

    /**
     * @return The total number of entities in the database matching this type.
     */
    Number getTotalCount();

    /**
     * @return The {@link Class} instance representing the persistent type for this EntityDao.
     */
    Class<E> getPersistentClass();

    /**
     * Method to set the flush mode for the current session.
     * <p>
     * <b>NOTE THAT THIS IS DANGEROUS AND SHOULD BE USED VERY VERY RARELY.  CHANGING THE FLUSH MODE ON THE DAO AFFECTS THE ENTIRE SESSION.</b>
     *
     * @param flushMode
     *     the flush mode to change to
     */
    void setFlushMode(FlushMode flushMode);

    /**
     * Method to get the current flush mode for the current session
     *
     * @return the current flush mode
     */
    FlushMode getFlushMode();
}
