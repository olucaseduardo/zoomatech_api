package com.olucaseduardo.zoomatech_api.dto.home;

import java.util.List;

public record HomePageResponseDTO(
        List<MemberHomePageResponseDTO> members,
        List<WorkPerformedHomePageResponseDTO> workPerformeds,
        List<SystemConfigurationHomePageDTO> systemConfigurations,
        List<ServiceHomePageResponseDTO> services
) {
}
