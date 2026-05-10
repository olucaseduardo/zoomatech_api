package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.team.CreateMemberRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.Member;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.services.MemberService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Member>> save(@Valid @ModelAttribute CreateMemberRequestDTO request) throws IOException {
        Member createdMember = memberService.create(request);
        ApiResponse<Member> apiResponse = ResponseUtil.success("Membro criado com sucesso!", createdMember, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Member>>> findAll() {
        List<Member> Members = memberService.findAll();
        ApiResponse<List<Member>> apiResponse = ResponseUtil.success("Membros listados com sucesso!", Members, null);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Member>> findById(@PathVariable UUID id) {
        Member Member = memberService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro do time não encontrado com id " + id));
        ApiResponse<Member> apiResponse = ResponseUtil.success("Membro encontrado com sucesso!", Member, null);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Member>> update(@PathVariable UUID id, @ModelAttribute com.olucaseduardo.zoomatech_api.dto.team.CreateMemberRequestDTO request) throws IOException {
        Member updatedMember = memberService.update(id, request);
        ApiResponse<Member> apiResponse = ResponseUtil.success("Membro atualizado com sucesso!", updatedMember, null);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable UUID id) {
        memberService.deleteById(id);
        ApiResponse<Void> apiResponse = ResponseUtil.success("Membro desativado com sucesso!", null, null);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID id) {
        Member member = this.memberService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com o ID " + id));

        return ResponseEntity.ok().build();

    }
}
