package com.olucaseduardo.zoomatech_api.dto.edital;

import com.olucaseduardo.zoomatech_api.entity.CategoriaEdital;
import com.olucaseduardo.zoomatech_api.entity.StatusEdital;
import org.springframework.web.multipart.MultipartFile;

public record UpdateEditalRequestDTO(
        String titulo,
        String numeroEdital,
        String descricao,
        StatusEdital status,
        CategoriaEdital categoria,
        String dataPublicacao,
        String dataEncerramento,
        String linkInscricao,
        MultipartFile arquivo
) {
}
