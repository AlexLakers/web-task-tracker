package com.alex.task.tracker.mapper;

/**
 * This interface is a mapper for special entity.
 *
 * @param <E> entity.
 * @param <R> returned dto.
 * @param <W> given dto.
 */

public interface Mapper<E, R, W> {
    /**
     * Returns dto by given entity.
     *
     * @param entity entity for mapping.
     * @return dto for mapping.
     */
    R toDto(E entity);

    /**
     * Returns entity by given dto.
     *
     * @param dto dto for mapping.
     * @return entity after mapping.
     */
    E toEntity(W dto);
}
