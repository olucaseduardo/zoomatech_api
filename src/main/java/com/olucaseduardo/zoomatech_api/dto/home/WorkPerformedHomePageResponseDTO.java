package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;

import java.util.Date;
import java.util.List;

public record WorkPerformedHomePageResponseDTO(
        String photo,
        String title,
        String description,
        String performedAt,
        List<String> services
) {
    public WorkPerformedHomePageResponseDTO(WorkPerformed workPerformed)
    {
        this(workPerformed.getPhoto(),workPerformed.getTitle(),workPerformed.getDescription(),workPerformed.getPerformedAt());
    }
}
