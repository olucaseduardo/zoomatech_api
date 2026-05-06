package com.olucaseduardo.zoomatech_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "service_topic_id")
    private UUID id;

    @Column(nullable = false)
    private String topic;

    private String description;

    @ElementCollection
    private List<String> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    @JsonBackReference
    private Service service;
}
