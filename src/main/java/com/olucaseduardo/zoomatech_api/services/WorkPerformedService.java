package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.work_performed.CreateWorkPerformedRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;
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

    public WorkPerformed findById(UUID id) {
        return this.workPerformedRepository.findById(id).orElse(null);
    }

    public List<WorkPerformed> findAll() {
        return this.workPerformedRepository.findAll();
    }

    public void delete(UUID id) {
        this.workPerformedRepository.deleteById(id);
    }

    public WorkPerformed create(CreateWorkPerformedRequestDTO request) throws IOException {

        Set<com.olucaseduardo.zoomatech_api.entity.Service> services = new HashSet<>(this.serviceRepository.findAllById(request.serviceIds().stream().toList()));

        WorkPerformed workPerformed = WorkPerformed.builder()
                .photo(request.photo().getBytes())
                .description(request.description())
                .title(request.title())
                .performedAt(request.performedAt())
                .services(services)
                .build();

        return this.workPerformedRepository.save(workPerformed);
    }

    public WorkPerformed update(UUID id, CreateWorkPerformedRequestDTO request) throws IOException {
        WorkPerformed workPerformed = this.workPerformedRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("O trabalho realizado não foi encontrado com o ID " + id));

        Set<com.olucaseduardo.zoomatech_api.entity.Service> services = new HashSet<>(this.serviceRepository.findAllById(request.serviceIds().stream().toList()));

        WorkPerformed.WorkPerformedBuilder updatePerformed = WorkPerformed.builder()
                .id(workPerformed.getId())
                .description(request.description() != null && !request.description().isBlank() ? request.description() : workPerformed.getDescription())
                .title(request.title() != null && !request.title().isBlank() ? request.title() : workPerformed.getTitle())
                .performedAt(request.performedAt() != null && !request.performedAt().isBlank() ? request.performedAt() : workPerformed.getPerformedAt())
                .services(!request.serviceIds().isEmpty() ? services : workPerformed.getServices());

        if (request.photo() != null && !request.photo().isEmpty()) {
            updatePerformed.photo(request.photo().getBytes());
        } else {
            updatePerformed.photo(workPerformed.getPhoto());
        }


        return this.workPerformedRepository.save(updatePerformed.build());
    }

}
