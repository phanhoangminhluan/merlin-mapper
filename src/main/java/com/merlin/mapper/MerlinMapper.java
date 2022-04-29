package com.merlin.mapper;

import com.merlin.mapper.utils.MerlinMapperUtils;
import lombok.SneakyThrows;

import java.util.Collection;

public abstract class MerlinMapper<E> implements IMerlinMapper<E> {
    private Class<E> entityClass;

    public MerlinMapper(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @SneakyThrows
    public <T> T toDTO(E entity, Class<T> targetClass) {
        return new MerlinMapperUtils().mapSingleObject(entity, targetClass);
    }

    @Override
    @SneakyThrows
    public <T> E toEntity(T dto) {
        return new MerlinMapperUtils().mapSingleObject(dto, this.entityClass);
    }

    @Override
    @SneakyThrows
    public <T> Collection<T> toDTOs(Collection<E> entities, Class<T> targetClass, Class<? extends Collection> targetCollection) {
        return new MerlinMapperUtils().mapList(entities, targetClass, targetCollection);
    }

    @Override
    @SneakyThrows
    public <T> Collection<E> toEntities(Collection<T> dtos, Class<? extends Collection> targetCollection) {
        return new MerlinMapperUtils().mapList(dtos, this.entityClass, targetCollection);
    }
}