package com.olucaseduardo.zoomatech_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    @Value("${rate-limiting.capacity:20}")
    private double capacity;

    @Value("${rate-limiting.duration-seconds:60}")
    private long durationSeconds;

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public RateLimitingFilter() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "rate-limit-cleaner");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(this::cleanupStaleBuckets, 10, 10, TimeUnit.MINUTES);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Skip rate limiting for CORS preflight OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = getClientIp(request);
        TokenBucket bucket = buckets.computeIfAbsent(clientIp, k -> new TokenBucket(capacity));

        synchronized (bucket) {
            long now = System.currentTimeMillis();
            double refillRatePerMs = capacity / (durationSeconds * 1000.0);
            double elapsedTime = now - bucket.lastRefillTimestamp;

            bucket.tokens = Math.min(capacity, bucket.tokens + (elapsedTime * refillRatePerMs));
            bucket.lastRefillTimestamp = now;

            if (bucket.tokens >= 1.0) {
                bucket.tokens -= 1.0;
                response.setHeader("X-RateLimit-Limit", String.valueOf((long) capacity));
                response.setHeader("X-RateLimit-Remaining", String.valueOf((long) Math.floor(bucket.tokens)));
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Retry-After", String.valueOf(durationSeconds));
                response.setHeader("X-RateLimit-Limit", String.valueOf((long) capacity));
                response.setHeader("X-RateLimit-Remaining", "0");

                String jsonResponse = """
                        {
                            "status": 429,
                            "error": "Too Many Requests",
                            "message": "Limite de requisições excedido (%d por minuto). Tente novamente em breve."
                        }
                        """.formatted((long) capacity);

                response.getWriter().write(jsonResponse);
            }
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.trim().isEmpty()) {
            return xfHeader.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.trim().isEmpty()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    private void cleanupStaleBuckets() {
        long now = System.currentTimeMillis();
        long staleThresholdMs = TimeUnit.MINUTES.toMillis(15);
        buckets.entrySet().removeIf(entry -> (now - entry.getValue().lastRefillTimestamp) > staleThresholdMs);
    }

    private static class TokenBucket {
        double tokens;
        long lastRefillTimestamp;

        TokenBucket(double capacity) {
            this.tokens = capacity;
            this.lastRefillTimestamp = System.currentTimeMillis();
        }
    }
}
