package com.olucaseduardo.zoomatech_api;

import com.olucaseduardo.zoomatech_api.dto.work_performed.WorkPerformedResponseDTO;
import com.olucaseduardo.zoomatech_api.entity.Service;
import com.olucaseduardo.zoomatech_api.entity.ServiceTopic;
import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;
import com.olucaseduardo.zoomatech_api.repository.ServiceRepository;
import com.olucaseduardo.zoomatech_api.repository.ServiceTopicRepository;
import com.olucaseduardo.zoomatech_api.repository.WorkPerformedRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false"
})
@Import(LazyLoadingOpenInViewFalseTest.TestCacheConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class LazyLoadingOpenInViewFalseTest {

    @TestConfiguration
    static class TestCacheConfig {
        @Bean
        CacheManager cacheManager() {
            return new NoOpCacheManager();
        }
    }

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceTopicRepository serviceTopicRepository;

    @Autowired
    private WorkPerformedRepository workPerformedRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @AfterEach
    void tearDown() {
        transactionTemplate.executeWithoutResult(status -> {
            workPerformedRepository.deleteAll();
            serviceTopicRepository.deleteAll();
            serviceRepository.deleteAll();
        });
    }

    @Test
    void shouldLoadAssociationsWithoutOpenSessionInView() {
        UUID[] ids = transactionTemplate.execute(status -> {
            Service savedService = serviceRepository.save(Service.builder()
                    .name("Consultoria")
                    .description("Consultoria em zootecnia")
                    .icon("icon.svg")
                    .build());

            ServiceTopic savedTopic = serviceTopicRepository.save(ServiceTopic.builder()
                    .topic("Manejo")
                    .description("Manejo nutricional")
                    .items(List.of("Item 1", "Item 2"))
                    .service(savedService)
                    .build());

            WorkPerformed savedWork = workPerformedRepository.save(WorkPerformed.builder()
                    .title("Projeto Fazenda X")
                    .description("Implementação de manejo")
                    .performedAt("2026-01")
                    .photo("photo.jpg")
                    .services(Set.of(savedService))
                    .build());

            return new UUID[]{savedService.getId(), savedTopic.getId(), savedWork.getId()};
        });

        assertThat(ids).isNotNull();
        UUID serviceId = ids[0];
        UUID topicId = ids[1];
        UUID workId = ids[2];

        // 1. WorkPerformedRepository outside any transaction / Hibernate session
        List<WorkPerformed> works = workPerformedRepository.findAllByOrderByCreatedAtDesc();
        assertThat(works).hasSize(1);
        assertThat(Hibernate.isInitialized(works.get(0).getServices())).isTrue();
        WorkPerformedResponseDTO dto = new WorkPerformedResponseDTO(works.get(0));
        assertThat(dto.services()).hasSize(1);
        assertThat(dto.services().get(0).get("name")).isEqualTo("Consultoria");

        WorkPerformed workById = workPerformedRepository.findById(workId).orElseThrow();
        assertThat(Hibernate.isInitialized(workById.getServices())).isTrue();
        assertThat(workById.getServices()).hasSize(1);

        // 2. ServiceRepository outside any transaction / Hibernate session
        List<Service> services = serviceRepository.findAllByOrderByCreatedAtAsc();
        assertThat(services).hasSize(1);
        assertThat(Hibernate.isInitialized(services.get(0).getWorkPerformeds())).isTrue();
        assertThat(services.get(0).getWorkPerformeds()).hasSize(1);
        assertThat(Hibernate.isInitialized(services.get(0).getServiceTopic())).isTrue();
        ServiceTopic loadedTopicFromService = services.get(0).getServiceTopic().iterator().next();
        assertThat(Hibernate.isInitialized(loadedTopicFromService.getItems())).isTrue();
        assertThat(loadedTopicFromService.getItems()).containsExactly("Item 1", "Item 2");

        Service serviceById = serviceRepository.findById(serviceId).orElseThrow();
        assertThat(Hibernate.isInitialized(serviceById.getWorkPerformeds())).isTrue();
        assertThat(serviceById.getWorkPerformeds()).hasSize(1);

        // 3. ServiceTopicRepository outside any transaction / Hibernate session
        List<ServiceTopic> topics = serviceTopicRepository.findAllByOrderByCreatedAtAsc();
        assertThat(topics).hasSize(1);
        assertThat(Hibernate.isInitialized(topics.get(0).getItems())).isTrue();
        assertThat(topics.get(0).getItems()).containsExactly("Item 1", "Item 2");

        ServiceTopic topicById = serviceTopicRepository.findById(topicId).orElseThrow();
        assertThat(Hibernate.isInitialized(topicById.getItems())).isTrue();
        assertThat(topicById.getItems()).containsExactly("Item 1", "Item 2");
    }
}
