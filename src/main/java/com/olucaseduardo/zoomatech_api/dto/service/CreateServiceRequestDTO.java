package com.olucaseduardo.zoomatech_api.dto.service;

import jakarta.validation.constraints.NotBlank;

public record CreateServiceRequestDTO(
        @NotBlank(message = "O ícone a ser exibido é obrigatório")
        String icon,
        @NotBlank(message = "A descrição do serviço é obrigatória")
        String description,
        @NotBlank(message = "O nome do serviço é obrigatório")
        String name
) {
}
