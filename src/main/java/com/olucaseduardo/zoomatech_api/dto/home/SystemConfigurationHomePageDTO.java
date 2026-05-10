package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.entity.SystemConfiguration;

public record SystemConfigurationHomePageDTO(
        String key,
        String value,
        String description
) {
    public SystemConfigurationHomePageDTO(SystemConfiguration systemConfiguration) {
        this(systemConfiguration.getKey(), systemConfiguration.getValue(), systemConfiguration.getDescription());
    }
}
