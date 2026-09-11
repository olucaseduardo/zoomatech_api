package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.metrics.CreateInteractionRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.metrics.DashboardMetricsResponseDTO;
import com.olucaseduardo.zoomatech_api.dto.metrics.DashboardMetricsResponseDTO.*;
import com.olucaseduardo.zoomatech_api.entity.SiteInteraction;
import com.olucaseduardo.zoomatech_api.repository.SiteInteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteMetricService {

    private final SiteInteractionRepository siteInteractionRepository;

    @Transactional
    public void recordInteraction(CreateInteractionRequestDTO dto) {
        String tipo = dto.tipo() != null ? dto.tipo().trim().toUpperCase() : "CLICK";
        String categoria = dto.categoria() != null ? dto.categoria().trim().toUpperCase() : "GERAL";
        String acao = dto.acao() != null ? dto.acao().trim().toUpperCase() : "UNKNOWN";

        SiteInteraction interaction = SiteInteraction.builder()
                .tipo(tipo)
                .categoria(categoria)
                .acao(acao)
                .rotulo(dto.rotulo())
                .referenciaId(dto.referenciaId())
                .origemPagina(dto.origemPagina())
                .build();

        this.siteInteractionRepository.save(interaction);
    }

    @Transactional(readOnly = true)
    public DashboardMetricsResponseDTO getDashboardMetrics() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(6).atStartOfDay();

        long totalViews = siteInteractionRepository.countByTipo("VIEW");
        long totalClicks = siteInteractionRepository.countByTipo("CLICK");
        long viewsHoje = siteInteractionRepository.countByTipoAndCreatedAtGreaterThanEqual("VIEW", startOfToday);
        long clicksHoje = siteInteractionRepository.countByTipoAndCreatedAtGreaterThanEqual("CLICK", startOfToday);
        long whatsappClicks = siteInteractionRepository.countByAcao("WHATSAPP_CLICK");
        long editalDownloads = siteInteractionRepository.countByAcao("EDITAL_DOWNLOAD");
        long editalInscricoes = siteInteractionRepository.countByAcao("EDITAL_INSCRICAO");

        // Top ações
        List<Object[]> topAcoesRaw = siteInteractionRepository.findTopAcoes(PageRequest.of(0, 8));
        List<AcaoContagemDTO> topAcoes = topAcoesRaw.stream()
                .map(row -> new AcaoContagemDTO((String) row[0], (String) row[1], ((Number) row[2]).longValue()))
                .toList();

        // Últimos 7 dias
        List<SiteInteraction> lastWeekEvents = siteInteractionRepository.findByCreatedAtGreaterThanEqualOrderByCreatedAtAsc(sevenDaysAgo);
        Map<LocalDate, Map<String, Long>> dailyMap = lastWeekEvents.stream()
                .collect(Collectors.groupingBy(
                        i -> i.getCreatedAt().toLocalDate(),
                        Collectors.groupingBy(SiteInteraction::getTipo, Collectors.counting())
                ));

        List<MetricaDiariaDTO> ultimos7Dias = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            Map<String, Long> counts = dailyMap.getOrDefault(date, Collections.emptyMap());
            long views = counts.getOrDefault("VIEW", 0L);
            long clicks = counts.getOrDefault("CLICK", 0L);
            ultimos7Dias.add(new MetricaDiariaDTO(date, views, clicks));
        }

        // Últimas interações
        List<RecenteInteracaoDTO> ultimasInteracoes = siteInteractionRepository.findTop15ByOrderByCreatedAtDesc()
                .stream()
                .map(i -> new RecenteInteracaoDTO(
                        i.getId(),
                        i.getTipo(),
                        i.getCategoria(),
                        i.getAcao(),
                        i.getRotulo(),
                        i.getOrigemPagina(),
                        i.getCreatedAt()
                ))
                .toList();

        return new DashboardMetricsResponseDTO(
                totalViews,
                totalClicks,
                viewsHoje,
                clicksHoje,
                whatsappClicks,
                editalDownloads,
                editalInscricoes,
                topAcoes,
                ultimos7Dias,
                ultimasInteracoes
        );
    }
}
