package com.alex.task.tracker.dto;


import com.alex.task.tracker.service.TaskService;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * This is POJO for interaction between different layers of app.
 * It used during as an answer from methods of {@link TaskService service}.
 */

@Builder
@Value
public class TaskResponseDto {
    Long id;
    String name;
    String costumer;
    String performer;
    LocalDate creationDate;
    LocalDate deadLineDate;
    String description;
    String priority;
    String status;
}
