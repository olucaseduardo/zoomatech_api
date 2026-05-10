package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.dto.work_performed.WorkPerformedResponseDTO;
import com.olucaseduardo.zoomatech_api.entity.Service;
import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;

import java.util.List;

public record WorkPerformedHomePageResponseDTO(
        String photo,
        String title,
        String description,
        String performedAt,
        List<String> services
) {
    public WorkPerformedHomePageResponseDTO(WorkPerformedResponseDTO workPerformed) {
        this(workPerformed.photo(), workPerformed.title(), workPerformed.description(), workPerformed.performedAt(), workPerformed.services().stream().map((w) -> (String) w.get("name")).toList());
    }

    public WorkPerformedHomePageResponseDTO(WorkPerformed workPerformed) {
        this(workPerformed.getPhoto(), workPerformed.getTitle(), workPerformed.getDescription(), workPerformed.getPerformedAt(), workPerformed.getServices().stream().map(Service::getName).toList());
    }
}
