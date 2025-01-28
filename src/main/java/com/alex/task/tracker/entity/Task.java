package com.alex.task.tracker.entity;


import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import com.alex.task.tracker.listener.CountTasksListener;
import jakarta.persistence.*;
import lombok.*;

/**
 * This is an entity class.
 * It associated with 'task' table in the database.
 *
 * @see Account account.
 * @see TaskInfo embdeded part of task.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"performer","customer"})
@EqualsAndHashCode(exclude = {"performer","customer"})
@EntityListeners(CountTasksListener.class)
@Entity
@Table(name = "task"/*, schema = "task_manager_storage"*/)
public class Task extends AuditEntity<Long> implements Comparable<Task>{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id",nullable = false)
    private Account customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="performer_id",nullable = false)
    private Account performer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate", column = @Column(name = "creation_date",nullable = false)),
            @AttributeOverride(name="deadlineDate",column = @Column(name="deadline_date",nullable = false)),
            @AttributeOverride(name = "name", column = @Column(nullable = false)),
            @AttributeOverride(name="description",column = @Column(nullable = false,length = 1024)),
    })
    private TaskInfo taskInfo;

    @Builder.Default
    private Integer number=0;
    //Maybe char(3)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Override
    public int compareTo(Task o) {
        return this.number-o.getNumber();
    }

}