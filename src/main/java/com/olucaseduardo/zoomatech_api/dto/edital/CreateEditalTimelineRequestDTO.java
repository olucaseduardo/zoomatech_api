package com.olucaseduardo.zoomatech_api.dto.edital;

import com.olucaseduardo.zoomatech_api.entity.TipoItemTimeline;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateEditalTimelineRequestDTO(
        @NotBlank(message = "O título da etapa é obrigatório!")
        String titulo,

        @NotNull(message = "O tipo da etapa é obrigatório!")
        TipoItemTimeline tipo,

        String descricao,

        @NotBlank(message = "A data do evento é obrigatória!")
        String dataEvento,

        String linkExterno,

        Boolean destaque,

        MultipartFile arquivo
) {
}
