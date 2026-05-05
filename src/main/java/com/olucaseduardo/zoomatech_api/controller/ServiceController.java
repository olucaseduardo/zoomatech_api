package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.service.CreateServiceRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.service.UpdateServiceRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.Service;
import com.olucaseduardo.zoomatech_api.services.ServiceService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Service>> create(@RequestBody CreateServiceRequestDTO request) {
        var newService = this.service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success("Serviço criado com sucesso!", newService, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Service>> update(@PathVariable UUID id, @RequestBody UpdateServiceRequestDTO request) {
        var updateService = this.service.update(id, request);
        return ResponseEntity.ok(ResponseUtil.success("Serviço atualizado com sucesso!", updateService, null));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Service>> findById(@PathVariable UUID id) {
        var service = this.service.findById(id);
        return ResponseEntity.ok(ResponseUtil.success("Serviço encontrado com sucesso!", service, null));
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Service>>> findAll() {
        var service = this.service.findAll();
        return ResponseEntity.ok(ResponseUtil.success("Serviço encontrado com sucesso!", service, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable UUID id) {
        this.service.deleteById(id);
        return ResponseEntity.ok(ResponseUtil.success("Serviço excluído com sucesso!", null, null));
    }

}
