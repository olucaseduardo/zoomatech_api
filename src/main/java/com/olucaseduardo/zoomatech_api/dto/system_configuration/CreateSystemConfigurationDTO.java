package com.olucaseduardo.zoomatech_api.dto.system_configuration;

import java.util.UUID;

public record CreateSystemConfigurationDTO(
        UUID id,
        String key,
        String value,
        String description
) {
}
