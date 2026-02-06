package com.olucaseduardo.template.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}
