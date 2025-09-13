package com.adoteumpet.adoteumpetapi.service;

import com.adoteumpet.adoteumpetapi.dto.BreedResponse;
import com.adoteumpet.adoteumpetapi.dto.external.CatApiBreed;
import com.adoteumpet.adoteumpetapi.dto.external.DogApiBreed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para BreedService
 * Testa a lógica de integração com APIs externas e transformação de dados
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BreedService - Testes Unitários")
class BreedServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BreedService breedService;

    private CatApiBreed[] mockCatBreeds;
    private DogApiBreed[] mockDogBreeds;

    @BeforeEach
    void setUp() {
        // Mock data para gatos
        CatApiBreed persian = new CatApiBreed();
        persian.setName("Persian");
        persian.setOrigin("Iran");
        persian.setTemperament("Docile, Quiet, Affectionate");
        persian.setEnergyLevel(1);
        persian.setReferenceImageId("0XYvRd7oD");

        CatApiBreed bengal = new CatApiBreed();
        bengal.setName("Bengal");
        bengal.setOrigin("United States");
        bengal.setTemperament("Alert, Agile, Energetic");
        bengal.setEnergyLevel(5);
        bengal.setReferenceImageId("O3btzLlsO");

        mockCatBreeds = new CatApiBreed[]{persian, bengal};

        // Mock data para cães
        DogApiBreed goldenRetriever = new DogApiBreed();
        goldenRetriever.setName("Golden Retriever");
        goldenRetriever.setOrigin("Scotland, England");
        goldenRetriever.setTemperament("Intelligent, Kind, Reliable");
        goldenRetriever.setReferenceImageId("HJ7Pzg5EQ");

        DogApiBreed labrador = new DogApiBreed();
        labrador.setName("Labrador Retriever");
        labrador.setOrigin("Canada");
        labrador.setTemperament("Kind, Outgoing, Agile");
        labrador.setReferenceImageId("B1uW7l5VX");

        mockDogBreeds = new DogApiBreed[]{goldenRetriever, labrador};
    }

    @Test
    @DisplayName("Deve buscar gatos com energy_level presente")
    void shouldGetCatBreedsWithEnergyLevel() {
        // Given
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(mockCatBreeds);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        BreedResponse persian = result.get(0);
        assertEquals("Persian", persian.getName());
        assertEquals("Iran", persian.getOrigin());
        assertEquals("Docile, Quiet, Affectionate", persian.getTemperament());
        assertEquals(1, persian.getEnergyLevel()); // ✅ Gatos TÊM energy_level
        assertEquals("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg", persian.getImageUrl());

        BreedResponse bengal = result.get(1);
        assertEquals("Bengal", bengal.getName());
        assertEquals(5, bengal.getEnergyLevel()); // ✅ Gatos TÊM energy_level

        verify(restTemplate).getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class);
    }

    @Test
    @DisplayName("Deve buscar cães SEM energy_level")
    void shouldGetDogBreedsWithoutEnergyLevel() {
        // Given
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(mockDogBreeds);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("dog", null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        BreedResponse goldenRetriever = result.get(0);
        assertEquals("Golden Retriever", goldenRetriever.getName());
        assertEquals("Scotland, England", goldenRetriever.getOrigin());
        assertEquals("Intelligent, Kind, Reliable", goldenRetriever.getTemperament());
        assertNull(goldenRetriever.getEnergyLevel()); // ❌ Cães NÃO TÊM energy_level
        assertEquals("https://cdn2.thedogapi.com/images/HJ7Pzg5EQ.jpg", goldenRetriever.getImageUrl());

        BreedResponse labrador = result.get(1);
        assertEquals("Labrador Retriever", labrador.getName());
        assertNull(labrador.getEnergyLevel()); // ❌ Cães NÃO TÊM energy_level

        verify(restTemplate).getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class);
    }

    @Test
    @DisplayName("Deve filtrar gatos por nome")
    void shouldFilterCatsByName() {
        // Given
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(mockCatBreeds);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", "Persian");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Persian", result.get(0).getName());
        assertEquals(1, result.get(0).getEnergyLevel());
    }

    @Test
    @DisplayName("Deve filtrar cães por nome")
    void shouldFilterDogsByName() {
        // Given
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(mockDogBreeds);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("dog", "Golden");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Golden Retriever", result.get(0).getName());
        assertNull(result.get(0).getEnergyLevel());
    }

    @Test
    @DisplayName("Deve filtrar por nome de forma case-insensitive")
    void shouldFilterByNameCaseInsensitive() {
        // Given
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(mockCatBreeds);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", "BENGAL");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bengal", result.get(0).getName());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando filtro não encontra resultados")
    void shouldReturnEmptyListWhenFilterDoesNotMatch() {
        // Given
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(mockCatBreeds);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", "Inexistente");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar todas as raças quando filtro é null")
    void shouldReturnAllBreedsWhenFilterIsNull() {
        // Given
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(mockCatBreeds);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve retornar todas as raças quando filtro é string vazia")
    void shouldReturnAllBreedsWhenFilterIsEmptyString() {
        // Given
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(mockCatBreeds);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", "");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve lançar exceção para espécie inválida")
    void shouldThrowExceptionForInvalidSpecies() {
        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> breedService.getBreedsBySpecies("bird", null)
        );

        assertEquals("Erro ao consultar API externa", exception.getMessage());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        verifyNoInteractions(restTemplate);
    }

    @Test
    @DisplayName("Deve lidar com erro na API de gatos")
    void shouldHandleCatApiError() {
        // Given
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenThrow(new RestClientException("API error"));

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> breedService.getBreedsBySpecies("cat", null)
        );

        assertEquals("Erro ao consultar API externa", exception.getMessage());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Serviço TheCatAPI indisponível", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Deve lidar com erro na API de cães")
    void shouldHandleDogApiError() {
        // Given
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenThrow(new RestClientException("API error"));

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> breedService.getBreedsBySpecies("dog", null)
        );

        assertEquals("Erro ao consultar API externa", exception.getMessage());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Serviço TheDogAPI indisponível", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando API retorna null")
    void shouldReturnEmptyListWhenApiReturnsNull() {
        // Given
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(null);

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve construir URL de imagem do gato corretamente")
    void shouldBuildCatImageUrlCorrectly() {
        // Given
        CatApiBreed catWithImage = new CatApiBreed();
        catWithImage.setName("Test Cat");
        catWithImage.setReferenceImageId("test123");
        catWithImage.setEnergyLevel(3);

        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(new CatApiBreed[]{catWithImage});

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", null);

        // Then
        assertEquals("https://cdn2.thecatapi.com/images/test123.jpg", result.get(0).getImageUrl());
    }

    @Test
    @DisplayName("Deve construir URL de imagem do cão corretamente")
    void shouldBuildDogImageUrlCorrectly() {
        // Given
        DogApiBreed dogWithImage = new DogApiBreed();
        dogWithImage.setName("Test Dog");
        dogWithImage.setReferenceImageId("test456");

        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(new DogApiBreed[]{dogWithImage});

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("dog", null);

        // Then
        assertEquals("https://cdn2.thedogapi.com/images/test456.jpg", result.get(0).getImageUrl());
    }

    @Test
    @DisplayName("Deve retornar null para imagem quando referenceImageId é null")
    void shouldReturnNullImageWhenReferenceImageIdIsNull() {
        // Given
        CatApiBreed catWithoutImage = new CatApiBreed();
        catWithoutImage.setName("Test Cat");
        catWithoutImage.setReferenceImageId(null);
        catWithoutImage.setEnergyLevel(3);

        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(new CatApiBreed[]{catWithoutImage});

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", null);

        // Then
        assertNull(result.get(0).getImageUrl());
    }

    @Test
    @DisplayName("Deve ignorar breeds com nome null")
    void shouldIgnoreBreedsWithNullName() {
        // Given
        CatApiBreed validCat = new CatApiBreed();
        validCat.setName("Valid Cat");
        validCat.setEnergyLevel(3);

        CatApiBreed invalidCat = new CatApiBreed();
        invalidCat.setName(null);
        invalidCat.setEnergyLevel(2);

        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(new CatApiBreed[]{validCat, invalidCat});

        // When
        List<BreedResponse> result = breedService.getBreedsBySpecies("cat", null);

        // Then
        assertEquals(1, result.size());
        assertEquals("Valid Cat", result.get(0).getName());
    }
}