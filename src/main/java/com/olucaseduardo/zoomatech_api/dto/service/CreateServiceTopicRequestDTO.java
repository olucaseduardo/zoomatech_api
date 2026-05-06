package com.olucaseduardo.zoomatech_api.dto.service;

import com.olucaseduardo.zoomatech_api.util.interfaces.AtLeastOneField;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@AtLeastOneField
public record CreateServiceTopicRequestDTO(
        @NotBlank
        String topic,

        String description,

        List<String> items
) {
}
