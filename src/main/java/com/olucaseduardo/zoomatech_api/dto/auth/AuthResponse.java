package com.olucaseduardo.zoomatech_api.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}
