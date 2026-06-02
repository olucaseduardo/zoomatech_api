package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.evento.CreateEventoRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.evento.EventosClassificadosResponseDTO;
import com.olucaseduardo.zoomatech_api.dto.evento.UpdateEventoRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.Evento;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.services.EventoService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Evento>> create(@ModelAttribute @Valid CreateEventoRequestDTO request) throws IOException {
        Evento evento = eventoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Evento criado com sucesso!", evento, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<EventosClassificadosResponseDTO>> findAll() {
        EventosClassificadosResponseDTO eventos = eventoService.findAll();
        return ResponseEntity.ok(ResponseUtil.success("Eventos listados com sucesso!", eventos, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Evento>> findById(@PathVariable UUID id) {
        Evento evento = eventoService.findById(id);
        if (evento == null) {
            throw new ResourceNotFoundException("Evento não encontrado com o ID " + id);
        }
        return ResponseEntity.ok(ResponseUtil.success("Evento encontrado com sucesso!", evento, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Evento>> update(@PathVariable UUID id,
            @ModelAttribute UpdateEventoRequestDTO request) throws IOException {
        Evento evento = eventoService.update(id, request);
        return ResponseEntity.ok(ResponseUtil.success("Evento atualizado com sucesso!", evento, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        eventoService.delete(id);
        return ResponseEntity.ok(ResponseUtil.success("Evento removido com sucesso!", null, null));
    }
}
