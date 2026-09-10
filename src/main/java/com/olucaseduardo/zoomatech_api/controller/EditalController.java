package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.edital.*;
import com.olucaseduardo.zoomatech_api.services.EditalService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/editais")
@RequiredArgsConstructor
public class EditalController {

    private final EditalService editalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EditalResponseDTO>>> findAll() {
        List<EditalResponseDTO> editais = editalService.findAll();
        return ResponseEntity.ok(ResponseUtil.success("Editais listados com sucesso!", editais, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EditalResponseDTO>> findById(@PathVariable UUID id) {
        EditalResponseDTO edital = editalService.findDTOById(id);
        return ResponseEntity.ok(ResponseUtil.success("Edital encontrado com sucesso!", edital, null));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EditalResponseDTO>> create(@ModelAttribute @Valid CreateEditalRequestDTO request) throws IOException {
        EditalResponseDTO edital = editalService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Edital criado com sucesso!", edital, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EditalResponseDTO>> update(
            @PathVariable UUID id,
            @ModelAttribute UpdateEditalRequestDTO request
    ) throws IOException {
        EditalResponseDTO edital = editalService.update(id, request);
        return ResponseEntity.ok(ResponseUtil.success("Edital atualizado com sucesso!", edital, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        editalService.delete(id);
        return ResponseEntity.ok(ResponseUtil.success("Edital removido com sucesso!", null, null));
    }

    // --- TIMELINE ENDPOINTS ---

    @PostMapping("/{id}/timeline")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EditalTimelineResponseDTO>> addTimelineItem(
            @PathVariable UUID id,
            @ModelAttribute @Valid CreateEditalTimelineRequestDTO request
    ) {
        EditalTimelineResponseDTO item = editalService.addTimelineItem(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Etapa adicionada à timeline com sucesso!", item, null));
    }

    @PutMapping("/{id}/timeline/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EditalTimelineResponseDTO>> updateTimelineItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId,
            @ModelAttribute UpdateEditalTimelineRequestDTO request
    ) {
        EditalTimelineResponseDTO item = editalService.updateTimelineItem(id, itemId, request);
        return ResponseEntity.ok(ResponseUtil.success("Etapa da timeline atualizada com sucesso!", item, null));
    }

    @DeleteMapping("/{id}/timeline/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTimelineItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId
    ) {
        editalService.deleteTimelineItem(id, itemId);
        return ResponseEntity.ok(ResponseUtil.success("Etapa removida da timeline com sucesso!", null, null));
    }
}
