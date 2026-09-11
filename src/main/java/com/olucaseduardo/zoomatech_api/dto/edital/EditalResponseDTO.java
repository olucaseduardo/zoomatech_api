package com.olucaseduardo.zoomatech_api.dto.edital;

import com.olucaseduardo.zoomatech_api.entity.CategoriaEdital;
import com.olucaseduardo.zoomatech_api.entity.Edital;
import com.olucaseduardo.zoomatech_api.entity.StatusEdital;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EditalResponseDTO(
        UUID id,
        String titulo,
        String numeroEdital,
        String descricao,
        StatusEdital status,
        CategoriaEdital categoria,
        String arquivoPath,
        String arquivoUrl,
        String nomeOriginal,
        String contentType,
        Long tamanhoBytes,
        LocalDate dataPublicacao,
        LocalDate dataEncerramento,
        String linkInscricao,
        List<EditalTimelineResponseDTO> timeline,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public EditalResponseDTO(Edital edital) {
        this(edital, null);
    }

    public EditalResponseDTO(Edital edital, String arquivoUrl) {
        this(
                edital.getId(),
                edital.getTitulo(),
                edital.getNumeroEdital(),
                edital.getDescricao(),
                edital.getStatus(),
                edital.getCategoria(),
                edital.getArquivoPath(),
                arquivoUrl != null ? arquivoUrl : edital.getArquivoPath(),
                edital.getNomeOriginal(),
                edital.getContentType(),
                edital.getTamanhoBytes(),
                edital.getDataPublicacao(),
                edital.getDataEncerramento(),
                edital.getLinkInscricao(),
                edital.getTimeline() != null
                        ? edital.getTimeline().stream().map(EditalTimelineResponseDTO::new).toList()
                        : List.of(),
                edital.getCreatedAt(),
                edital.getUpdatedAt()
        );
    }
}
