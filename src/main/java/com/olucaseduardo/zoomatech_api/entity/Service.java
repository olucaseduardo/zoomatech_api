package com.olucaseduardo.zoomatech_api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "service")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String icon;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @OrderBy("createdAt ASC")
    private Set<ServiceTopic> serviceTopic;

    @ManyToMany(mappedBy = "services")
    @JsonManagedReference
    @OrderBy("createdAt DESC")
    private Set<WorkPerformed> workPerformeds;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;
}
