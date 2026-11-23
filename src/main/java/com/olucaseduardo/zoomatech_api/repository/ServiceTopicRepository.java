package com.olucaseduardo.zoomatech_api.repository;

import com.olucaseduardo.zoomatech_api.entity.ServiceTopic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceTopicRepository extends JpaRepository<ServiceTopic, UUID> {

    @EntityGraph(attributePaths = "items")
    List<ServiceTopic> findAllByOrderByCreatedAtAsc();

    @Override
    @EntityGraph(attributePaths = "items")
    Optional<ServiceTopic> findById(UUID id);
}
