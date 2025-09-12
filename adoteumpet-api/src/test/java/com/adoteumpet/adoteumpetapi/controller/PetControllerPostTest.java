package com.adoteumpet.adoteumpetapi.controller;

import com.adoteumpet.adoteumpetapi.dto.PetCreateDTO;
import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para o PetController, focando no endpoint POST /pets.
 */
@WebMvcTest(PetController.class)
class PetControllerPostTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    private PetCreateDTO validPetCreateDTO;
    private Pet savedPet;

    @BeforeEach
    void setUp() {
        // Criando um DTO válido para testes
        validPetCreateDTO = new PetCreateDTO();
        validPetCreateDTO.setName("Rex");
        validPetCreateDTO.setSpecies(Species.DOG);
        validPetCreateDTO.setBreed("Golden Retriever");
        validPetCreateDTO.setAgeYears(3);
        validPetCreateDTO.setShelterCity("São Paulo");
        validPetCreateDTO.setShelterLat(new BigDecimal("-23.55052"));
        validPetCreateDTO.setShelterLng(new BigDecimal("-46.633308"));
        validPetCreateDTO.setStatus(Status.AVAILABLE);

        // Pet que seria retornado pelo service
        savedPet = new Pet();
        savedPet.setId(UUID.fromString("e98e4d25-5f5b-4b9e-9b6b-4e6f6f6f6f6f"));
        savedPet.setName("Rex");
        savedPet.setSpecies(Species.DOG);
        savedPet.setBreed("Golden Retriever");
        savedPet.setAgeYears(3);
        savedPet.setShelterCity("São Paulo");
        savedPet.setShelterLat(new BigDecimal("-23.55052"));
        savedPet.setShelterLng(new BigDecimal("-46.633308"));
        savedPet.setStatus(Status.AVAILABLE);
        savedPet.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createPet_WithValidData_ShouldReturn201Created() throws Exception {
        // Given
        when(petService.savePet(any(Pet.class))).thenReturn(savedPet);

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedPet.getId().toString()))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.species").value("DOG"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.ageYears").value(3))
                .andExpect(jsonPath("$.shelterCity").value("São Paulo"))
                .andExpect(jsonPath("$.shelterLat").value(-23.55052))
                .andExpect(jsonPath("$.shelterLng").value(-46.633308))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createPet_WithMissingName_ShouldReturn400BadRequest() throws Exception {
        // Given
        validPetCreateDTO.setName(null);

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.name").value("Nome é obrigatório"));
    }

    @Test
    void createPet_WithBlankName_ShouldReturn400BadRequest() throws Exception {
        // Given
        validPetCreateDTO.setName("   ");

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.name").value("Nome é obrigatório"));
    }

    @Test
    void createPet_WithMissingSpecies_ShouldReturn400BadRequest() throws Exception {
        // Given
        validPetCreateDTO.setSpecies(null);

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.species").value("Espécie é obrigatória"));
    }

    @Test
    void createPet_WithMissingShelterCity_ShouldReturn400BadRequest() throws Exception {
        // Given
        validPetCreateDTO.setShelterCity(null);

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.shelterCity").value("Cidade do abrigo é obrigatória"));
    }

    @Test
    void createPet_WithInvalidAge_ShouldReturn400BadRequest() throws Exception {
        // Given
        validPetCreateDTO.setAgeYears(-1);

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.ageYears").value("Idade deve ser maior ou igual a 0"));
    }

    @Test
    void createPet_WithInvalidLatitude_ShouldReturn400BadRequest() throws Exception {
        // Given
        validPetCreateDTO.setShelterLat(new BigDecimal("91.0"));

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.shelterLat").value("Latitude deve estar entre -90 e 90 graus"));
    }

    @Test
    void createPet_WithInvalidLongitude_ShouldReturn400BadRequest() throws Exception {
        // Given
        validPetCreateDTO.setShelterLng(new BigDecimal("181.0"));

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.errors.shelterLng").value("Longitude deve estar entre -180 e 180 graus"));
    }

    @Test
    void createPet_WithoutStatus_ShouldDefaultToAvailable() throws Exception {
        // Given
        validPetCreateDTO.setStatus(null);
        savedPet.setStatus(Status.AVAILABLE);
        when(petService.savePet(any(Pet.class))).thenReturn(savedPet);

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void createPet_WithOptionalFields_ShouldReturn201Created() throws Exception {
        // Given
        validPetCreateDTO.setBreed(null);
        validPetCreateDTO.setAgeYears(null);
        validPetCreateDTO.setShelterLat(null);
        validPetCreateDTO.setShelterLng(null);

        savedPet.setBreed(null);
        savedPet.setAgeYears(null);
        savedPet.setShelterLat(null);
        savedPet.setShelterLng(null);

        when(petService.savePet(any(Pet.class))).thenReturn(savedPet);

        // When & Then
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedPet.getId().toString()))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.breed").doesNotExist())
                .andExpect(jsonPath("$.ageYears").doesNotExist())
                .andExpect(jsonPath("$.shelterLat").doesNotExist())
                .andExpect(jsonPath("$.shelterLng").doesNotExist());
    }
}