package com.alex.task.tracker.dto;


import com.alex.task.tracker.service.AccountService;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * This is POJO for interaction between different layers of app.
 * It used during as an answer from methods of {@link AccountService service}.
 */

@Builder
@Value
public class AccountResponseDto {
    Long id;
    String fullName;
    List<String> roles;
}
