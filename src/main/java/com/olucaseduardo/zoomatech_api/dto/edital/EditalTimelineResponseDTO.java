package com.olucaseduardo.zoomatech_api.dto.edital;

import com.olucaseduardo.zoomatech_api.entity.EditalTimeline;
import com.olucaseduardo.zoomatech_api.entity.TipoItemTimeline;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EditalTimelineResponseDTO(
        UUID id,
        String titulo,
        TipoItemTimeline tipo,
        String descricao,
        LocalDate dataEvento,
        String arquivoPath,
        String arquivoUrl,
        String nomeOriginal,
        String contentType,
        Long tamanhoBytes,
        String linkExterno,
        boolean destaque,
        LocalDateTime createdAt
) {
    public EditalTimelineResponseDTO(EditalTimeline item) {
        this(item, null);
    }

    public EditalTimelineResponseDTO(EditalTimeline item, String arquivoUrl) {
        this(
                item.getId(),
                item.getTitulo(),
                item.getTipo(),
                item.getDescricao(),
                item.getDataEvento(),
                item.getArquivoPath(),
                arquivoUrl != null ? arquivoUrl : item.getArquivoPath(),
                item.getNomeOriginal(),
                item.getContentType(),
                item.getTamanhoBytes(),
                item.getLinkExterno(),
                item.isDestaque(),
                item.getCreatedAt()
        );
    }
}
