package com.alex.task.tracker.entity;

import java.io.Serializable;

/**
 * This interface as a super entity.
 * It allows you to add other entities in this app with minimal changes.
 * In this case you just stick the contract.
 *
 * @param <K> id of entity.
 */
public interface BaseEntity<K extends Serializable> {
    K getId();

    void setId(K id);
}
