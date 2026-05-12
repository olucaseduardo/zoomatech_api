package com.olucaseduardo.zoomatech_api.repository;

import com.olucaseduardo.zoomatech_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findAllByOrderByOrderAsc();

    @Modifying
    @Query("UPDATE Role r SET r.order = r.order + 1 WHERE r.order >= :start AND r.order <= :end")
    void incrementOrderBetween(Integer start, Integer end);

    @Modifying
    @Query("UPDATE Role r SET r.order = r.order - 1 WHERE r.order >= :start AND r.order <= :end")
    void decrementOrderBetween(Integer start, Integer end);
}
