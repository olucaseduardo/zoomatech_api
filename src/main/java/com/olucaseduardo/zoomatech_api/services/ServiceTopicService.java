package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.service.CreateServiceTopicRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.ServiceTopic;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.repository.ServiceRepository;
import com.olucaseduardo.zoomatech_api.repository.ServiceTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceTopicService {

    private final ServiceTopicRepository serviceTopicRepository;
    private final ServiceRepository serviceRepository;

    public List<ServiceTopic> findAll() {
        return serviceTopicRepository.findAll();
    }

    public ServiceTopic findById(UUID id) {
        Optional<ServiceTopic> serviceTopic = serviceTopicRepository.findById(id);
        return serviceTopic.orElse(null);
    }

    public ServiceTopic create(UUID serviceId, CreateServiceTopicRequestDTO request) {
        com.olucaseduardo.zoomatech_api.entity.Service service = this.serviceRepository.findById(serviceId).orElseThrow(() -> new ResourceNotFoundException("Serviço com o ID " + serviceId + " não encontrado.")
        );

        ServiceTopic serviceTopic = ServiceTopic.builder()
                .topic(request.topic())
                .service(service)
                .description(request.description())
                .items(request.items())
                .build();

        return this.serviceTopicRepository.save(serviceTopic);
    }

    public ServiceTopic update(UUID id, CreateServiceTopicRequestDTO request) {
        ServiceTopic serviceTopic = this.serviceTopicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tópico de serviço com ID" + id + " não encontrado."));

        ServiceTopic updateServiceTopic = ServiceTopic.builder()
                .id(id)
                .topic(request.topic())
                .service(serviceTopic.getService())
                .items(request.items())
                .description(request.description())
                .build();

        return this.serviceTopicRepository.save(updateServiceTopic);
    }

    public void delete(UUID id) {
        this.serviceTopicRepository.deleteById(id);
    }

}
