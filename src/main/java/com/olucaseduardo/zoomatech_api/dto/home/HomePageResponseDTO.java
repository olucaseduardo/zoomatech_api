package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.entity.Member;
import com.olucaseduardo.zoomatech_api.entity.Service;
import com.olucaseduardo.zoomatech_api.entity.SystemConfiguration;
import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;

import java.util.List;

public record HomePageResponseDTO(
        List<Member> members, List<WorkPerformed> workPerformeds, List<SystemConfiguration> systemConfigurations, List<Service> services
) {
}
