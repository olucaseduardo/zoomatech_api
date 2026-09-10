package com.olucaseduardo.zoomatech_api.dto.edital;

import com.olucaseduardo.zoomatech_api.entity.CategoriaEdital;
import com.olucaseduardo.zoomatech_api.entity.StatusEdital;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateEditalRequestDTO(
        @NotBlank(message = "O título é obrigatório!")
        String titulo,

        String numeroEdital,

        @NotBlank(message = "A descrição é obrigatória!")
        String descricao,

        StatusEdital status,

        @NotNull(message = "A categoria é obrigatória!")
        CategoriaEdital categoria,

        @NotBlank(message = "A data de publicação é obrigatória!")
        String dataPublicacao,

        String dataEncerramento,

        String linkInscricao,

        @NotNull(message = "O arquivo do edital é obrigatório!")
        MultipartFile arquivo
) {
}
