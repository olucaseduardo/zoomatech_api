package com.olucaseduardo.zoomatech_api.dto.evento;

import com.olucaseduardo.zoomatech_api.entity.CategoriaEvento;
import org.springframework.web.multipart.MultipartFile;

public record UpdateEventoRequestDTO(
        MultipartFile photo,
        String title,
        String description,
        String startDate,
        String endDate,
        CategoriaEvento categoria
) {
}
