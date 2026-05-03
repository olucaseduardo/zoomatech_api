package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.system_configuration.CreateSystemConfigurationDTO;
import com.olucaseduardo.zoomatech_api.dto.system_configuration.UpdateSystemConfigurationDTO;
import com.olucaseduardo.zoomatech_api.entity.SystemConfiguration;
import com.olucaseduardo.zoomatech_api.services.SystemConfigurationService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/configuration/")
public class SystemConfigurationController {

    private final SystemConfigurationService systemConfigurationService;

    @Autowired
    public SystemConfigurationController(SystemConfigurationService systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SystemConfiguration>> createSystemConfiguration(@RequestBody @Valid CreateSystemConfigurationDTO systemConfiguration) {
        SystemConfiguration savedConfig = this.systemConfigurationService.create(systemConfiguration);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success("Configuração Criada com Sucesso!", savedConfig, null));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SystemConfiguration>>> readAllSystemConfiguration() {
        List<SystemConfiguration> allConfigs = this.systemConfigurationService.fetchAll();
        return ResponseEntity.ok(ResponseUtil.success("Buscar realizados com Sucesso!", allConfigs, null));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SystemConfiguration>> updateSystemConfiguration(@PathVariable UUID id, @RequestBody @Valid UpdateSystemConfigurationDTO systemConfiguration) {
        SystemConfiguration updateConfig = this.systemConfigurationService.update(id, systemConfiguration);
        return ResponseEntity.ok(ResponseUtil.success("Configuração atualizada com sucesso!", updateConfig, null));
    }
}
