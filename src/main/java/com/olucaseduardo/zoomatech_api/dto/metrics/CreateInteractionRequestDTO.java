package com.olucaseduardo.zoomatech_api.dto.metrics;

import jakarta.validation.constraints.NotBlank;

public record CreateInteractionRequestDTO(
        @NotBlank(message = "O tipo da interação é obrigatório")
        String tipo,
        @NotBlank(message = "A categoria da interação é obrigatória")
        String categoria,
        @NotBlank(message = "A ação da interação é obrigatória")
        String acao,
        String rotulo,
        String referenciaId,
        String origemPagina
) {}
