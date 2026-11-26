package com.olucaseduardo.zoomatech_api;

import com.olucaseduardo.zoomatech_api.dto.service.CreateServiceTopicRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.service.UpdateServiceRequestDTO;
import com.olucaseduardo.zoomatech_api.dto.work_performed.WorkPerformedResponseDTO;
import com.olucaseduardo.zoomatech_api.entity.Service;
import com.olucaseduardo.zoomatech_api.entity.ServiceTopic;
import com.olucaseduardo.zoomatech_api.entity.WorkPerformed;
import com.olucaseduardo.zoomatech_api.repository.ServiceRepository;
import com.olucaseduardo.zoomatech_api.repository.ServiceTopicRepository;
import com.olucaseduardo.zoomatech_api.repository.WorkPerformedRepository;
import com.olucaseduardo.zoomatech_api.services.ServiceService;
import com.olucaseduardo.zoomatech_api.services.ServiceTopicService;
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
@Import({
        LazyLoadingOpenInViewFalseTest.TestCacheConfig.class,
        ServiceService.class,
        ServiceTopicService.class
})
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
    private ServiceService serviceService;

    @Autowired
    private ServiceTopicService serviceTopicService;

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

            workPerformedRepository.save(WorkPerformed.builder()
                    .title("Projeto Fazenda Y")
                    .description("Segundo projeto")
                    .performedAt("2026-02")
                    .photo("photo2.jpg")
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
        assertThat(works).hasSize(2);
        assertThat(Hibernate.isInitialized(works.get(0).getServices())).isTrue();
        WorkPerformedResponseDTO dto = new WorkPerformedResponseDTO(works.get(0));
        assertThat(dto.services()).hasSize(1);
        assertThat(dto.services().get(0).get("name")).isEqualTo("Consultoria");

        WorkPerformed workById = workPerformedRepository.findById(workId).orElseThrow();
        assertThat(Hibernate.isInitialized(workById.getServices())).isTrue();
        assertThat(workById.getServices()).hasSize(1);

        // 2. ServiceService outside any transaction / Hibernate session (with multiple workPerformeds!)
        List<Service> services = serviceService.findAll();
        assertThat(services).hasSize(1);
        assertThat(Hibernate.isInitialized(services.get(0).getWorkPerformeds())).isTrue();
        assertThat(services.get(0).getWorkPerformeds()).hasSize(2);
        assertThat(Hibernate.isInitialized(services.get(0).getServiceTopic())).isTrue();
        ServiceTopic loadedTopicFromService = services.get(0).getServiceTopic().iterator().next();
        assertThat(Hibernate.isInitialized(loadedTopicFromService.getItems())).isTrue();
        assertThat(loadedTopicFromService.getItems()).containsExactly("Item 1", "Item 2");

        Service serviceById = serviceService.findById(serviceId);
        assertThat(serviceById).isNotNull();
        assertThat(Hibernate.isInitialized(serviceById.getWorkPerformeds())).isTrue();
        assertThat(serviceById.getWorkPerformeds()).hasSize(2);
        ServiceTopic loadedTopicById = serviceById.getServiceTopic().iterator().next();
        assertThat(Hibernate.isInitialized(loadedTopicById.getItems())).isTrue();
        assertThat(loadedTopicById.getItems()).containsExactly("Item 1", "Item 2");

        // 3. ServiceTopicService update & fetch outside any transaction / Hibernate session
        ServiceTopic updatedTopic = serviceTopicService.update(
                topicId,
                new CreateServiceTopicRequestDTO("Manejo Atualizado", "Nova desc", List.of("Item 1", "Item 2", "Item 3"))
        );
        assertThat(Hibernate.isInitialized(updatedTopic.getItems())).isTrue();
        assertThat(updatedTopic.getItems()).containsExactly("Item 1", "Item 2", "Item 3");

        ServiceTopic topicAfterUpdate = serviceTopicService.findById(topicId);
        assertThat(Hibernate.isInitialized(topicAfterUpdate.getItems())).isTrue();
        assertThat(topicAfterUpdate.getItems()).containsExactly("Item 1", "Item 2", "Item 3");

        Service serviceAfterTopicUpdate = serviceService.findById(serviceId);
        ServiceTopic topicFromServiceAfterUpdate = serviceAfterTopicUpdate.getServiceTopic().iterator().next();
        assertThat(topicFromServiceAfterUpdate.getItems()).containsExactly("Item 1", "Item 2", "Item 3");

        // 4. Repeated topic update and ServiceService update should never duplicate items
        ServiceTopic updatedTopicAgain = serviceTopicService.update(
                topicId,
                new CreateServiceTopicRequestDTO("Manejo Atualizado 2", "Desc 2", List.of("Item 1", "Item 2", "Item 3"))
        );
        assertThat(updatedTopicAgain.getItems()).containsExactly("Item 1", "Item 2", "Item 3");

        Service updatedService = serviceService.update(
                serviceId,
                new UpdateServiceRequestDTO("icon2.svg", "Desc atualizada", "Consultoria 2")
        );
        ServiceTopic topicAfterServiceUpdate = updatedService.getServiceTopic().iterator().next();
        assertThat(Hibernate.isInitialized(topicAfterServiceUpdate.getItems())).isTrue();
        assertThat(topicAfterServiceUpdate.getItems()).containsExactly("Item 1", "Item 2", "Item 3");
    }
}
