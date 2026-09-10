package com.olucaseduardo.zoomatech_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "edital")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Edital {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "numero_edital")
    private String numeroEdital;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEdital status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaEdital categoria;

    @Column(name = "arquivo_path", nullable = false)
    private String arquivoPath;

    @Column(name = "nome_original")
    private String nomeOriginal;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;

    @Column(name = "data_encerramento")
    private LocalDate dataEncerramento;

    @Column(name = "link_inscricao")
    private String linkInscricao;

    @OneToMany(mappedBy = "edital", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dataEvento DESC, createdAt DESC")
    @Builder.Default
    private List<EditalTimeline> timeline = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
