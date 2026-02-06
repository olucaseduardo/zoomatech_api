package com.olucaseduardo.template.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank @Size(min = 8) String password,
        String firstName,
        String lastName
) {
}


