package com.e9pay.e9pay.api.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.e9pay.e9pay.api.utils.QueryCriteria;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
public abstract class BaseEntityService<E extends Identifiable> implements EntityService<E> {

    /**
     * Finds the entity with the supplied id
     *
     * @param id
     *     the id to search for
     *
     * @return the entity with the supplied id, or null if no entity exists with that id.
     */
    @Override
    @Transactional
    public E findById(Long id) {
        return getEntityDao().get(id);
    }

    /**
     * @return Every known instance of this entity type.  Prefer calling {@link #findAll(QueryCriteria)} in preference to this method when querying.
     */
    @Override
    @Transactional
    public List<E> findAll(){
        return getEntityDao().findAll();
    }

    /**
     * Returns all of the entities in the data store for the specified type
     *
     * @param criteria
     *     The criteria for the query.
     *
     * @return a list of all entities in the data store, or an empty list of there are no entities in the data store.
     */
    @Override
    @Transactional
    public List<E> findAll(QueryCriteria criteria) {
        return getEntityDao().findAll(criteria);
    }

    /**
     * Service method to delete an entity from the data store with the supplied entity
     *
     * @param id
     *     the entity of the entity to be deleted
     *
     * @return the entity as it was in the data store before it was deleted.
     */
    @Override
    @Transactional
    public E delete(Long id) {
        final E entity = getEntityDao().get(id);
        E target = doPreDelete(doValidateDelete(entity));

        getEntityDao().delete(target);

        return doPostDelete(target);
    }

    /**
     * Method to perform actions on a deleted entity after the entity has been removed from the data store
     *
     * @param entity
     *     the entity that was deleted
     */
    protected E doPostDelete(E entity) {
        return entity;
    }

    /**
     * Method to perform actions on an updated entity after the entity has been updated in the data store
     *
     * @param entity
     *     the entity that was updated
     */
    protected E doPostSaveOrUpdate(E entity) {
        return entity;
    }

    /**
     * Method to perform actions on an entity before that entity is deleted from the data store
     *
     * @param entity
     *     the entity that will be deleted
     */
    protected E doPreDelete(E entity) {
        return entity;
    }

    /**
     * Method to perform actions on an entity before that entity is updated in the data store
     *
     * @param entity
     *     the entity that will be updated
     */
    protected E doPreSaveOrUpdate(E entity) {
        return entity;
    }

    /**
     * Method to perform validations on an entity before that entity is deleted from the data store
     *
     * @param entity
     *     the entity that will be deleted
     */
    protected E doValidateDelete(E entity) {
        return entity;
    }

    /**
     * Method to perform validations on an entity before that entity is updated in the data store
     *
     * @param entity
     *     the entity that will be updated
     */
    protected E doValidateSaveOrUpdate(E entity) {
        return entity;
    }

    /**
     * Service method to update an entity in the data store
     *
     * @param entity
     *     the entity
     *
     * @return the entity after it has been updated in the database.
     */
    @Override
    @Transactional
    public E saveOrUpdate(E entity) {
        E target = doPreSaveOrUpdate(doValidateSaveOrUpdate(entity));

        getEntityDao().saveOrUpdate(target);

        return doPostSaveOrUpdate(target);
    }

    /**
     * Service method to save a list of entities in the data store
     *
     * @param entities
     *     the entities
     *
     * @return the entity after it has been updated in the database.
     */
    @Override
    @Transactional
    public List<E> saveOrUpdate(List<E> entities) {
        final List<E> savedEntities = new ArrayList<>();
        for (E entity : entities) {
            final E savedEntity = saveOrUpdate(entity);
            savedEntities.add(savedEntity);
        }

        return savedEntities;
    }

    /**
     * @return The {@link BaseEntityDao} associated with this service.
     */
    protected abstract EntityDao<E> getEntityDao();

    /**
     * Returns a count of all of the entities in the data store matching the specified criteria
     *
     * @param criteria
     *     The criteria for the query.
     *
     * @return a count of all entities in the data store matching the selected criteria
     */
    @Override
    @Transactional
    public Long getCount(QueryCriteria criteria) {
        return getEntityDao().getCount(criteria);
    }
}

