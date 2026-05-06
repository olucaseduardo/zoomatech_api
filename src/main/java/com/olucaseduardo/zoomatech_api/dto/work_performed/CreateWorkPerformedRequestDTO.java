package com.olucaseduardo.zoomatech_api.dto.work_performed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

public record CreateWorkPerformedRequestDTO(

        @NotNull(message = "A foto é obrigatória")
        MultipartFile photo,

        @NotBlank(message = "O titulo é obrigatório")
        String title,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotBlank(message = "A data de realização é obrigatória")
        String performedAt,

        @NotEmpty(message = "Ao menos um serviço deve ser selecionado")
        Set<UUID> serviceIds
) {
}
