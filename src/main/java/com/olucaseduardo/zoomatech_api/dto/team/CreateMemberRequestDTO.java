package com.olucaseduardo.zoomatech_api.dto.team;

import com.olucaseduardo.zoomatech_api.entity.CategoryMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateMemberRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotNull(message = "A foto é obrigatória")
        MultipartFile photo,

        @NotNull(message = "O cargo é obrigatório")
        UUID role,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @NotNull(message = "O status de atividade é obrigatório")
        Boolean active,

        @NotBlank(message = "O link do Lattes é obrigatório")
        String lattes,

        @NotNull(message = "A categoria do membro é obrigatória") CategoryMember category
) {
}
