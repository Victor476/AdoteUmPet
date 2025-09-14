package com.adoteumpet.adoteumpetapi.service;

import com.adoteumpet.adoteumpetapi.dto.BreedResponse;
import com.adoteumpet.adoteumpetapi.dto.external.CatApiBreed;
import com.adoteumpet.adoteumpetapi.dto.external.DogApiBreed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o cache do BreedService
 * 
 * Verifica se o cache está funcionando corretamente:
 * - Cache hit/miss para diferentes combinações de species + nameFilter
 * - Limpeza do cache
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("BreedService Cache - Testes Unitários")
class BreedServiceCacheTest {

    @Autowired
    private BreedService breedService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @MockBean
    private RestTemplate restTemplate;
    
    private CatApiBreed[] mockCatBreeds;
    private DogApiBreed[] mockDogBreeds;

    @BeforeEach
    void setUp() {
        // Limpa o cache antes de cada teste
        cacheManager.getCache("breeds").clear();
        
        // Mock data para gatos
        CatApiBreed persian = new CatApiBreed();
        persian.setName("Persian");
        persian.setOrigin("Iran");
        persian.setTemperament("Docile, Quiet");
        persian.setEnergyLevel(2);
        persian.setReferenceImageId("persian123");
        
        CatApiBreed bengal = new CatApiBreed();
        bengal.setName("Bengal");
        bengal.setOrigin("United States");
        bengal.setTemperament("Alert, Agile");
        bengal.setEnergyLevel(5);
        bengal.setReferenceImageId("bengal456");
        
        mockCatBreeds = new CatApiBreed[]{persian, bengal};
        
        // Mock data para cães
        DogApiBreed labrador = new DogApiBreed();
        labrador.setName("Labrador Retriever");
        labrador.setOrigin("Canada");
        labrador.setTemperament("Kind, Outgoing");
        labrador.setReferenceImageId("labrador789");
        
        DogApiBreed golden = new DogApiBreed();
        golden.setName("Golden Retriever");
        golden.setOrigin("Scotland");
        golden.setTemperament("Intelligent, Kind");
        golden.setReferenceImageId("golden012");
        
        mockDogBreeds = new DogApiBreed[]{labrador, golden};
    }

