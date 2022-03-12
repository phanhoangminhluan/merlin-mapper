package com.merlin.mapper;

import java.util.Collection;

public interface BaseMapping<E> {

    <T> T toDTO(E entity, Class<T> targetClass) throws Exception;

    <T> E toEntity(T dto) throws Exception;

    <T> Collection<T> toDTOs(Collection<E> entities, Class<T> targetClass, Class<? extends Collection> targetCollection) throws Exception;

    <T> Collection<E> toEntities(Collection<T> dtos, Class<? extends Collection> targetCollection) throws Exception;

}
