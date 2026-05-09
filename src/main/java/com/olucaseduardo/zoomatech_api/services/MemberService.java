package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.team.CreateMemberRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.Member;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.entity.Role;
import com.olucaseduardo.zoomatech_api.repository.MemberRepository;
import com.olucaseduardo.zoomatech_api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    public Member create(CreateMemberRequestDTO request) throws IOException {
        Role role = roleRepository.findById(request.role())
                .orElseThrow(() -> new ResourceNotFoundException("Função não encontrada com id " + request.role()));

        Member member = Member.builder()
                .name(request.name())
                .photo(request.photo().getBytes())
                .description(request.description())
                .category(request.category())
                .active(request.active())
                .lattes(request.lattes())
                .role(role)
                .build();
        return this.save(member);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(UUID id) {
        return memberRepository.findById(id);
    }

    public Member save(Member team) {
        return memberRepository.save(team);
    }

    public void deleteById(UUID id) {
        Member team = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro do time não encontrado com id " + id));

        this.memberRepository.delete(team);
    }

    public Member update(UUID id, CreateMemberRequestDTO request) throws IOException {
        Member existingTeam = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro do time não encontrado com id " + id));

        Member.MemberBuilder updatedTeamBuilder = Member.builder()
                .id(existingTeam.getId());

        updatedTeamBuilder.name(request.name() != null && !request.name().isBlank() ? request.name() : existingTeam.getName());

        if (request.role() != null) {
            Role role = roleRepository.findById(request.role())
                    .orElseThrow(() -> new ResourceNotFoundException("Função não encontrada com id " + request.role()));
            updatedTeamBuilder.role(role);
        } else {
            updatedTeamBuilder.role(existingTeam.getRole());
        }
        updatedTeamBuilder.description(request.description() != null && !request.description().isBlank() ? request.description() : existingTeam.getDescription());
        updatedTeamBuilder.active(request.active() != null ? request.active() : existingTeam.getActive());
        updatedTeamBuilder.lattes(request.lattes() != null && !request.lattes().isBlank() ? request.lattes() : existingTeam.getLattes());
        updatedTeamBuilder.category(request.category() != null ? request.category() : existingTeam.getCategory());

        if (request.photo() != null && !request.photo().isEmpty()) {
            updatedTeamBuilder.photo(request.photo().getBytes());
        } else {
            updatedTeamBuilder.photo(existingTeam.getPhoto());
        }

        return memberRepository.save(updatedTeamBuilder.build());
    }
}
