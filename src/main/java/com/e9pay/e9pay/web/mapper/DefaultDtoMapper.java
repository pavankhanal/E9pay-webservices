package com.e9pay.e9pay.web.mapper;

import com.e9pay.e9pay.api.mapper.MappingDirection;
import com.e9pay.e9pay.api.mapper.PropertyMappings;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
public class DefaultDtoMapper<E, D> implements DtoMapper<E, D> {

    private final Class<E> entityClass;
    private final Class<D> dtoClass;
    private PropertyMappings dtoToEntityMappings;
    private PropertyMappings entityToDtoMappings;

    public DefaultDtoMapper(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public PropertyMappings getSourceTargetPropertyMappings() {
        return getDtoToEntityMappings();
    }

    private PropertyMappings getDtoToEntityMappings() {
        if (dtoToEntityMappings == null) {
            dtoToEntityMappings = PropertyMappings.create(dtoClass, MappingDirection.DEFAULT);
        }
        return dtoToEntityMappings;
    }

    private PropertyMappings getEntityToDtoMappings() {
        if (entityToDtoMappings == null) {
            entityToDtoMappings = PropertyMappings.create(dtoClass, MappingDirection.INVERSE);
        }
        return entityToDtoMappings;
    }

    @Override
    public E toEntity(D dto) {
        return getDtoToEntityMappings().map(dto, entityClass);
    }

    @Override
    public D toDto(E entity) {
        return getEntityToDtoMappings().map(entity, dtoClass);
    }

    @Override
    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public Class<D> getDtoClass() {
        return dtoClass;
    }
}
