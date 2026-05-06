package com.olucaseduardo.zoomatech_api.util.validator;

import com.olucaseduardo.zoomatech_api.dto.service.CreateServiceTopicRequestDTO;
import com.olucaseduardo.zoomatech_api.util.interfaces.AtLeastOneField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ServiceTopicValidator implements ConstraintValidator<AtLeastOneField, CreateServiceTopicRequestDTO> {

    @Override
    public boolean isValid(CreateServiceTopicRequestDTO dto, ConstraintValidatorContext context) {
        boolean hasDescription = dto.description() != null && !dto.description().isBlank();
        boolean hasItems = dto.items() != null && !dto.items().isEmpty();

        return hasDescription || hasItems;
    }
}