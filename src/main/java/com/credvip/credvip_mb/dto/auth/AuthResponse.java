package com.credvip.credvip_mb.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}
