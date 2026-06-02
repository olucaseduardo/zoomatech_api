package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.evento.CreateEventoRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.evento.EventoResponseDTO;
import com.olucaseduardo.zoomatech_api.dto.evento.EventosClassificadosResponseDTO;
import com.olucaseduardo.zoomatech_api.dto.evento.UpdateEventoRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.CategoriaEvento;
import com.olucaseduardo.zoomatech_api.entity.Evento;
import com.olucaseduardo.zoomatech_api.exceptions.BadRequestException;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final StorageService storageService;

    public Evento findById(UUID id) {
        return this.eventoRepository.findById(id).orElse(null);
    }

    public EventosClassificadosResponseDTO findAll() {
        List<EventoResponseDTO> participacao = eventoRepository
                .findAllByCategoriaOrderByStartDateDesc(CategoriaEvento.PARTICIPACAO)
                .stream().map(EventoResponseDTO::new).toList();

        List<EventoResponseDTO> realizados = eventoRepository
                .findAllByCategoriaOrderByStartDateDesc(CategoriaEvento.REALIZADOS)
                .stream().map(EventoResponseDTO::new).toList();

        return new EventosClassificadosResponseDTO(participacao, realizados);
    }

    public void delete(UUID id) {
        this.eventoRepository.deleteById(id);
    }

    public Evento create(CreateEventoRequestDTO request) throws IOException {
        LocalDate startDate = LocalDate.parse(request.startDate());
        LocalDate endDate = LocalDate.parse(request.endDate());

        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("A data de término não pode ser anterior à data de início!");
        }

        var photoPath = storageService.uploadFile(request.photo())
                .orElseThrow(() -> new BadRequestException("Erro ao armazenar a foto no sistema!"));

        Evento evento = Evento.builder()
                .photo(photoPath)
                .title(request.title())
                .description(request.description())
                .startDate(startDate)
                .endDate(endDate)
                .categoria(request.categoria())
                .build();

        return this.eventoRepository.save(evento);
    }

    public Evento update(UUID id, UpdateEventoRequestDTO request) throws IOException {
        Evento evento = this.eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com o ID " + id));

        LocalDate startDate = request.startDate() != null && !request.startDate().isBlank()
                ? LocalDate.parse(request.startDate()) : evento.getStartDate();
        LocalDate endDate = request.endDate() != null && !request.endDate().isBlank()
                ? LocalDate.parse(request.endDate()) : evento.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("A data de término não pode ser anterior à data de início!");
        }

        Evento.EventoBuilder updatedEvento = Evento.builder()
                .id(evento.getId())
                .title(request.title() != null && !request.title().isBlank() ? request.title() : evento.getTitle())
                .description(request.description() != null && !request.description().isBlank() ? request.description() : evento.getDescription())
                .startDate(startDate)
                .endDate(endDate)
                .categoria(request.categoria() != null ? request.categoria() : evento.getCategoria());

        if (request.photo() != null && !request.photo().isEmpty()) {
            var photoPath = storageService.replaceFile(request.photo(), evento.getPhoto())
                    .orElseThrow(() -> new BadRequestException("Erro ao armazenar a foto no sistema!"));
            updatedEvento.photo(photoPath);
        } else {
            updatedEvento.photo(evento.getPhoto());
        }

        return this.eventoRepository.save(updatedEvento.build());
    }
}
