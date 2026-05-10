package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.work_performed.CreateWorkPerformedRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.work_performed.UpdateWorkPerformedRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.work_performed.WorkPerformedResponseDTO;
import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;
import com.olucaseduardo.zoomatech_api.exceptions.BadRequestException;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.repository.ServiceRepository;
import com.olucaseduardo.zoomatech_api.repository.WorkPerformedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkPerformedService {

    private final WorkPerformedRepository workPerformedRepository;
    private final ServiceRepository serviceRepository;
    private final StorageService storageService;

    public WorkPerformed findById(UUID id) {
        return this.workPerformedRepository.findById(id).orElse(null);
    }

    public List<WorkPerformedResponseDTO> findAll() {
        return this.workPerformedRepository.findAll().stream().map(WorkPerformedResponseDTO::new).toList();
    }

    public void delete(UUID id) {
        this.workPerformedRepository.deleteById(id);
    }

    public WorkPerformed create(CreateWorkPerformedRequestDTO request) throws IOException {

        Set<com.olucaseduardo.zoomatech_api.entity.Service> services = new HashSet<>(this.serviceRepository.findAllById(request.serviceIds().stream().toList()));

        var photoPath = storageService.uploadFile(request.photo()).orElseThrow(() -> new BadRequestException("Erro ao armazenar a foto no sistema!"));

        WorkPerformed workPerformed = WorkPerformed.builder()
                .photo(photoPath)
                .description(request.description())
                .title(request.title())
                .performedAt(request.performedAt())
                .services(services)
                .build();

        return this.workPerformedRepository.save(workPerformed);
    }

    public WorkPerformed update(UUID id, UpdateWorkPerformedRequestDTO request) throws IOException {
        WorkPerformed workPerformed = this.workPerformedRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("O trabalho realizado não foi encontrado com o ID " + id));

        Set<com.olucaseduardo.zoomatech_api.entity.Service> services = new HashSet<>(this.serviceRepository.findAllById(request.serviceIds().stream().toList()));


        WorkPerformed.WorkPerformedBuilder updatePerformed = WorkPerformed.builder()
                .id(workPerformed.getId())
                .description(request.description() != null && !request.description().isBlank() ? request.description() : workPerformed.getDescription())
                .title(request.title() != null && !request.title().isBlank() ? request.title() : workPerformed.getTitle())
                .performedAt(request.performedAt() != null && !request.performedAt().isBlank() ? request.performedAt() : workPerformed.getPerformedAt())
                .services(!request.serviceIds().isEmpty() ? services : workPerformed.getServices());

        if (request.photo() != null && !request.photo().isEmpty()) {
            var photoPath = storageService.uploadFile(request.photo(), workPerformed.getPhoto()).orElseThrow(() -> new BadRequestException("Erro ao armazenar a foto no sistema!"));
            updatePerformed.photo(photoPath);
        } else {
            updatePerformed.photo(workPerformed.getPhoto());
        }


        return this.workPerformedRepository.save(updatePerformed.build());
    }

}
