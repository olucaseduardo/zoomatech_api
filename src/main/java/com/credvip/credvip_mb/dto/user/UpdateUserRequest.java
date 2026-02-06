package com.credvip.credvip_mb.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank @Size(min = 8) String password,
        String firstName,
        String lastName
) {
}


