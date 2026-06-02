package com.olucaseduardo.zoomatech_api.dto.evento;

import java.util.List;

public record EventosClassificadosResponseDTO(
        List<EventoResponseDTO> participacao,
        List<EventoResponseDTO> realizados
) {
}
