package com.olucaseduardo.zoomatech_api.dto.evento;

import com.olucaseduardo.zoomatech_api.entity.CategoriaEvento;
import com.olucaseduardo.zoomatech_api.entity.Evento;

import java.time.LocalDate;
import java.util.UUID;

public record EventoResponseDTO(
        UUID id,
        String photo,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        CategoriaEvento categoria
) {
    public EventoResponseDTO(Evento evento) {
        this(evento.getId(), evento.getPhoto(), evento.getTitle(), evento.getDescription(),
                evento.getStartDate(), evento.getEndDate(), evento.getCategoria());
    }
}
