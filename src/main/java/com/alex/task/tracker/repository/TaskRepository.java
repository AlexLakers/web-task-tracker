package com.alex.task.tracker.repository;
import com.alex.task.tracker.dto.TaskFilterDto;
import com.alex.task.tracker.entity.Account_;
import com.alex.task.tracker.entity.Task;
import com.alex.task.tracker.entity.TaskInfo_;
import com.alex.task.tracker.entity.Task_;
import com.alex.task.tracker.util.DateTimeHelper;
import com.alex.task.tracker.util.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

/**
 * This class is task repository.
 * It contains all the 'CRUD' functions realized into {@link BaseRepository baseRepository} and additional func.
 * This repository interaction with 'task' table in the database.
 *
 * @see Task entity.
 */

public class TaskRepository extends BaseRepository<Long, Task> {

    private static TaskRepository INSTANCE = new TaskRepository(HibernateUtil.getSessionProxy());

    public static TaskRepository getInstance() {
        return INSTANCE;
    }

    public TaskRepository(EntityManager entityManager) {
        super(Task.class, entityManager);
    }

    /**
     * Returns all the available persistent task entities by input filter from user.
     * If someone parameter of filter is null then it doesn't take part in final filter condition.
     *
     * @param filter input filter.
     * @return If transmitted task are unavailable by filter then list will be empty.
     * Else-not empty list.
     */

    public List<Task> findAll(TaskFilterDto filter) {
       var createDate = DateTimeHelper.dateIsValid(filter.creationDate())
                ? DateTimeHelper.parseDate(filter.creationDate())
                : null;


        var deadDate = DateTimeHelper.dateIsValid(filter.deadLineDate())
                ? DateTimeHelper.parseDate(filter.deadLineDate())
                : null;


        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Task.class);
        var task = criteria.from(Task.class);


        /*var predicateCustomerPerformer= filter.loggedAccountId().equals(filter.performerId())

                ? cb.or(cb.equal(task.get(Task_.CUSTOMER).get(Account_.ID),filter.loggedAccountId()),
                    cb.equal(task.get(Task_.PERFORMER).get(Account_.ID),filter.performerId()))

                : cb.and(cb.equal(task.get(Task_.CUSTOMER).get(Account_.ID),filter.loggedAccountId()),
                    cb.equal(task.get(Task_.PERFORMER).get(Account_.ID),filter.performerId()));*/


        var predicateCustomerPerformer = filter.loggedAccountId().equals(filter.performerId())

                ? HPredicate.builder().addOr(filter.loggedAccountId(), filter.performerId(),
                (cId, pId) -> cb.or(
                        cb.equal(task.get(Task_.CUSTOMER).get(Account_.ID), cId),
                        cb.equal(task.get(Task_.PERFORMER).get(Account_.ID), pId)
                )).build()

                : HPredicate.builder().add(filter.loggedAccountId(),
                        (cId) -> cb.equal(task.get(Task_.CUSTOMER).get(Account_.ID), cId))
                .add(filter.performerId(),
                        (pId) -> cb.equal(task.get(Task_.PERFORMER).get(Account_.ID), pId)).build();

        var predicates = HPredicate.builder()
                /* .addOr(filter.loggedAccountId(),filter.performerId(),
                         (cId,pId)->cb.or(
                         cb.equal(task.get(Task_.CUSTOMER).get(Account_.ID),cId),
                         cb.equal(task.get(Task_.PERFORMER).get(Account_.ID),pId)
                 ))


             .add(filter.loggedAccountId(),
                            (cId) -> cb.equal(task.get(Task_.CUSTOMER).get(Account_.ID), cId))
                    .add(filter.performerId(),
                            (pId) -> cb.equal(task.get(Task_.PERFORMER).get(Account_.ID), pId))*/
                .add(createDate,
                        (cDate) -> cb.greaterThanOrEqualTo(task.get(Task_.taskInfo).get(TaskInfo_.CREATION_DATE), cDate))
                .add(deadDate,
                        (dDate) -> cb.lessThanOrEqualTo(task.get(Task_.taskInfo).get(TaskInfo_.DEADLINE_DATE), dDate))
                .add(filter.priority(),
                        (prty) -> cb.equal(task.get(Task_.PRIORITY), prty))
                .add(filter.status(),
                        (status) -> cb.equal(task.get(Task_.STATUS), status))
                .build();

        predicates.addAll(predicateCustomerPerformer);
        criteria.select(task)
                .where(predicates.toArray(Predicate[]::new));

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }
        //Maybe add method findById by properties.This method is exist in Account Repo too.

}
