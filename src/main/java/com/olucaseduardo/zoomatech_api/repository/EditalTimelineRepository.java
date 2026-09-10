package com.olucaseduardo.zoomatech_api.repository;

import com.olucaseduardo.zoomatech_api.entity.EditalTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EditalTimelineRepository extends JpaRepository<EditalTimeline, UUID> {
    List<EditalTimeline> findAllByEditalIdOrderByDataEventoDescCreatedAtDesc(UUID editalId);
}
