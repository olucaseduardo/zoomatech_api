package com.olucaseduardo.zoomatech_api.repository;

import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkPerformedRepository extends JpaRepository<WorkPerformed, UUID> {
    List<WorkPerformed> findAllByOrderByCreatedAtDesc();
}
