package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.metrics.DashboardMetricsResponseDTO;
import com.olucaseduardo.zoomatech_api.services.SiteMetricService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class SiteMetricAdminController {

    private final SiteMetricService siteMetricService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardMetricsResponseDTO>> getDashboardMetrics() {
        DashboardMetricsResponseDTO metrics = siteMetricService.getDashboardMetrics();
        return ResponseEntity.ok(ResponseUtil.success("Métricas do dashboard recuperadas com sucesso!", metrics, null));
    }
}
