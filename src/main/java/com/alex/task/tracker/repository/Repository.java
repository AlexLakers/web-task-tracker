package com.alex.task.tracker.repository;

import com.alex.task.tracker.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * This interface is a repository-part of this app.
 * It defines the main 'CRUD' methods for interaction between java model and database model.
 */

public interface Repository<K extends Serializable, E extends BaseEntity<K>> {
    /**
     * Returns all the available persistent entities.
     *
     * @return persistent entities.
     */

    List<E> findAll();

    /**
     * Returns entity that's wrapped in {@link Optional optional}.
     * The search occurs by id.
     *
     * @param id id of entity.
     * @return optional entity.
     */

    Optional<E> findById(K id);

    /**
     * Returns saved-persistent entity with id.
     *
     * @param entity transient entity.
     * @return persistent entity.
     */

    E save(E entity);

    /**
     * Performs removing process for entity by id.
     *
     * @param id id of entity.
     */

    void delete(K id);

    /**
     * Updates detach or transient entity into database.
     *
     * @param entity detached or transient entity.
     */
    void update(E entity);
}
