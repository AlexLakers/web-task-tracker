package com.alex.task.tracker.dto;

import com.alex.task.tracker.service.TaskService;
import com.alex.task.tracker.validator.ValidDateFormat;
import com.alex.task.tracker.validator.ValidPriorityTask;
import com.alex.task.tracker.validator.ValidStatusTask;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * This is POJO for interaction between different layers.
 * It used for updating an available task into {@link TaskService service}.
 *
 * @param id
 * @param loggedAccountId
 * @param performerId
 * @param deadLineDate
 * @param description
 * @param priority
 * @param status
 */

@Builder
public record TaskUpdatingDto(@NotNull
                              Long id,

                              @NotNull
                              Long loggedAccountId,

                              Long performerId,

                              @ValidDateFormat(isUpdating = true, message = "The date should have the following format yyyy-mm-dd")
                              String deadLineDate,

                              @Size(min = 0, max = 256, message = "The description should have less symbols")
                              String description,

                              @ValidPriorityTask(
                                      isUpdatingCheck = true,
                                      message = "The priority of task should be 'LOW','HGH','MDM' or empty")
                              String priority,

                              @ValidStatusTask(
                                      message = "The status of task should be 'PROGRESS','PERFORMED','CANCELLED' or empty")
                              String status
) {

}