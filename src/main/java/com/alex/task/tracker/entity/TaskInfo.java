package com.alex.task.tracker.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This is additional class for {@link Task entity}.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class TaskInfo {
    private String name;
    private String description;
    private LocalDate creationDate;
    private LocalDate deadlineDate;

}