package com.olucaseduardo.zoomatech_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkPerformed {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String photo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String performedAt;

    @ManyToMany
    @JoinTable(
            name = "service_performed",
            joinColumns = @JoinColumn(name = "work_performed_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @JsonBackReference
    private Set<Service> services;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
