package com.adoteumpet.adoteumpetapi.controller;

import com.adoteumpet.adoteumpetapi.dto.BreedResponse;
import com.adoteumpet.adoteumpetapi.service.BreedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários simplificados para BreedController
 * Testa a lógica do controller sem Spring MVC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BreedController - Testes Unitários Simplificados")
class BreedControllerSimpleTest {

    @Mock
    private BreedService breedService;

    @InjectMocks
    private BreedController breedController;

    private List<BreedResponse> mockCatBreeds;
    private List<BreedResponse> mockDogBreeds;

    @BeforeEach
    void setUp() {
        // Setup mock cat breeds (COM energy_level)
        mockCatBreeds = Arrays.asList(
                new BreedResponse("Persian", "Iran", "Docile", 1, "https://cat1.jpg"),
                new BreedResponse("Bengal", "Egypt", "Active", 5, "https://cat2.jpg")
        );

        // Setup mock dog breeds (SEM energy_level)
        mockDogBreeds = Arrays.asList(
                new BreedResponse("Golden Retriever", "Scotland", "Friendly", null, "https://dog1.jpg"),
                new BreedResponse("Labrador", "Canada", "Loyal", null, "https://dog2.jpg")
        );
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve retornar gatos com energy_level")
    void shouldReturnCatsWithEnergyLevel() {
        // Given
        when(breedService.getBreedsBySpecies("cat", null)).thenReturn(mockCatBreeds);

        // When
        ResponseEntity<List<BreedResponse>> response = breedController.getBreedsBySpecies("cat", null);

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        
        // Verifica que gatos TÊM energy_level
        assertNotNull(response.getBody().get(0).getEnergyLevel());
        assertEquals(1, response.getBody().get(0).getEnergyLevel());
        assertNotNull(response.getBody().get(1).getEnergyLevel());
        assertEquals(5, response.getBody().get(1).getEnergyLevel());
        
        verify(breedService).getBreedsBySpecies("cat", null);
    }

    @Test
    @DisplayName("GET /breeds/dog - Deve retornar cães sem energy_level")
    void shouldReturnDogsWithoutEnergyLevel() {
        // Given
        when(breedService.getBreedsBySpecies("dog", null)).thenReturn(mockDogBreeds);

        // When
        ResponseEntity<List<BreedResponse>> response = breedController.getBreedsBySpecies("dog", null);

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        
        // Verifica que cães NÃO TÊM energy_level (null)
        assertNull(response.getBody().get(0).getEnergyLevel());
        assertNull(response.getBody().get(1).getEnergyLevel());
        
        verify(breedService).getBreedsBySpecies("dog", null);
    }

    @Test
    @DisplayName("GET /breeds/cat?name=Persian - Deve aplicar filtro por nome")
    void shouldApplyNameFilter() {
        // Given
        List<BreedResponse> filteredBreeds = Collections.singletonList(
                new BreedResponse("Persian", "Iran", "Docile", 1, "https://cat1.jpg")
        );
        when(breedService.getBreedsBySpecies("cat", "Persian")).thenReturn(filteredBreeds);

        // When
        ResponseEntity<List<BreedResponse>> response = breedController.getBreedsBySpecies("cat", "Persian");

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Persian", response.getBody().get(0).getName());
        
        verify(breedService).getBreedsBySpecies("cat", "Persian");
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve retornar lista vazia quando não há resultados")
    void shouldReturnEmptyListWhenNoResults() {
        // Given
        when(breedService.getBreedsBySpecies("cat", "Inexistente")).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<BreedResponse>> response = breedController.getBreedsBySpecies("cat", "Inexistente");

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        
        verify(breedService).getBreedsBySpecies("cat", "Inexistente");
    }

    @Test
    @DisplayName("GET /breeds/bird - Deve retornar 400 para espécie inválida")
    void shouldReturn400ForInvalidSpecies() {
        // When
        ResponseEntity<List<BreedResponse>> response = breedController.getBreedsBySpecies("bird", null);

        // Then
        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
        
        // Service não deve ser chamado para espécies inválidas
        verifyNoInteractions(breedService);
    }

    @Test
    @DisplayName("GET /breeds/health - Deve testar health check")
    void shouldReturnHealthStatus() {
        // Given
        List<BreedResponse> catBreeds = Collections.singletonList(
                new BreedResponse("Persian", "Iran", "Docile", 1, "https://cat1.jpg")
        );
        List<BreedResponse> dogBreeds = Collections.singletonList(
                new BreedResponse("Golden", "Scotland", "Friendly", null, "https://dog1.jpg")
        );
        
        when(breedService.getBreedsBySpecies("cat", null)).thenReturn(catBreeds);
        when(breedService.getBreedsBySpecies("dog", null)).thenReturn(dogBreeds);

        // When
        ResponseEntity<String> response = breedController.healthCheck();

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("APIs externas funcionais"));
        assertTrue(response.getBody().contains("Gatos: 1 raças"));
        assertTrue(response.getBody().contains("Cães: 1 raças"));
        
        verify(breedService).getBreedsBySpecies("cat", null);
        verify(breedService).getBreedsBySpecies("dog", null);
    }

    @Test
    @DisplayName("GET /breeds/health - Deve tratar erro no health check")
    void shouldHandleHealthCheckError() {
        // Given
        when(breedService.getBreedsBySpecies("cat", null))
                .thenThrow(new RuntimeException("API indisponível"));

        // When
        ResponseEntity<String> response = breedController.healthCheck();

        // Then
        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("❌ Erro"));
        
        verify(breedService).getBreedsBySpecies("cat", null);
    }

