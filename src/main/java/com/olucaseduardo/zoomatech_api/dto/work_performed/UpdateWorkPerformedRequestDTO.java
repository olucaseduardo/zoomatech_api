package com.olucaseduardo.zoomatech_api.dto.work_performed;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

public record UpdateWorkPerformedRequestDTO(

        MultipartFile photo,
        String title,
        String description,
        String performedAt,
        Set<UUID> serviceIds
) {
}
