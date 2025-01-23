package com.alex.task.tracker.dto;

import com.alex.task.tracker.service.TaskService;
import com.alex.task.tracker.validator.ValidDateFormat;
import com.alex.task.tracker.validator.ValidPriorityTask;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * This is POJO for interaction between different layers of app.
 * It used during creation a new task for a logged in account and given into {@link TaskService#create(TaskCreationDto)} (TaskCreationDto dto)}.
 *
 * @param name entered task name.
 * @param loggedAccountId logged in account id.
 * @param performerId id of the selected task performer.
 * @param deadLineDate entered deadline date.
 * @param description entered task description.
 * @param priority entered task priority.
 */

@Builder
public record TaskCreationDto(
        @NotNull @Pattern(regexp = "[A-Za-z]{2,20}",message = "The task name should contains more symbols")
        String name,
        @NotNull
        Long loggedAccountId,
        @NotNull
        Long performerId,
        @NotNull @ValidDateFormat(message = "The date should have the following format yyyy-mm-dd")
        String deadLineDate,
        @NotNull @Size(min = 20,message = "The description should have more symbols")
        String description,
        @NotNull @ValidPriorityTask(message = "The priority of task should be 'LOW','HGH','MDM'")
        String priority

) {
}

