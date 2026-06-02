package com.olucaseduardo.zoomatech_api.dto.evento;

import com.olucaseduardo.zoomatech_api.entity.CategoriaEvento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateEventoRequestDTO(

        @NotNull(message = "A foto é obrigatória")
        MultipartFile photo,

        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotBlank(message = "A data de início é obrigatória")
        String startDate,

        @NotBlank(message = "A data de término é obrigatória")
        String endDate,

        @NotNull(message = "A categoria é obrigatória")
        CategoriaEvento categoria
) {
}
