package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.metrics.CreateInteractionRequestDTO;
import com.olucaseduardo.zoomatech_api.services.SiteMetricService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/interactions")
@RequiredArgsConstructor
public class SiteMetricPublicController {

    private final SiteMetricService siteMetricService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> recordInteraction(@RequestBody @Valid CreateInteractionRequestDTO dto) {
        siteMetricService.recordInteraction(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Interação registrada com sucesso!", null, null));
    }
}
