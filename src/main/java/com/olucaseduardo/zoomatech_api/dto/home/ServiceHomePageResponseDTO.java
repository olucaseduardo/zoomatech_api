package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.entity.Service;

import java.util.List;

public record ServiceHomePageResponseDTO(
        List<ServiceTopicHomePageResponseDTO> topics,
        String icon,
        String description,
        String name,
        List<WorkPerformedHomePageResponseDTO> performed
) {
    public ServiceHomePageResponseDTO(Service service) {
        this(service.getServiceTopic().stream().map(ServiceTopicHomePageResponseDTO::new).toList(), service.getIcon(), service.getDescription(), service.getName(), service.getWorkPerformeds().stream().map(WorkPerformedHomePageResponseDTO::new).toList());
    }
}
