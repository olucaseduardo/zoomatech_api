package com.olucaseduardo.zoomatech_api.repository;

import com.olucaseduardo.zoomatech_api.entity.ServiceTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceTopicRepository extends JpaRepository<ServiceTopic, UUID> {

}
