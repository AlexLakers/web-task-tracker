package com.alex.task.tracker.dto;


import lombok.Builder;

/**
 * This POJO for filtering task process.
 * It contains all the necessary parameters which can be used for search of tasks.
 *
 *
 * @param loggedAccountId selected  task consumer.
 * @param performerId     selected task performer.
 * @param creationDate    selected creation date.
 * @param deadLineDate    selected deadline date.
 * @param priority        selected priority of task.
 * @param status          selected status of task.
 */

@Builder
public record TaskFilterDto(Long loggedAccountId,
                            Long performerId,
                            String creationDate,
                            String deadLineDate,
                            String priority,
                            String status
) {
}