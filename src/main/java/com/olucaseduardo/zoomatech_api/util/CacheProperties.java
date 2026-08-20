package com.olucaseduardo.zoomatech_api.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class CacheProperties {
    private String host;
    private int port;
    private String password;
}
