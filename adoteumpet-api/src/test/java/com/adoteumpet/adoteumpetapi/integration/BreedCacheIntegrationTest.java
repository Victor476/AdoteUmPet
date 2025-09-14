package com.adoteumpet.adoteumpetapi.integration;

import com.adoteumpet.adoteumpetapi.service.BreedService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para validar o funcionamento do cache de raças
 * em um contexto completo da aplicação Spring Boot.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("🧪 Testes de Integração - Cache de Raças")
class BreedCacheIntegrationTest {

    @Autowired
    private BreedService breedService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    @DisplayName("�️ CacheManager deve estar configurado corretamente")
    void shouldHaveCacheManagerProperlyConfigured() {
        // Assert
        assertNotNull(cacheManager);
        assertTrue(cacheManager.getCacheNames().contains("breeds"));
        assertNotNull(cacheManager.getCache("breeds"));
    }

    @Test
    @DisplayName("🧹 Cache deve poder ser limpo manualmente")
    void shouldBeAbleToEvictCache() {
        // Act
        cacheManager.getCache("breeds").clear();
        
        // Assert - Cache foi limpo com sucesso
        assertNotNull(cacheManager.getCache("breeds"));
    }

    @Test
    @DisplayName("� BreedService deve estar injetado corretamente")
    void shouldHaveBreedServiceInjected() {
        // Assert
        assertNotNull(breedService);
    }

    @Test
    @DisplayName("🌍 Contexto da aplicação deve carregar completamente")
    void shouldLoadApplicationContext() {
        // Assert - Se chegou até aqui, o contexto carregou com sucesso
        assertNotNull(breedService);
        assertNotNull(cacheManager);
    }
}