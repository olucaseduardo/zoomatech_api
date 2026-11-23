package com.olucaseduardo.zoomatech_api.repository;

import com.olucaseduardo.zoomatech_api.entity.Service;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    @EntityGraph(attributePaths = {"serviceTopic", "serviceTopic.items", "workPerformeds"})
    List<Service> findAllByOrderByCreatedAtAsc();

    @Override
    @EntityGraph(attributePaths = {"serviceTopic", "serviceTopic.items", "workPerformeds"})
    Optional<Service> findById(UUID id);
}
