package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.service.CreateServiceRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.service.UpdateServiceRequestDTO;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public com.olucaseduardo.zoomatech_api.entity.Service create(CreateServiceRequestDTO data) {
        var service = com.olucaseduardo.zoomatech_api.entity.Service.builder()
                .name(data.name())
                .description(data.description())
                .icon(data.icon())
                .build();

        return this.serviceRepository.save(service);
    }

    @Transactional(readOnly = true)
    public com.olucaseduardo.zoomatech_api.entity.Service findById(UUID id) {
        return this.serviceRepository.findById(id).orElse(null);
    }

    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public void deleteById(UUID id) {
        this.serviceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<com.olucaseduardo.zoomatech_api.entity.Service> findAll() {
        return this.serviceRepository.findAllByOrderByCreatedAtAsc();
    }


    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public com.olucaseduardo.zoomatech_api.entity.Service update(UUID id, UpdateServiceRequestDTO data) {
        com.olucaseduardo.zoomatech_api.entity.Service service = this.serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id " + id));

        com.olucaseduardo.zoomatech_api.entity.Service.ServiceBuilder builder = com.olucaseduardo.zoomatech_api.entity.Service.builder();

        builder.id(service.getId());
        builder.icon(data.icon() != null ? data.icon() : service.getIcon());
        builder.description(data.description() != null ? data.description() : service.getDescription());
        builder.name(data.name() != null ? data.name() : service.getName());
        builder.serviceTopic(service.getServiceTopic());
        builder.workPerformeds(service.getWorkPerformeds());
        builder.createdAt(service.getCreatedAt());

        com.olucaseduardo.zoomatech_api.entity.Service updateService = builder.build();

        return this.serviceRepository.save(updateService);
    }
}
