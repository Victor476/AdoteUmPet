package com.adoteumpet.adoteumpetapi.validation;

import com.adoteumpet.adoteumpetapi.dto.PetCreateDTO;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para validação dos endpoints de pets.
 * Verifica se as validações funcionam corretamente na API completa.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class PetValidationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/pets";
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @DisplayName("Should create pet successfully with valid data")
    void shouldCreatePetSuccessfullyWithValidData() {
        PetCreateDTO validPet = new PetCreateDTO(
            "Rex",
            Species.DOG,
            "Golden Retriever",
            3,
            "São Paulo",
            new BigDecimal("-23.5505"),
            new BigDecimal("-46.6333"),
            Status.AVAILABLE
        );

        HttpEntity<PetCreateDTO> request = new HttpEntity<>(validPet, createHeaders());
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return 400 when name is empty")
    void shouldReturn400WhenNameIsEmpty() throws Exception {
        PetCreateDTO invalidPet = new PetCreateDTO(
            "", // Invalid name
            Species.DOG,
            "Golden Retriever",
            3,
            "São Paulo",
            new BigDecimal("-23.5505"),
            new BigDecimal("-46.6333"),
            Status.AVAILABLE
        );

        HttpEntity<PetCreateDTO> request = new HttpEntity<>(invalidPet, createHeaders());
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
        
        assertEquals(400, responseBody.get("status"));
        assertEquals("Bad Request", responseBody.get("error"));
        assertEquals("Validation failed for 'Pet'.", responseBody.get("message"));
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> details = (List<Map<String, String>>) responseBody.get("details");
        assertEquals(1, details.size());
        
        Map<String, String> error = details.get(0);
        assertEquals("name", error.get("field"));
        assertEquals("O nome não pode ser vazio.", error.get("message"));
    }

    @Test
    @DisplayName("Should return 400 when species is invalid")
    void shouldReturn400WhenSpeciesIsInvalid() throws Exception {
        String invalidJson = """
            {
                "name": "Tweety",
                "species": "bird",
                "breed": "Canário",
                "age_years": 2,
                "shelter_city": "São Paulo",
                "shelter_lat": -23.5505,
                "shelter_lng": -46.6333,
                "status": "AVAILABLE"
            }
            """;

        HttpHeaders headers = createHeaders();
        HttpEntity<String> request = new HttpEntity<>(invalidJson, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
        
        assertEquals(400, responseBody.get("status"));
        assertEquals("Dados inválidos", responseBody.get("error"));
        assertTrue(responseBody.get("message").toString().contains("species"));
    }

    @Test
    @DisplayName("Should return 400 when age is negative")
    void shouldReturn400WhenAgeIsNegative() throws Exception {
        PetCreateDTO invalidPet = new PetCreateDTO(
            "Rex",
            Species.DOG,
            "Golden Retriever",
            -2, // Invalid age
            "São Paulo",
            new BigDecimal("-23.5505"),
            new BigDecimal("-46.6333"),
            Status.AVAILABLE
        );

        HttpEntity<PetCreateDTO> request = new HttpEntity<>(invalidPet, createHeaders());
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> details = (List<Map<String, String>>) responseBody.get("details");
        assertEquals(1, details.size());
        
        Map<String, String> error = details.get(0);
        assertEquals("ageYears", error.get("field"));
        assertEquals("A idade deve ser um número positivo.", error.get("message"));
    }

    @Test
    @DisplayName("Should return 400 when shelter city is empty")
    void shouldReturn400WhenShelterCityIsEmpty() throws Exception {
        PetCreateDTO invalidPet = new PetCreateDTO(
            "Rex",
            Species.DOG,
            "Golden Retriever",
            3,
            "", // Invalid shelter city
            new BigDecimal("-23.5505"),
            new BigDecimal("-46.6333"),
            Status.AVAILABLE
        );

        HttpEntity<PetCreateDTO> request = new HttpEntity<>(invalidPet, createHeaders());
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> details = (List<Map<String, String>>) responseBody.get("details");
        assertEquals(1, details.size());
        
        Map<String, String> error = details.get(0);
        assertEquals("shelterCity", error.get("field"));
        assertEquals("Cidade do abrigo é obrigatória", error.get("message"));
    }

    @Test
    @DisplayName("Should return 400 when coordinates are out of range")
    void shouldReturn400WhenCoordinatesAreOutOfRange() throws Exception {
        PetCreateDTO invalidPet = new PetCreateDTO(
            "Rex",
            Species.DOG,
            "Golden Retriever",
            3,
            "São Paulo",
            new BigDecimal("95.0"), // Invalid latitude
            new BigDecimal("185.0"), // Invalid longitude
            Status.AVAILABLE
        );

        HttpEntity<PetCreateDTO> request = new HttpEntity<>(invalidPet, createHeaders());
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> details = (List<Map<String, String>>) responseBody.get("details");
        assertEquals(2, details.size());
        
        // Verify both latitude and longitude errors are present
        boolean latitudeError = details.stream().anyMatch(error -> 
            "shelterLat".equals(error.get("field")) && 
            "Latitude deve estar entre -90 e 90 graus".equals(error.get("message"))
        );
        
        boolean longitudeError = details.stream().anyMatch(error -> 
            "shelterLng".equals(error.get("field")) && 
            "Longitude deve estar entre -180 e 180 graus".equals(error.get("message"))
        );
        
        assertTrue(latitudeError, "Should have latitude validation error");
        assertTrue(longitudeError, "Should have longitude validation error");
    }

    @Test
    @DisplayName("Should return 400 with multiple validation errors")
    void shouldReturn400WithMultipleValidationErrors() throws Exception {
        PetCreateDTO invalidPet = new PetCreateDTO(
            "", // Invalid name
            null, // Invalid species - will be handled as JSON parsing error
            "Valid Breed",
            -2, // Invalid age
            "", // Invalid shelter city
            new BigDecimal("95.0"), // Invalid latitude
            new BigDecimal("185.0"), // Invalid longitude
            Status.AVAILABLE
        );

        HttpEntity<PetCreateDTO> request = new HttpEntity<>(invalidPet, createHeaders());
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
        
        assertEquals(400, responseBody.get("status"));
        assertEquals("Bad Request", responseBody.get("error"));
        assertEquals("Validation failed for 'Pet'.", responseBody.get("message"));
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> details = (List<Map<String, String>>) responseBody.get("details");
        assertTrue(details.size() >= 5, "Should have at least 5 validation errors");
        
        // Verify some expected error messages are present
        java.util.Set<String> errorMessages = details.stream()
            .map(error -> error.get("message"))
            .collect(java.util.stream.Collectors.toSet());
        
        assertTrue(errorMessages.contains("O nome não pode ser vazio."));
        assertTrue(errorMessages.contains("A idade deve ser um número positivo."));
        assertTrue(errorMessages.contains("Cidade do abrigo é obrigatória"));
        assertTrue(errorMessages.contains("Latitude deve estar entre -90 e 90 graus"));
        assertTrue(errorMessages.contains("Longitude deve estar entre -180 e 180 graus"));
    }

    @Test
    @DisplayName("Should accept boundary coordinate values")
    void shouldAcceptBoundaryCoordinateValues() {
        // Test with boundary values
        PetCreateDTO petWithBoundaryCoords = new PetCreateDTO(
            "Rex",
            Species.DOG,
            "Golden Retriever",
            3,
            "São Paulo",
            new BigDecimal("-90.0"), // Minimum latitude
            new BigDecimal("-180.0"), // Minimum longitude
            Status.AVAILABLE
        );

        HttpEntity<PetCreateDTO> request = new HttpEntity<>(petWithBoundaryCoords, createHeaders());
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        // Test with maximum boundary values
        petWithBoundaryCoords.setShelterLat(new BigDecimal("90.0"));
        petWithBoundaryCoords.setShelterLng(new BigDecimal("180.0"));
        
        request = new HttpEntity<>(petWithBoundaryCoords, createHeaders());
        response = restTemplate.postForEntity(getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should accept pet with minimal required fields")
    void shouldAcceptPetWithMinimalRequiredFields() {
        PetCreateDTO minimalPet = new PetCreateDTO(
            "Rex",
            Species.DOG,
            null, // breed is optional
            null, // age is optional
            "São Paulo",
            null, // coordinates are optional
            null,
            null // status is optional (will default to AVAILABLE)
        );

        HttpEntity<PetCreateDTO> request = new HttpEntity<>(minimalPet, createHeaders());
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl(), request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}