package com.adoteumpet.adoteumpetapi.controller;

import com.adoteumpet.adoteumpetapi.dto.PetCreateDTO;
import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@MockBean(org.springframework.data.jpa.mapping.JpaMetamodelMappingContext.class)
@DisplayName("PetController - POST /api/pets")
class PetControllerPostTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    private PetCreateDTO validPetRequest;
    private Pet createdPet;

    @BeforeEach
    void setUp() {
        validPetRequest = new PetCreateDTO(
                "Buddy",
                Species.DOG,
                "Golden Retriever",
                3,
                "São Paulo",
                java.math.BigDecimal.valueOf(-23.5505),
                java.math.BigDecimal.valueOf(-46.6333),
                null
        );

        createdPet = new Pet();
        createdPet.setId(UUID.randomUUID());
        createdPet.setName("Buddy");
        createdPet.setSpecies(Species.DOG);
        createdPet.setBreed("Golden Retriever");
        createdPet.setAgeYears(3);
        createdPet.setShelterCity("São Paulo");
        createdPet.setShelterLat(java.math.BigDecimal.valueOf(-23.5505));
        createdPet.setShelterLng(java.math.BigDecimal.valueOf(-46.6333));
        createdPet.setStatus(Status.AVAILABLE);
    }

    @Test
    @DisplayName("Deve criar um pet com dados válidos")
    void createPet_WithValidData_ShouldReturn201AndCreatedPet() throws Exception {
        // Arrange
        when(petService.savePet(any(Pet.class))).thenReturn(createdPet);

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPetRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdPet.getId().toString()))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.species").value("DOG"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.ageYears").value(3))
                .andExpect(jsonPath("$.shelterCity").value("São Paulo"))
                .andExpect(jsonPath("$.shelterLat").value(-23.5505))
                .andExpect(jsonPath("$.shelterLng").value(-46.6333))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando nome for nulo")
    void createPet_WithNullName_ShouldReturn400() throws Exception {
        // Arrange
        PetCreateDTO invalidRequest = new PetCreateDTO(
                null,
                Species.DOG,
                "Labrador",
                2,
                "Rio de Janeiro",
                java.math.BigDecimal.valueOf(-22.9068),
                java.math.BigDecimal.valueOf(-43.1729),
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 quando nome for vazio")
    void createPet_WithEmptyName_ShouldReturn400() throws Exception {
        // Arrange
        PetCreateDTO invalidRequest = new PetCreateDTO(
                "",
                Species.CAT,
                "Siamês",
                1,
                "Belo Horizonte",
                java.math.BigDecimal.valueOf(-19.9191),
                java.math.BigDecimal.valueOf(-43.9386),
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 quando espécie for nula")
    void createPet_WithNullSpecies_ShouldReturn400() throws Exception {
        // Arrange
        PetCreateDTO invalidRequest = new PetCreateDTO(
                "Luna",
                null,
                "Vira-lata",
                4,
                "Salvador",
                java.math.BigDecimal.valueOf(-12.9714),
                java.math.BigDecimal.valueOf(-38.5014),
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 quando idade for negativa")
    void createPet_WithNegativeAge_ShouldReturn400() throws Exception {
        // Arrange
        PetCreateDTO invalidRequest = new PetCreateDTO(
                "Max",
                Species.DOG,
                "Poodle",
                -1,
                "Curitiba",
                java.math.BigDecimal.valueOf(-25.4284),
                java.math.BigDecimal.valueOf(-49.2733),
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 quando cidade do abrigo for nula")
    void createPet_WithNullShelterCity_ShouldReturn400() throws Exception {
        // Arrange
        PetCreateDTO invalidRequest = new PetCreateDTO(
                "Nala",
                Species.CAT,
                "Persa",
                2,
                null,
                java.math.BigDecimal.valueOf(-15.8267),
                java.math.BigDecimal.valueOf(-47.9218),
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 quando latitude for inválida")
    void createPet_WithInvalidLatitude_ShouldReturn400() throws Exception {
        // Arrange
        PetCreateDTO invalidRequest = new PetCreateDTO(
                "Thor",
                Species.DOG,
                "Rottweiler",
                5,
                "Recife",
                java.math.BigDecimal.valueOf(-91.0), // Latitude inválida (fora do range -90 a 90)
                java.math.BigDecimal.valueOf(-34.8770),
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 quando longitude for inválida")
    void createPet_WithInvalidLongitude_ShouldReturn400() throws Exception {
        // Arrange
        PetCreateDTO invalidRequest = new PetCreateDTO(
                "Bella",
                Species.CAT,
                "Angorá",
                3,
                "Fortaleza",
                java.math.BigDecimal.valueOf(-3.7172),
                java.math.BigDecimal.valueOf(-181.0), // Longitude inválida (fora do range -180 a 180)
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @DisplayName("Deve aceitar raça nula ou vazia")
    void createPet_WithNullOrEmptyBreed_ShouldReturn201() throws Exception {
        // Arrange
        PetCreateDTO requestWithNullBreed = new PetCreateDTO(
                "Charlie",
                Species.DOG,
                null, // Raça pode ser nula
                2,
                "Manaus",
                java.math.BigDecimal.valueOf(-3.1190),
                java.math.BigDecimal.valueOf(-60.0217),
                null
        );

        Pet petWithNullBreed = new Pet();
        petWithNullBreed.setId(UUID.randomUUID());
        petWithNullBreed.setName("Charlie");
        petWithNullBreed.setSpecies(Species.DOG);
        petWithNullBreed.setBreed(null);
        petWithNullBreed.setAgeYears(2);
        petWithNullBreed.setShelterCity("Manaus");
        petWithNullBreed.setShelterLat(java.math.BigDecimal.valueOf(-3.1190));
        petWithNullBreed.setShelterLng(java.math.BigDecimal.valueOf(-60.0217));
        petWithNullBreed.setStatus(Status.AVAILABLE);

        when(petService.savePet(any(Pet.class))).thenReturn(petWithNullBreed);

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithNullBreed)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Charlie"))
                .andExpect(jsonPath("$.breed").isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 400 quando JSON for malformado")
    void createPet_WithMalformedJson_ShouldReturn400() throws Exception {
        // Arrange
        String malformedJson = "{ \"name\": \"Pet\", \"species\": ";

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }
}
