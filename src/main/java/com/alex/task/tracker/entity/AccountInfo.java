package com.alex.task.tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is additional class for {@link Account entity}.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class AccountInfo {
    private String firstName;
    private String lastName;
}