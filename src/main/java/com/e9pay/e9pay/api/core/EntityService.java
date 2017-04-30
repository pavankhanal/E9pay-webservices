package com.e9pay.e9pay.api.core;

import java.util.List;

import com.e9pay.e9pay.api.utils.QueryCriteria;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
public interface EntityService<E extends Identifiable> {

    /**
     * Find a given entity based on ID.
     *
     * @param id
     *     The ID to search for.
     *
     * @return The entity or {@code null} if not found.
     */
    E findById(Long id);

    /**
     * @return Every known instance of this entity type.  Prefer calling {@link #findAll(QueryCriteria)} in preference to this method when querying.
     */
    List<E> findAll();

    /**
     * Find an entity based on a set of criteria.
     *
     * @param query
     *     The criteria for the query.
     *
     * @return A {@link List} containing the search results.
     */
    List<E> findAll(QueryCriteria query);

    /**
     * Delete a given entity.
     *
     * @param id
     *     The ID of the entity to delete.
     *
     * @return The deleted entity.
     */
    E delete(Long id);

    /**
     * Inserts/Updates a given entity.
     *
     * @param entity
     *     The entity to be persisted
     *
     * @return The saved entity.
     */
    E saveOrUpdate(E entity);

    /**
     * Inserts/Updates a list of entities.
     *
     * @param entities
     *     The entities to be persisted
     *
     * @return The saved entities.
     */
    List<E> saveOrUpdate(List<E> entities);

    /**
     * Returns the count of the objects returned based on the supplied query criteria
     *
     * @param queryCriteria
     *     the query criteria
     *
     * @return the number of objects that match the criteria
     */
    Long getCount(QueryCriteria queryCriteria);
}

