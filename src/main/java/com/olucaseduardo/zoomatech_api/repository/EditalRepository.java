package com.olucaseduardo.zoomatech_api.repository;

import com.olucaseduardo.zoomatech_api.entity.CategoriaEdital;
import com.olucaseduardo.zoomatech_api.entity.Edital;
import com.olucaseduardo.zoomatech_api.entity.StatusEdital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EditalRepository extends JpaRepository<Edital, UUID> {
    List<Edital> findAllByOrderByDataPublicacaoDesc();
    List<Edital> findAllByStatusOrderByDataPublicacaoDesc(StatusEdital status);
    List<Edital> findAllByCategoriaOrderByDataPublicacaoDesc(CategoriaEdital categoria);
}
