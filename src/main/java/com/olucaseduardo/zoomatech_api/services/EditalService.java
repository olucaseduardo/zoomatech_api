package com.olucaseduardo.zoomatech_api.services;

import com.olucaseduardo.zoomatech_api.dto.edital.*;
import com.olucaseduardo.zoomatech_api.entity.*;
import com.olucaseduardo.zoomatech_api.exceptions.BadRequestException;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.repository.EditalRepository;
import com.olucaseduardo.zoomatech_api.repository.EditalTimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EditalService {

    private final EditalRepository editalRepository;
    private final EditalTimelineRepository editalTimelineRepository;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public List<EditalResponseDTO> findAll() {
        return this.editalRepository.findAllByOrderByDataPublicacaoDesc()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Edital findById(UUID id) {
        return this.editalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edital não encontrado com o ID " + id));
    }

    @Transactional(readOnly = true)
    public EditalResponseDTO findDTOById(UUID id) {
        return toDTO(findById(id));
    }

    public EditalResponseDTO toDTO(Edital edital) {
        return new EditalResponseDTO(edital);
    }

    public EditalTimelineResponseDTO toTimelineDTO(EditalTimeline item) {
        return new EditalTimelineResponseDTO(item);
    }

    @Transactional(readOnly = true)
    public String getDownloadUrl(UUID editalId) {
        Edital edital = findById(editalId);
        return storageService.generatePresignedUrl(edital.getArquivoPath(), 60);
    }

    @Transactional(readOnly = true)
    public String getTimelineDownloadUrl(UUID editalId, UUID itemId) {
        Edital edital = findById(editalId);
        EditalTimeline item = edital.getTimeline().stream()
                .filter(t -> t.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item da timeline não encontrado com o ID " + itemId));
        if (item.getArquivoPath() == null || item.getArquivoPath().isBlank()) {
            throw new BadRequestException("Este item da timeline não possui arquivo anexado.");
        }
        return storageService.generatePresignedUrl(item.getArquivoPath(), 60);
    }

    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public EditalResponseDTO create(CreateEditalRequestDTO request) throws IOException {
        LocalDate dataPublicacao = LocalDate.parse(request.dataPublicacao());
        LocalDate dataEncerramento = (request.dataEncerramento() != null && !request.dataEncerramento().isBlank())
                ? LocalDate.parse(request.dataEncerramento())
                : null;

        if (dataEncerramento != null && dataEncerramento.isBefore(dataPublicacao)) {
            throw new BadRequestException("A data de encerramento não pode ser anterior à data de publicação!");
        }

        if (request.arquivo() == null || request.arquivo().isEmpty()) {
            throw new BadRequestException("O arquivo do edital é obrigatório!");
        }

        String originalName = request.arquivo().getOriginalFilename();
        String contentType = request.arquivo().getContentType();
        long size = request.arquivo().getSize();

        String filePath = storageService.uploadDocument(request.arquivo(), "editais")
                .orElseThrow(() -> new BadRequestException("Erro ao armazenar o arquivo do edital no bucket!"));

        StatusEdital status = request.status() != null ? request.status() : StatusEdital.ABERTO;

        Edital edital = Edital.builder()
                .titulo(request.titulo())
                .numeroEdital(request.numeroEdital())
                .descricao(request.descricao())
                .status(status)
                .categoria(request.categoria())
                .arquivoPath(filePath)
                .nomeOriginal(originalName)
                .contentType(contentType)
                .tamanhoBytes(size)
                .dataPublicacao(dataPublicacao)
                .dataEncerramento(dataEncerramento)
                .linkInscricao(request.linkInscricao())
                .build();

        Edital saved = this.editalRepository.save(edital);

        // Cria automaticamente o 1º evento da timeline: Abertura do Edital
        EditalTimeline abertura = EditalTimeline.builder()
                .edital(saved)
                .titulo("Publicação do Edital")
                .tipo(TipoItemTimeline.EDITAL)
                .descricao("Abertura e publicação oficial do edital no sistema.")
                .dataEvento(dataPublicacao)
                .arquivoPath(filePath)
                .nomeOriginal(originalName)
                .contentType(contentType)
                .tamanhoBytes(size)
                .linkExterno(request.linkInscricao())
                .destaque(false)
                .build();

        this.editalTimelineRepository.save(abertura);

        return findDTOById(saved.getId());
    }

    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public EditalResponseDTO update(UUID id, UpdateEditalRequestDTO request) throws IOException {
        Edital edital = findById(id);

        LocalDate dataPublicacao = request.dataPublicacao() != null && !request.dataPublicacao().isBlank()
                ? LocalDate.parse(request.dataPublicacao())
                : edital.getDataPublicacao();

        LocalDate dataEncerramento = request.dataEncerramento() != null && !request.dataEncerramento().isBlank()
                ? LocalDate.parse(request.dataEncerramento())
                : edital.getDataEncerramento();

        if (dataEncerramento != null && dataEncerramento.isBefore(dataPublicacao)) {
            throw new BadRequestException("A data de encerramento não pode ser anterior à data de publicação!");
        }

        edital.setTitulo(request.titulo() != null && !request.titulo().isBlank() ? request.titulo() : edital.getTitulo());
        edital.setNumeroEdital(request.numeroEdital() != null ? request.numeroEdital() : edital.getNumeroEdital());
        edital.setDescricao(request.descricao() != null && !request.descricao().isBlank() ? request.descricao() : edital.getDescricao());
        if (request.status() != null) edital.setStatus(request.status());
        if (request.categoria() != null) edital.setCategoria(request.categoria());
        edital.setDataPublicacao(dataPublicacao);
        edital.setDataEncerramento(dataEncerramento);
        edital.setLinkInscricao(request.linkInscricao() != null ? request.linkInscricao() : edital.getLinkInscricao());

        if (request.arquivo() != null && !request.arquivo().isEmpty()) {
            String newPath = storageService.replaceDocument(request.arquivo(), edital.getArquivoPath(), "editais")
                    .orElseThrow(() -> new BadRequestException("Erro ao atualizar o arquivo do edital no bucket!"));
            edital.setArquivoPath(newPath);
            edital.setNomeOriginal(request.arquivo().getOriginalFilename());
            edital.setContentType(request.arquivo().getContentType());
            edital.setTamanhoBytes(request.arquivo().getSize());
        }

        this.editalRepository.save(edital);
        return findDTOById(edital.getId());
    }

    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public void delete(UUID id) {
        Edital edital = findById(id);

        // Exclui o arquivo principal do edital
        storageService.deleteFile(edital.getArquivoPath());

        // Exclui todos os arquivos das etapas da timeline
        if (edital.getTimeline() != null) {
            for (EditalTimeline item : edital.getTimeline()) {
                if (item.getArquivoPath() != null) {
                    storageService.deleteFile(item.getArquivoPath());
                }
            }
        }

        this.editalRepository.delete(edital);
    }

    // --- TIMELINE MANAGEMENT ---

    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public EditalTimelineResponseDTO addTimelineItem(UUID editalId, CreateEditalTimelineRequestDTO request) {
        Edital edital = findById(editalId);
        LocalDate dataEvento = LocalDate.parse(request.dataEvento());

        String filePath = null;
        String originalName = null;
        String contentType = null;
        Long tamanhoBytes = null;

        if (request.arquivo() != null && !request.arquivo().isEmpty()) {
            filePath = storageService.uploadDocument(request.arquivo(), "editais/timeline")
                    .orElseThrow(() -> new BadRequestException("Erro ao salvar arquivo da etapa no bucket!"));
            originalName = request.arquivo().getOriginalFilename();
            contentType = request.arquivo().getContentType();
            tamanhoBytes = request.arquivo().getSize();
        }

        EditalTimeline item = EditalTimeline.builder()
                .edital(edital)
                .titulo(request.titulo())
                .tipo(request.tipo())
                .descricao(request.descricao())
                .dataEvento(dataEvento)
                .arquivoPath(filePath)
                .nomeOriginal(originalName)
                .contentType(contentType)
                .tamanhoBytes(tamanhoBytes)
                .linkExterno(request.linkExterno())
                .destaque(request.destaque() != null && request.destaque())
                .build();

        EditalTimeline saved = this.editalTimelineRepository.save(item);
        return toTimelineDTO(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public EditalTimelineResponseDTO updateTimelineItem(UUID editalId, UUID itemId, UpdateEditalTimelineRequestDTO request) {
        findById(editalId); // garante existência do edital
        EditalTimeline item = this.editalTimelineRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Etapa da timeline não encontrada com ID " + itemId));

        if (request.titulo() != null && !request.titulo().isBlank()) {
            item.setTitulo(request.titulo());
        }
        if (request.tipo() != null) {
            item.setTipo(request.tipo());
        }
        if (request.descricao() != null) {
            item.setDescricao(request.descricao());
        }
        if (request.dataEvento() != null && !request.dataEvento().isBlank()) {
            item.setDataEvento(LocalDate.parse(request.dataEvento()));
        }
        if (request.linkExterno() != null) {
            item.setLinkExterno(request.linkExterno());
        }
        if (request.destaque() != null) {
            item.setDestaque(request.destaque());
        }

        if (request.arquivo() != null && !request.arquivo().isEmpty()) {
            String newPath = storageService.replaceDocument(request.arquivo(), item.getArquivoPath(), "editais/timeline")
                    .orElseThrow(() -> new BadRequestException("Erro ao substituir arquivo da etapa no bucket!"));
            item.setArquivoPath(newPath);
            item.setNomeOriginal(request.arquivo().getOriginalFilename());
            item.setContentType(request.arquivo().getContentType());
            item.setTamanhoBytes(request.arquivo().getSize());
        }

        EditalTimeline saved = this.editalTimelineRepository.save(item);
        return toTimelineDTO(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "homepage", allEntries = true)
    public void deleteTimelineItem(UUID editalId, UUID itemId) {
        findById(editalId);
        EditalTimeline item = this.editalTimelineRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Etapa da timeline não encontrada com ID " + itemId));

        if (item.getArquivoPath() != null) {
            storageService.deleteFile(item.getArquivoPath());
        }

        this.editalTimelineRepository.delete(item);
    }
}
