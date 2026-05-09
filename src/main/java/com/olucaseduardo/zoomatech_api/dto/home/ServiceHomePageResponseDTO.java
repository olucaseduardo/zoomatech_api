package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;

import java.util.List;

public record ServiceHomePageResponseDTO(
        List<ServiceTopicHomePageResponseDTO> topics,
        String icon,
        String description,
        String name,
        List<WorkPerformedHomePageResponseDTO> performed
) {
}
