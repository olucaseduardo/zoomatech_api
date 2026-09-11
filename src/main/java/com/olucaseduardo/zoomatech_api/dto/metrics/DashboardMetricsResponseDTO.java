package com.olucaseduardo.zoomatech_api.dto.metrics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DashboardMetricsResponseDTO(
        long totalVisualizacoes,
        long totalCliques,
        long visualizacoesHoje,
        long cliquesHoje,
        long cliquesWhatsapp,
        long downloadsEditais,
        long inscricoesEditais,
        List<AcaoContagemDTO> topAcoes,
        List<MetricaDiariaDTO> ultimos7Dias,
        List<RecenteInteracaoDTO> ultimasInteracoes
) {
    public record AcaoContagemDTO(String acao, String categoria, long total) {}
    public record MetricaDiariaDTO(LocalDate data, long views, long clicks) {}
    public record RecenteInteracaoDTO(UUID id, String tipo, String categoria, String acao, String rotulo, String origemPagina, LocalDateTime createdAt) {}
}
