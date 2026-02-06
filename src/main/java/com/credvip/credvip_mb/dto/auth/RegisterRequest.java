package com.credvip.credvip_mb.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String email,
        @NotBlank @Size(min = 8) String password,
        String firstName,
        String lastName
) {}
