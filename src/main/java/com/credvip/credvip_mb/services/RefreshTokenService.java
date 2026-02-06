package com.credvip.credvip_mb.services;

import com.credvip.credvip_mb.entity.RefreshToken;
import com.credvip.credvip_mb.entity.User;
import com.credvip.credvip_mb.exceptions.TokenExpiredException;
import com.credvip.credvip_mb.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepository repository;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .build();

        return repository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now()) || token.isRevoked()) {
            repository.delete(token);
            throw new TokenExpiredException("Refresh token expired");
        }
        return token;
    }

    @Transactional
    public void revokeAllUserTokens(User user) {
        repository.deleteByUser(user);
    }
}