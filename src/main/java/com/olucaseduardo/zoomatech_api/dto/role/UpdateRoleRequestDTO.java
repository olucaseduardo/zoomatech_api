package com.olucaseduardo.zoomatech_api.dto.role;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateRoleRequestDTO(
        @Size(max = 50, message = "O nome da função deve ter no máximo 50 caracteres")
        String name,

        @Size(max = 255, message = "A descrição da função deve ter no máximo 255 caracteres")
        String description,

        @Positive(message = "A ordem deve ser um número positivo")
        Integer order
) {
}
