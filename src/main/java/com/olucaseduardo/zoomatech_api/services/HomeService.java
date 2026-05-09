package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.home.HomePageResponseDTO;
import com.olucaseduardo.zoomatech_api.entity.Member;
import com.olucaseduardo.zoomatech_api.entity.SystemConfiguration;
import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final ServiceService serviceService;
    private final WorkPerformedService workPerformedService;
    private final MemberService  memberService;
    private final SystemConfigurationService systemConfigurationService;

    public HomePageResponseDTO findHomePageData() {
        List<Member> members = this.memberService.findAll();
        List<WorkPerformed> workPerformeds = this.workPerformedService.findAll();
        List<SystemConfiguration> systemConfigurations = this.systemConfigurationService.fetchAll();
        List<com.olucaseduardo.zoomatech_api.entity.Service> services = this.serviceService.findAll();

        return new HomePageResponseDTO(members,workPerformeds,systemConfigurations,services);
    }
}
