package com.olucaseduardo.zoomatech_api.dto.edital;

import com.olucaseduardo.zoomatech_api.entity.TipoItemTimeline;
import org.springframework.web.multipart.MultipartFile;

public record UpdateEditalTimelineRequestDTO(
        String titulo,
        TipoItemTimeline tipo,
        String descricao,
        String dataEvento,
        String linkExterno,
        Boolean destaque,
        MultipartFile arquivo
) {
}
