package com.alex.task.tracker.listener;

import com.alex.task.tracker.entity.Task;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;

/**
 * This is a listener that allows to change count of task.
 */

public class CountTasksListener {
    @PrePersist
    public void prePersist(Task entity) {
        var num = entity.getNumber();
        entity.setNumber(++num);
    }

    @PreRemove
    public void preRemove(Task entity) {
        var num = entity.getNumber();
        entity.setNumber(--num);
    }

}
