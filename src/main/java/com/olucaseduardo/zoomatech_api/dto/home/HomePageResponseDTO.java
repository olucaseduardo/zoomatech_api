package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.dto.edital.EditalResponseDTO;
import com.olucaseduardo.zoomatech_api.dto.evento.EventosClassificadosResponseDTO;

import java.util.List;

public record HomePageResponseDTO(
        List<MemberHomePageResponseDTO> members,
        List<WorkPerformedHomePageResponseDTO> workPerformeds,
        List<SystemConfigurationHomePageDTO> systemConfigurations,
        List<ServiceHomePageResponseDTO> services,
        EventosClassificadosResponseDTO eventos,
        List<EditalResponseDTO> editais
) {
}
