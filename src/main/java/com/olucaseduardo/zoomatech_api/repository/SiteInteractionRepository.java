package com.olucaseduardo.zoomatech_api.repository;

import com.olucaseduardo.zoomatech_api.entity.SiteInteraction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SiteInteractionRepository extends JpaRepository<SiteInteraction, UUID> {

    long countByTipo(String tipo);

    long countByTipoAndCreatedAtGreaterThanEqual(String tipo, LocalDateTime start);

    long countByAcao(String acao);

    List<SiteInteraction> findTop15ByOrderByCreatedAtDesc();

    List<SiteInteraction> findByCreatedAtGreaterThanEqualOrderByCreatedAtAsc(LocalDateTime start);

    @Query("SELECT s.acao, s.categoria, COUNT(s) " +
           "FROM SiteInteraction s " +
           "GROUP BY s.acao, s.categoria " +
           "ORDER BY COUNT(s) DESC")
    List<Object[]> findTopAcoes(Pageable pageable);
}
