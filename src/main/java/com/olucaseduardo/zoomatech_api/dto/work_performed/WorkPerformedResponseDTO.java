package com.olucaseduardo.zoomatech_api.dto.work_performed;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record WorkPerformedResponseDTO(
        UUID id,
        String photo,
        String title,
        String description,
        String performedAt,
        List<Map> services
) {
    public WorkPerformedResponseDTO(com.olucaseduardo.zoomatech_api.entity.WorkPerformed workPerformed) {
        this(workPerformed.getId(), workPerformed.getPhoto(), workPerformed.getTitle(), workPerformed.getDescription(), workPerformed.getPerformedAt(), workPerformed.getServices().stream()
                .map(s -> Map.of(
                        "id", s.getId(),
                        "name", s.getName()
                ))
                .collect(Collectors.toList()));
    }
}