    @Test
    @DisplayName("Deve cachear raças de cães quando chamado múltiplas vezes")
    void shouldCacheDogsBreeds_WhenCalledMultipleTimes() {
        // Arrange
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(mockDogBreeds);
        
        // Act - Primeira chamada (cache miss)
        List<BreedResponse> firstCall = breedService.getBreedsBySpecies("dog", null);
        
        // Act - Segunda chamada (cache hit)
        List<BreedResponse> secondCall = breedService.getBreedsBySpecies("dog", null);
        
        // Assert
        assertEquals(2, firstCall.size());
        assertEquals(2, secondCall.size());
        assertEquals(firstCall.get(0).getName(), secondCall.get(0).getName());
        assertSame(firstCall, secondCall); // Deve ser a mesma instância do cache
        
        // Verifica que a API externa foi chamada apenas uma vez
        verify(restTemplate, times(1)).getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class);
    }

    @Test
    @DisplayName("Deve cachear raças de gatos quando chamado múltiplas vezes")
    void shouldCacheCatsBreeds_WhenCalledMultipleTimes() {
        // Arrange
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(mockCatBreeds);
        
        // Act - Primeira chamada (cache miss)
        List<BreedResponse> firstCall = breedService.getBreedsBySpecies("cat", null);
        
        // Act - Segunda chamada (cache hit)
        List<BreedResponse> secondCall = breedService.getBreedsBySpecies("cat", null);
        
        // Assert
        assertEquals(2, firstCall.size());
        assertEquals(2, secondCall.size());
        assertEquals(firstCall.get(0).getName(), secondCall.get(0).getName());
        assertSame(firstCall, secondCall); // Deve ser a mesma instância do cache
        
        // Verifica que a API externa foi chamada apenas uma vez
        verify(restTemplate, times(1)).getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class);
    }

    @Test
    @DisplayName("Deve cachear separadamente quando nameFilter diferentes")
    void shouldCacheSeparately_WhenDifferentNameFilters() {
        // Arrange
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(mockDogBreeds);
        
        // Act - Chamadas com filtros diferentes
        List<BreedResponse> withoutFilter = breedService.getBreedsBySpecies("dog", null);
        List<BreedResponse> withLabFilter = breedService.getBreedsBySpecies("dog", "Lab");
        List<BreedResponse> withoutFilterAgain = breedService.getBreedsBySpecies("dog", null);
        
        // Assert
        assertEquals(2, withoutFilter.size());
        assertEquals(1, withLabFilter.size()); // Filtrado para "Lab"
        assertEquals("Labrador Retriever", withLabFilter.get(0).getName());
        assertEquals(2, withoutFilterAgain.size());
        
        // Verifica que sem filtro foi chamado apenas uma vez (cache hit na terceira)
        // Com filtro foi uma nova consulta (chave de cache diferente)
        verify(restTemplate, times(2)).getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class);
    }

    @Test
    @DisplayName("Deve cachear separadamente quando diferentes espécies")
    void shouldCacheSeparately_WhenDifferentSpecies() {
        // Arrange
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(mockDogBreeds);
        when(restTemplate.getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class))
                .thenReturn(mockCatBreeds);
        
        // Act
        List<BreedResponse> dogs = breedService.getBreedsBySpecies("dog", null);
        List<BreedResponse> cats = breedService.getBreedsBySpecies("cat", null);
        List<BreedResponse> dogsAgain = breedService.getBreedsBySpecies("dog", null);
        List<BreedResponse> catsAgain = breedService.getBreedsBySpecies("cat", null);
        
        // Assert
        assertEquals(2, dogs.size());
        assertEquals(2, cats.size());
        assertEquals(2, dogsAgain.size());
        assertEquals(2, catsAgain.size());
        
        // Cada espécie deve ter sido chamada apenas uma vez
        verify(restTemplate, times(1)).getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class);
        verify(restTemplate, times(1)).getForObject("https://api.thecatapi.com/v1/breeds", CatApiBreed[].class);
    }

    @Test
    @DisplayName("Deve gerar chaves de cache corretas com diferentes parâmetros")
    void shouldGenerateCorrectCacheKey_WithDifferentParameters() {
        // Arrange
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(mockDogBreeds);
        
        // Act - Diferentes combinações de parâmetros
        breedService.getBreedsBySpecies("dog", null);      // Chave: "dog_all"
        breedService.getBreedsBySpecies("dog", "Lab");     // Chave: "dog_Lab"
        breedService.getBreedsBySpecies("dog", "Golden");  // Chave: "dog_Golden"
        
        breedService.getBreedsBySpecies("dog", null);      // Cache hit: "dog_all"
        breedService.getBreedsBySpecies("dog", "Lab");     // Cache hit: "dog_Lab"
        
        // Assert - Deve ter sido chamado 3 vezes (uma para cada chave única)
        verify(restTemplate, times(3)).getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class);
    }

    @Test
    @DisplayName("Deve lançar exceção para espécie inválida sem usar cache")
    void shouldNotCache_WhenInvalidSpecies() {
        // Act & Assert - O serviço envolve IllegalArgumentException em RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            breedService.getBreedsBySpecies("bird", null);
        });
        
        // Verifica que a causa é IllegalArgumentException
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertTrue(exception.getCause().getMessage().contains("Espécie inválida: bird"));
        
        // Verifica que as APIs externas não foram chamadas
        verify(restTemplate, never()).getForObject(anyString(), eq(DogApiBreed[].class));
        verify(restTemplate, never()).getForObject(anyString(), eq(CatApiBreed[].class));
    }

    @Test
    @DisplayName("Deve limpar cache quando evictAll é chamado")
    void shouldClearCache_WhenEvictAll() {
        // Arrange
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(mockDogBreeds);
        
        // Act - Primeira chamada (cache miss)
        breedService.getBreedsBySpecies("dog", null);
        verify(restTemplate, times(1)).getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class);
        
        // Act - Limpa o cache manualmente
        cacheManager.getCache("breeds").clear();
        
        // Act - Segunda chamada após limpar cache (cache miss novamente)
        breedService.getBreedsBySpecies("dog", null);
        
        // Assert - API deve ter sido chamada novamente
        verify(restTemplate, times(2)).getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class);
    }

    @Test
    @DisplayName("Deve demonstrar cache hit em teste isolado")
    void shouldDemonstrateRealCacheHit() {
        // Arrange - Não limpa cache neste teste específico
        when(restTemplate.getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class))
                .thenReturn(mockDogBreeds);
        
        // Act - Múltiplas chamadas consecutivas
        List<BreedResponse> call1 = breedService.getBreedsBySpecies("dog", null);
        List<BreedResponse> call2 = breedService.getBreedsBySpecies("dog", null);
        List<BreedResponse> call3 = breedService.getBreedsBySpecies("dog", null);
        
        // Assert - Todas devem retornar os mesmos dados
        assertEquals(2, call1.size());
        assertEquals(2, call2.size());
        assertEquals(2, call3.size());
        
        // Cache funciona: primeira chamada cria cache, as outras usam
        assertSame(call1, call2);
        assertSame(call2, call3);
        
        // API foi chamada apenas uma vez (primeira chamada)
        verify(restTemplate, times(1)).getForObject("https://api.thedogapi.com/v1/breeds", DogApiBreed[].class);
    }
}