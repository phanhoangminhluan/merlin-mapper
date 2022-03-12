package com.merlin.mapper;

import java.util.Collection;

public class BaseMappingImpl<E> implements BaseMapping<E> {
    private Class<E> entityClass;

    public BaseMappingImpl(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public <T> T toDTO(E entity, Class<T> targetClass) throws Exception {
        return new MerlinMapper().mapSingleObject(entity, targetClass);
    }

    @Override
    public <T> E toEntity(T dto) throws Exception {
        return new MerlinMapper().mapSingleObject(dto, this.entityClass);
    }

    @Override
    public <T> Collection<T> toDTOs(Collection<E> entities, Class<T> targetClass, Class<? extends Collection> targetCollection) throws Exception {
        return new MerlinMapper().mapList(entities, targetClass, targetCollection);
    }

    @Override
    public <T> Collection<E> toEntities(Collection<T> dtos, Class<? extends Collection> targetCollection) throws Exception {
        return new MerlinMapper().mapList(dtos, this.entityClass, targetCollection);
    }
}