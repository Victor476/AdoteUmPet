package com.adoteumpet.adoteumpetapi.controller;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para o endpoint GET /pets/{id}
 * Utiliza TestRestTemplate para testar o endpoint completo com contexto Spring
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PetControllerGetByIdTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PetRepository petRepository;

    private Pet testPet;

    @BeforeEach
    void setUp() {
        // Limpa o repositório antes de cada teste
        petRepository.deleteAll();
        
        // Cria um pet de teste
        testPet = new Pet();
        testPet.setName("Buddy");
        testPet.setSpecies(Species.DOG);
        testPet.setBreed("Golden Retriever");
        testPet.setAgeYears(3);
        testPet.setShelterCity("São Paulo");
        testPet.setShelterLat(new BigDecimal("-23.5505"));
        testPet.setShelterLng(new BigDecimal("-46.6333"));
        testPet.setStatus(Status.AVAILABLE);
        
        testPet = petRepository.save(testPet);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e dados do pet quando ID existir")
    void deveRetornarPetQuandoIdExistir() {
        // Given
        UUID petId = testPet.getId();

        // When
        ResponseEntity<Pet> response = restTemplate.getForEntity("/api/pets/" + petId, Pet.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(petId);
        assertThat(response.getBody().getName()).isEqualTo("Buddy");
        assertThat(response.getBody().getSpecies()).isEqualTo(Species.DOG);
        assertThat(response.getBody().getBreed()).isEqualTo("Golden Retriever");
        assertThat(response.getBody().getAgeYears()).isEqualTo(3);
        assertThat(response.getBody().getShelterCity()).isEqualTo("São Paulo");
        assertThat(response.getBody().getShelterLat()).isEqualByComparingTo(new BigDecimal("-23.5505"));
        assertThat(response.getBody().getShelterLng()).isEqualByComparingTo(new BigDecimal("-46.6333"));
        assertThat(response.getBody().getStatus()).isEqualTo(Status.AVAILABLE);
        assertThat(response.getBody().getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID não existir")
    void deveRetornar404QuandoIdNaoExistir() {
        // Given
        UUID petIdInexistente = UUID.randomUUID();

        // When
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/pets/" + petIdInexistente, Map.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo(404);
        assertThat(response.getBody().get("error")).isEqualTo("Recurso não encontrado");
        assertThat(response.getBody().get("message")).isEqualTo("Pet com ID '" + petIdInexistente + "' não encontrado.");
        assertThat(response.getBody().get("path")).isEqualTo("/api/pets/" + petIdInexistente);
        assertThat(response.getBody().get("timestamp")).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar pet com campos opcionais nulos")
    void deveRetornarPetComCamposOpcionaisNulos() {
        // Given - Cria pet com campos mínimos
        Pet petMinimo = new Pet();
        petMinimo.setName("Rex");
        petMinimo.setSpecies(Species.DOG);
        petMinimo.setAgeYears(2);
        petMinimo.setShelterCity("Rio de Janeiro");
        petMinimo.setStatus(Status.AVAILABLE);
        // Deixa outros campos nulos intencionalmente (breed, shelterLat, shelterLng)
        petMinimo = petRepository.save(petMinimo);

        // When
        ResponseEntity<Pet> response = restTemplate.getForEntity("/api/pets/" + petMinimo.getId(), Pet.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Pet pet = response.getBody();
        assertThat(pet).isNotNull();
        assertThat(pet.getName()).isEqualTo("Rex");
        assertThat(pet.getSpecies()).isEqualTo(Species.DOG);
        assertThat(pet.getAgeYears()).isEqualTo(2);
        assertThat(pet.getShelterCity()).isEqualTo("Rio de Janeiro");
        assertThat(pet.getStatus()).isEqualTo(Status.AVAILABLE);
        assertThat(pet.getBreed()).isNull();
        assertThat(pet.getShelterLat()).isNull();
        assertThat(pet.getShelterLng()).isNull();
    }

    @Test
    @DisplayName("Deve retornar diferentes espécies de pets")
    void deveRetornarDiferentesEspecies() {
        // Given - Cria um gato
        Pet gato = new Pet();
        gato.setName("Miau");
        gato.setSpecies(Species.CAT);
        gato.setBreed("Siamês");
        gato.setAgeYears(1);
        gato.setShelterCity("Brasília");
        gato.setStatus(Status.AVAILABLE);
        gato = petRepository.save(gato);

        // When
        ResponseEntity<Pet> response = restTemplate.getForEntity("/api/pets/" + gato.getId(), Pet.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Pet pet = response.getBody();
        assertThat(pet).isNotNull();
        assertThat(pet.getName()).isEqualTo("Miau");
        assertThat(pet.getSpecies()).isEqualTo(Species.CAT);
        assertThat(pet.getBreed()).isEqualTo("Siamês");
        assertThat(pet.getAgeYears()).isEqualTo(1);
        assertThat(pet.getShelterCity()).isEqualTo("Brasília");
        assertThat(pet.getStatus()).isEqualTo(Status.AVAILABLE);
    }

    @Test
    @DisplayName("Deve retornar erro 500 com formato de UUID inválido")
    void deveRetornarErro500ComUuidInvalido() {
        // Given
        String uuidInvalido = "invalid-uuid-format";

        // When
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/pets/" + uuidInvalido, Map.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}