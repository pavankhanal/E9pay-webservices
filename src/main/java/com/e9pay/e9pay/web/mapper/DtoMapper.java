package com.e9pay.e9pay.web.mapper;

import com.e9pay.e9pay.api.mapper.PropertyMappings;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
public interface DtoMapper<E, D> {

    /**
     * Given a transfer object, map it to the corresponding entity type. A {@code null} argument should
     * return {@code null}.
     *
     * @param dto
     *     The transfer object to map.
     *
     * @return The newly created entity object.
     */
    E toEntity(D dto);

    /**
     * Given an entity object, map it to the corresponding transfer object type. A {@code null} argument should
     * return {@code null}.
     *
     * @param entity
     *     The entity object to map.
     *
     * @return The newly created transfer object.
     */
    D toDto(E entity);

    /**
     * This method should return that canonical mapping of source to target properties. Mappers need to explicitly define this valid set of properties
     * for proper security and encapsulation.
     *
     * @return A validated {@link PropertyMappings} instance.
     */
    PropertyMappings getSourceTargetPropertyMappings();

    /**
     * @return The type of the entity object.
     */
    Class<E> getEntityClass();

    /**
     * @return The type of the data-transfer object.
     */
    Class<D> getDtoClass();
}
