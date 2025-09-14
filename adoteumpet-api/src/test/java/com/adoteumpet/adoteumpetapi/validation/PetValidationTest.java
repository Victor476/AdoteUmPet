package com.adoteumpet.adoteumpetapi.validation;

import com.adoteumpet.adoteumpetapi.dto.PetCreateDTO;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para validações do PetCreateDTO.
 * Verifica se todas as anotações de validação estão funcionando corretamente.
 */
@SpringBootTest
class PetValidationTest {

    @Autowired
    private Validator validator;

    private PetCreateDTO validPetDTO;

    @BeforeEach
    void setUp() {
        validPetDTO = new PetCreateDTO(
            "Rex",
            Species.DOG,
            "Golden Retriever",
            3,
            "São Paulo",
            new BigDecimal("-23.5505"),
            new BigDecimal("-46.6333"),
            Status.AVAILABLE
        );
    }

    @Test
    @DisplayName("Should validate successfully with all valid fields")
    void shouldValidateSuccessfullyWithValidFields() {
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        assertTrue(violations.isEmpty(), "Valid pet should not have validation errors");
    }

    @Test
    @DisplayName("Should fail validation when name is null")
    void shouldFailWhenNameIsNull() {
        validPetDTO.setName(null);
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("O nome não pode ser vazio.", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when name is empty")
    void shouldFailWhenNameIsEmpty() {
        validPetDTO.setName("");
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("O nome não pode ser vazio.", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void shouldFailWhenNameIsBlank() {
        validPetDTO.setName("   ");
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("O nome não pode ser vazio.", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when species is null")
    void shouldFailWhenSpeciesIsNull() {
        validPetDTO.setSpecies(null);
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("species", violation.getPropertyPath().toString());
        assertEquals("Espécie inválida. Valores válidos são 'dog' ou 'cat'.", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when age is negative")
    void shouldFailWhenAgeIsNegative() {
        validPetDTO.setAgeYears(-1);
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("ageYears", violation.getPropertyPath().toString());
        assertEquals("A idade deve ser um número positivo.", violation.getMessage());
    }

    @Test
    @DisplayName("Should pass validation when age is zero")
    void shouldPassWhenAgeIsZero() {
        validPetDTO.setAgeYears(0);
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertTrue(violations.isEmpty(), "Age zero should be valid");
    }

    @Test
    @DisplayName("Should fail validation when age is too high")
    void shouldFailWhenAgeIsTooHigh() {
        validPetDTO.setAgeYears(31);
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("ageYears", violation.getPropertyPath().toString());
        assertEquals("Idade deve ser menor que 30 anos", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when shelter city is null")
    void shouldFailWhenShelterCityIsNull() {
        validPetDTO.setShelterCity(null);
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("shelterCity", violation.getPropertyPath().toString());
        assertEquals("Cidade do abrigo é obrigatória", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when shelter city is empty")
    void shouldFailWhenShelterCityIsEmpty() {
        validPetDTO.setShelterCity("");
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("shelterCity", violation.getPropertyPath().toString());
        assertEquals("Cidade do abrigo é obrigatória", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when latitude is below minimum")
    void shouldFailWhenLatitudeIsBelowMinimum() {
        validPetDTO.setShelterLat(new BigDecimal("-91.0"));
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("shelterLat", violation.getPropertyPath().toString());
        assertEquals("Latitude deve estar entre -90 e 90 graus", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when latitude is above maximum")
    void shouldFailWhenLatitudeIsAboveMaximum() {
        validPetDTO.setShelterLat(new BigDecimal("91.0"));
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("shelterLat", violation.getPropertyPath().toString());
        assertEquals("Latitude deve estar entre -90 e 90 graus", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when longitude is below minimum")
    void shouldFailWhenLongitudeIsBelowMinimum() {
        validPetDTO.setShelterLng(new BigDecimal("-181.0"));
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("shelterLng", violation.getPropertyPath().toString());
        assertEquals("Longitude deve estar entre -180 e 180 graus", violation.getMessage());
    }

    @Test
    @DisplayName("Should fail validation when longitude is above maximum")
    void shouldFailWhenLongitudeIsAboveMaximum() {
        validPetDTO.setShelterLng(new BigDecimal("181.0"));
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetCreateDTO> violation = violations.iterator().next();
        assertEquals("shelterLng", violation.getPropertyPath().toString());
        assertEquals("Longitude deve estar entre -180 e 180 graus", violation.getMessage());
    }

    @Test
    @DisplayName("Should pass validation with boundary latitude values")
    void shouldPassWithBoundaryLatitudeValues() {
        // Test minimum boundary
        validPetDTO.setShelterLat(new BigDecimal("-90.0"));
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        assertTrue(violations.isEmpty(), "Latitude -90 should be valid");

        // Test maximum boundary
        validPetDTO.setShelterLat(new BigDecimal("90.0"));
        violations = validator.validate(validPetDTO);
        assertTrue(violations.isEmpty(), "Latitude 90 should be valid");
    }

    @Test
    @DisplayName("Should pass validation with boundary longitude values")
    void shouldPassWithBoundaryLongitudeValues() {
        // Test minimum boundary
        validPetDTO.setShelterLng(new BigDecimal("-180.0"));
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(validPetDTO);
        assertTrue(violations.isEmpty(), "Longitude -180 should be valid");

        // Test maximum boundary
        validPetDTO.setShelterLng(new BigDecimal("180.0"));
        violations = validator.validate(validPetDTO);
        assertTrue(violations.isEmpty(), "Longitude 180 should be valid");
    }

    @Test
    @DisplayName("Should have multiple validation errors for multiple invalid fields")
    void shouldHaveMultipleValidationErrorsForMultipleInvalidFields() {
        PetCreateDTO invalidPet = new PetCreateDTO(
            "", // Invalid name
            null, // Invalid species
            "Valid Breed",
            -2, // Invalid age
            "", // Invalid shelter city
            new BigDecimal("95.0"), // Invalid latitude
            new BigDecimal("185.0"), // Invalid longitude
            Status.AVAILABLE
        );
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(invalidPet);
        
        assertEquals(6, violations.size(), "Should have 6 validation errors");
        
        // Verify all expected error messages are present
        Set<String> errorMessages = violations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(java.util.stream.Collectors.toSet());
        
        assertTrue(errorMessages.contains("O nome não pode ser vazio."));
        assertTrue(errorMessages.contains("Espécie inválida. Valores válidos são 'dog' ou 'cat'."));
        assertTrue(errorMessages.contains("A idade deve ser um número positivo."));
        assertTrue(errorMessages.contains("Cidade do abrigo é obrigatória"));
        assertTrue(errorMessages.contains("Latitude deve estar entre -90 e 90 graus"));
        assertTrue(errorMessages.contains("Longitude deve estar entre -180 e 180 graus"));
    }

    @Test
    @DisplayName("Should pass validation when optional fields are null")
    void shouldPassWhenOptionalFieldsAreNull() {
        PetCreateDTO petWithNullOptionals = new PetCreateDTO(
            "Rex",
            Species.DOG,
            null, // breed is optional
            null, // age is optional
            "São Paulo",
            null, // coordinates are optional
            null,
            null // status is optional
        );
        
        Set<ConstraintViolation<PetCreateDTO>> violations = validator.validate(petWithNullOptionals);
        
        assertTrue(violations.isEmpty(), "Pet with null optional fields should be valid");
    }
}