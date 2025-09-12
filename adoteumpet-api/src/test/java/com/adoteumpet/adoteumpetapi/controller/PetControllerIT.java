package com.adoteumpet.adoteumpetapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.repository.PetRepository;

import java.util.List;

/**
 * Testes de integração para o PetController.
 * Testa o endpoint POST /api/pets com o contexto completo da aplicação.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PetControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
    }

    @Test
    void createPet_WithValidData_ShouldReturn201AndSavePet() throws Exception {
        // Arrange
        String petJson = """
            {
                "name": "Buddy",
                "species": "DOG",
                "breed": "Golden Retriever",
                "ageYears": 3,
                "shelterCity": "São Paulo",
                "shelterLat": -23.5505,
                "shelterLng": -46.6333,
                "status": "AVAILABLE"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(petJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.species").value("DOG"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.ageYears").value(3))
                .andExpect(jsonPath("$.shelterCity").value("São Paulo"))
                .andExpect(jsonPath("$.shelterLat").value(-23.5505))
                .andExpect(jsonPath("$.shelterLng").value(-46.6333))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists());

        // Verifica se foi salvo no banco
        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(1);
        Pet savedPet = pets.get(0);
        assertThat(savedPet.getName()).isEqualTo("Buddy");
        assertThat(savedPet.getSpecies()).isEqualTo(Species.DOG);
        assertThat(savedPet.getStatus()).isEqualTo(Status.AVAILABLE);
    }

    @Test
    void createPet_WithBlankName_ShouldReturn400() throws Exception {
        // Arrange
        String petJson = """
            {
                "name": "",
                "species": "DOG",
                "ageYears": 1,
                "shelterCity": "Brasília",
                "shelterLat": -15.7801,
                "shelterLng": -47.9292
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(petJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors.name").value("Nome é obrigatório"));

        // Verifica que nada foi salvo no banco
        assertThat(petRepository.findAll()).isEmpty();
    }

    @Test
    void createPet_WithInvalidAge_ShouldReturn400() throws Exception {
        // Arrange
        String petJson = """
            {
                "name": "Max",
                "species": "DOG",
                "ageYears": -1,
                "shelterCity": "Fortaleza",
                "shelterLat": -3.7319,
                "shelterLng": -38.5267
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(petJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.ageYears").value("Idade deve ser maior ou igual a 0"));

        // Verifica que nada foi salvo no banco
        assertThat(petRepository.findAll()).isEmpty();
    }

    @Test
    void createPet_WithInvalidLatitude_ShouldReturn400() throws Exception {
        // Arrange
        String petJson = """
            {
                "name": "Bella",
                "species": "DOG",
                "ageYears": 5,
                "shelterCity": "Recife",
                "shelterLat": 95.0,
                "shelterLng": -34.8813
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(petJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.shelterLat").value("Latitude deve estar entre -90 e 90 graus"));

        // Verifica que nada foi salvo no banco
        assertThat(petRepository.findAll()).isEmpty();
    }

    @Test
    void createPet_WithMinimalData_ShouldReturn201AndSetDefaults() throws Exception {
        // Arrange
        String petJson = """
            {
                "name": "Luna",
                "species": "CAT",
                "ageYears": 2,
                "shelterCity": "Rio de Janeiro",
                "shelterLat": -22.9068,
                "shelterLng": -43.1729
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(petJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Luna"))
                .andExpect(jsonPath("$.species").value("CAT"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        // Verifica se foi salvo no banco com valores padrão
        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(1);
        Pet savedPet = pets.get(0);
        assertThat(savedPet.getBreed()).isNull();
        assertThat(savedPet.getStatus()).isEqualTo(Status.AVAILABLE);
    }
}