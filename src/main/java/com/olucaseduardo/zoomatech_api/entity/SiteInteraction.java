package com.olucaseduardo.zoomatech_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "site_interaction", indexes = {
        @Index(name = "idx_site_interaction_created_at", columnList = "created_at"),
        @Index(name = "idx_site_interaction_tipo", columnList = "tipo"),
        @Index(name = "idx_site_interaction_categoria", columnList = "categoria"),
        @Index(name = "idx_site_interaction_acao", columnList = "acao")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false, length = 100)
    private String acao;

    @Column(length = 255)
    private String rotulo;

    @Column(name = "referencia_id", length = 255)
    private String referenciaId;

    @Column(name = "origem_pagina", length = 255)
    private String origemPagina;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
