package com.olucaseduardo.zoomatech_api.dto.role;

import jakarta.validation.constraints.*;

public record CreateRoleRequestDTO(
        @NotBlank(message = "O nome da função é obrigatório")
        @Size(max = 50, message = "O nome da função deve ter no máximo 50 caracteres")
        String name,

        @NotBlank(message = "A descrição da função é obrigatória")
        @Size(max = 255, message = "A descrição da função deve ter no máximo 255 caracteres")
        String description,

        @NotNull(message = "Defina uma prioridade para membros com este cargo")
        @Min(0)
        @Positive(message = "A ordem deve ser um número positivo")
        Integer order
) {
}
