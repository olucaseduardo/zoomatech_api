package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.role.CreateRoleRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.role.UpdateRoleRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.Role;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.services.RoleService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> create(@RequestBody @Valid CreateRoleRequestDTO request) {
        Role createdRole = roleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Função criada com sucesso!", createdRole, null));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Role>>> findAll() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(ResponseUtil.success("Funções listadas com sucesso!", roles, null));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> findById(@PathVariable UUID id) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Função não encontrada com id " + id));
        return ResponseEntity.ok(ResponseUtil.success("Função encontrada com sucesso!", role, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> update(@PathVariable UUID id, @RequestBody @Valid UpdateRoleRequestDTO request) {
        Role updatedRole = roleService.update(id, request);
        return ResponseEntity.ok(ResponseUtil.success("Função atualizada com sucesso!", updatedRole, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable UUID id) {
        roleService.deleteById(id);
        return ResponseEntity.ok(ResponseUtil.success("Função excluída com sucesso!", null, null));
    }
}
