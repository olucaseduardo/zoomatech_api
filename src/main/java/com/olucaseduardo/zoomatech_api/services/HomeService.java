package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.evento.EventosClassificadosResponseDTO;
import com.olucaseduardo.zoomatech_api.dto.home.*;
import com.olucaseduardo.zoomatech_api.dto.work_performed.WorkPerformedResponseDTO;
import com.olucaseduardo.zoomatech_api.entity.Member;
import com.olucaseduardo.zoomatech_api.entity.SystemConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final ServiceService serviceService;
    private final WorkPerformedService workPerformedService;
    private final MemberService memberService;
    private final SystemConfigurationService systemConfigurationService;
    private final EventoService eventoService;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "homepage", key = "'default'")
    public HomePageResponseDTO findHomePageData() {
        List<Member> members = this.memberService.findAll();
        List<WorkPerformedResponseDTO> workPerformeds = this.workPerformedService.findAll();
        List<SystemConfiguration> systemConfigurations = this.systemConfigurationService.fetchAll();
        List<com.olucaseduardo.zoomatech_api.entity.Service> services = this.serviceService.findAll();
        EventosClassificadosResponseDTO eventos = this.eventoService.findAll();

        var membersDTO = members.stream().map(MemberHomePageResponseDTO::new).toList();
        var workPerformedsDTO = workPerformeds.stream().map(WorkPerformedHomePageResponseDTO::new).toList();
        var systemConfigurationsDTO = systemConfigurations.stream().map(SystemConfigurationHomePageDTO::new).toList();
        var servicesDTO = services.stream().map(ServiceHomePageResponseDTO::new).toList();

        return new HomePageResponseDTO(membersDTO, workPerformedsDTO, systemConfigurationsDTO, servicesDTO, eventos);
    }
}