    @Test
    @DisplayName("Deve validar diferentes casos de filtro")
    void shouldValidateDifferentFilterCases() {
        // Given
        List<BreedResponse> allBreeds = mockCatBreeds;
        List<BreedResponse> filteredBreeds = Collections.singletonList(mockCatBreeds.get(0));
        
        when(breedService.getBreedsBySpecies("cat", null)).thenReturn(allBreeds);
        when(breedService.getBreedsBySpecies("cat", "")).thenReturn(allBreeds);
        when(breedService.getBreedsBySpecies("cat", "Persian")).thenReturn(filteredBreeds);

        // When & Then - Sem filtro
        ResponseEntity<List<BreedResponse>> response1 = breedController.getBreedsBySpecies("cat", null);
        assertEquals(2, response1.getBody().size());

        // When & Then - Filtro vazio
        ResponseEntity<List<BreedResponse>> response2 = breedController.getBreedsBySpecies("cat", "");
        assertEquals(2, response2.getBody().size());

        // When & Then - Com filtro
        ResponseEntity<List<BreedResponse>> response3 = breedController.getBreedsBySpecies("cat", "Persian");
        assertEquals(1, response3.getBody().size());
        assertEquals("Persian", response3.getBody().get(0).getName());

        verify(breedService).getBreedsBySpecies("cat", null);
        verify(breedService).getBreedsBySpecies("cat", "");
        verify(breedService).getBreedsBySpecies("cat", "Persian");
    }

    @Test
    @DisplayName("Deve preservar dados específicos de cada espécie")
    void shouldPreserveSpeciesSpecificData() {
        // Given
        when(breedService.getBreedsBySpecies("cat", null)).thenReturn(mockCatBreeds);
        when(breedService.getBreedsBySpecies("dog", null)).thenReturn(mockDogBreeds);

        // When
        ResponseEntity<List<BreedResponse>> catResponse = breedController.getBreedsBySpecies("cat", null);
        ResponseEntity<List<BreedResponse>> dogResponse = breedController.getBreedsBySpecies("dog", null);

        // Then - Verificar gatos
        List<BreedResponse> cats = catResponse.getBody();
        assertNotNull(cats);
        for (BreedResponse cat : cats) {
            assertNotNull(cat.getEnergyLevel(), "Gatos devem ter energy_level");
            assertTrue(cat.getEnergyLevel() >= 1 && cat.getEnergyLevel() <= 5, 
                      "Energy level deve estar entre 1-5");
        }

        // Then - Verificar cães
        List<BreedResponse> dogs = dogResponse.getBody();
        assertNotNull(dogs);
        for (BreedResponse dog : dogs) {
            assertNull(dog.getEnergyLevel(), "Cães não devem ter energy_level");
        }

        verify(breedService).getBreedsBySpecies("cat", null);
        verify(breedService).getBreedsBySpecies("dog", null);
    }
}