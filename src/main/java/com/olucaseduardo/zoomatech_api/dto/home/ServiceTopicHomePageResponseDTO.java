package com.olucaseduardo.zoomatech_api.dto.home;

import com.olucaseduardo.zoomatech_api.entity.ServiceTopic;

import java.util.List;

public record ServiceTopicHomePageResponseDTO(
        String topic,
        String description,
        List<String> items
) {
    public ServiceTopicHomePageResponseDTO(ServiceTopic topic) {
        this(topic.getTopic(),topic.getDescription(),topic.getItems());
    }
}
