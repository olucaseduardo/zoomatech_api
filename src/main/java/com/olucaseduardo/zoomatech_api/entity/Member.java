package com.olucaseduardo.zoomatech_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Lob
    @JsonIgnore
    private byte[] photo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private CategoryMember category;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    @URL(protocol = "https", message = "Deve ser uma url correta")
    private String lattes;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Role role;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
