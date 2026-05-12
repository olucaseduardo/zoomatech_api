package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.role.CreateRoleRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.role.UpdateRoleRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.Role;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAllByOrderByOrderAsc();
    }

    public Optional<Role> findById(UUID id) {
        return roleRepository.findById(id);
    }

    @Transactional
    public Role create(CreateRoleRequestDTO request) {
        Role role = Role.builder()
                .name(request.name())
                .description(request.description())
                .order(request.order())
                .build();
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(UUID id, UpdateRoleRequestDTO request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Função não encontrada com id " + id));

        if (request.order() != null) {
            Integer oldOrder = role.getOrder();
            Integer newOrder = request.order();

            if (!oldOrder.equals(newOrder)) {
                if (newOrder > oldOrder) {
                    roleRepository.decrementOrderBetween(oldOrder + 1, newOrder);
                } else {
                    roleRepository.incrementOrderBetween(newOrder, oldOrder - 1);
                }
                role.setOrder(newOrder);
            }
        }

        if (request.name() != null) {
            role.setName(request.name());
        }
        if (request.description() != null) {
            role.setDescription(request.description());
        }

        return roleRepository.save(role);
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Função não encontrada com id " + id);
        }
        roleRepository.deleteById(id);
    }
}
