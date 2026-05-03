package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.system_configuration.CreateSystemConfigurationDTO;
import com.olucaseduardo.zoomatech_api.dto.system_configuration.UpdateSystemConfigurationDTO;
import com.olucaseduardo.zoomatech_api.entity.SystemConfiguration;
import com.olucaseduardo.zoomatech_api.exceptions.BadRequestException;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.repository.SystemConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemConfigurationService {

    private final SystemConfigurationRepository systemConfigurationRepository;

    public List<SystemConfiguration> fetchAll() {
        return systemConfigurationRepository.findAll();
    }

    @Transactional
    public SystemConfiguration create(CreateSystemConfigurationDTO newConfiguration) {

        this.systemConfigurationRepository.findSystemConfigurationByKey(newConfiguration.key()).ifPresent(config -> {
            throw new BadRequestException("Configuração com a chave '" + newConfiguration.key() + "' já existe.");
        });

        final SystemConfiguration systemConfiguration = SystemConfiguration.builder()
                .key(newConfiguration.key())
                .value(newConfiguration.value())
                .description(newConfiguration.description())
                .build();
        return this.systemConfigurationRepository.save(systemConfiguration);
    }

    public SystemConfiguration update(UUID id, UpdateSystemConfigurationDTO fields) {
        return systemConfigurationRepository.findById(id)
                .map(systemConfiguration -> {
                    systemConfiguration.setValue(fields.value());
                    systemConfiguration.setDescription(fields.description());
                    return systemConfigurationRepository.save(systemConfiguration);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Configuração", id));
    }
}
