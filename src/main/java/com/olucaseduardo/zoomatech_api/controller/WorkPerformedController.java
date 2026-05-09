package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.work_performed.CreateWorkPerformedRequestDTO;
import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.services.WorkPerformedService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/work-performed")
@RequiredArgsConstructor
public class WorkPerformedController {

    private final WorkPerformedService workPerformedService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WorkPerformed>> createWorkPerformed(@ModelAttribute @Valid CreateWorkPerformedRequestDTO request) throws IOException {
        WorkPerformed workPerformed = workPerformedService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success("Trabalho realizado criado com sucesso!", workPerformed, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkPerformed>>> findAll() {
        List<WorkPerformed> works = workPerformedService.findAll();
        return ResponseEntity.ok(ResponseUtil.success("Trabalhos realizados listados com sucesso!", works, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkPerformed>> findById(@PathVariable UUID id) {
        WorkPerformed workPerformed = workPerformedService.findById(id);
        if (workPerformed == null) {
            throw new ResourceNotFoundException("Trabalho realizado não encontrado com o ID " + id);
        }
        return ResponseEntity.ok(ResponseUtil.success("Trabalho realizado encontrado com sucesso!", workPerformed, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WorkPerformed>> update(@PathVariable UUID id, @ModelAttribute CreateWorkPerformedRequestDTO request) throws IOException {
        WorkPerformed updatedWork = workPerformedService.update(id, request);
        return ResponseEntity.ok(ResponseUtil.success("Trabalho realizado atualizado com sucesso!", updatedWork, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        workPerformedService.delete(id);
        return ResponseEntity.ok(ResponseUtil.success("Trabalho realizado removido com sucesso!", null, null));
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID id) {
        WorkPerformed work = this.workPerformedService.findById(id);

//        if (work != null) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .body(work.getPhoto());
//        }
        return ResponseEntity.notFound().build();

    }
}
