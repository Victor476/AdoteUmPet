package com.adoteumpet.adoteumpetapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração simplificados para BreedController
 * Usa TestRestTemplate ao invés de MockMvc para evitar problemas de dependência
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "external.api.thedogapi.url=http://localhost:8089/v1/breeds",
    "external.api.thecatapi.url=http://localhost:8089/v1/breeds"
})
@DisplayName("BreedController - Testes de Integração Simplificados")
class BreedControllerSimpleIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve integrar com TheCatAPI e retornar gatos com energy_level")
    void shouldIntegrateWithTheCatApiAndReturnCatsWithEnergyLevel() throws Exception {
        // Given - Mock da resposta da TheCatAPI
        String catApiResponse = """
            [
                {
                    "id": "pers",
                    "name": "Persian",
                    "origin": "Iran",
                    "temperament": "Docile, Quiet, Affectionate",
                    "energy_level": 1,
                    "image": {
                        "url": "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
                    }
                },
                {
                    "id": "beng",
                    "name": "Bengal",
                    "origin": "United States",
                    "temperament": "Alert, Agile, Energetic",
                    "energy_level": 5,
                    "image": {
                        "url": "https://cdn2.thecatapi.com/images/O3btzLlsO.jpg"
                    }
                }
            ]
            """;

        stubFor(get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(catApiResponse)));

        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/breeds/cat", List.class);

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Verifica que o primeiro gato tem energy_level
        Map<String, Object> firstCat = (Map<String, Object>) response.getBody().get(0);
        assertEquals("Persian", firstCat.get("name"));
        assertEquals("Iran", firstCat.get("origin"));
        assertEquals(1, firstCat.get("energy_level")); // ✅ Gatos TÊM energy_level

        // Verifica que o segundo gato tem energy_level
        Map<String, Object> secondCat = (Map<String, Object>) response.getBody().get(1);
        assertEquals("Bengal", secondCat.get("name"));
        assertEquals(5, secondCat.get("energy_level")); // ✅ Gatos TÊM energy_level

        // Verify API call
        verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/dog - Deve integrar com TheDogAPI e retornar cães sem energy_level")
    void shouldIntegrateWithTheDogApiAndReturnDogsWithoutEnergyLevel() throws Exception {
        // Given - Mock da resposta da TheDogAPI (sem energy_level)
        String dogApiResponse = """
            [
                {
                    "id": 1,
                    "name": "Golden Retriever",
                    "origin": "Scotland, England",
                    "temperament": "Intelligent, Kind, Reliable",
                    "image": {
                        "url": "https://cdn2.thedogapi.com/images/HJ7Pzg5EQ.jpg"
                    }
                },
                {
                    "id": 2,
                    "name": "Labrador Retriever",
                    "origin": "Canada",
                    "temperament": "Kind, Outgoing, Agile",
                    "image": {
                        "url": "https://cdn2.thedogapi.com/images/B1uW7l5VX.jpg"
                    }
                }
            ]
            """;

        stubFor(get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(dogApiResponse)));

        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/breeds/dog", List.class);

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Verifica que os cães NÃO têm energy_level
        Map<String, Object> firstDog = (Map<String, Object>) response.getBody().get(0);
        assertEquals("Golden Retriever", firstDog.get("name"));
        assertEquals("Scotland, England", firstDog.get("origin"));
        assertNull(firstDog.get("energy_level")); // ❌ Cães NÃO TÊM energy_level

        Map<String, Object> secondDog = (Map<String, Object>) response.getBody().get(1);
        assertEquals("Labrador Retriever", secondDog.get("name"));
        assertNull(secondDog.get("energy_level")); // ❌ Cães NÃO TÊM energy_level

        // Verify API call
        verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/cat?name=Persian - Deve aplicar filtro por nome na integração")
    void shouldApplyNameFilterInIntegration() throws Exception {
        // Given
        String catApiResponse = """
            [
                {
                    "id": "pers",
                    "name": "Persian",
                    "origin": "Iran",
                    "temperament": "Docile, Quiet, Affectionate",
                    "energy_level": 1,
                    "image": {
                        "url": "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
                    }
                },
                {
                    "id": "beng",
                    "name": "Bengal",
                    "origin": "United States",
                    "temperament": "Alert, Agile, Energetic",
                    "energy_level": 5,
                    "image": {
                        "url": "https://cdn2.thecatapi.com/images/O3btzLlsO.jpg"
                    }
                }
            ]
            """;

        stubFor(get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(catApiResponse)));

        // When - Aplicando filtro por nome
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/breeds/cat?name=Persian", List.class);

        // Then - Filtro aplicado no service, retorna apenas Persian
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        
        Map<String, Object> persian = (Map<String, Object>) response.getBody().get(0);
        assertEquals("Persian", persian.get("name"));
        assertEquals(1, persian.get("energy_level"));

        verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/health - Deve integrar health check com APIs reais")
    void shouldIntegrateHealthCheckWithRealApis() throws Exception {
        // Given - Mock de ambas as APIs
        String catResponse = """
            [
                {
                    "id": "pers",
                    "name": "Persian",
                    "energy_level": 1,
                    "image": {"url": "https://test.jpg"}
                }
            ]
            """;

        String dogResponse = """
            [
                {
                    "id": 1,
                    "name": "Golden Retriever",
                    "image": {"url": "https://test.jpg"}
                }
            ]
            """;

        // Configura cenário para múltiplas chamadas
        stubFor(get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(catResponse)));

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/breeds/health", String.class);

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("APIs externas funcionais"));
        assertTrue(response.getBody().contains("Gatos: 1 raças"));

        // Verify API foi chamada (pelo menos uma vez)
        verify(1, getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve tratar erro 500 da API externa")
    void shouldHandle500ErrorFromExternalApi() throws Exception {
        // Given - Mock de erro da API
        stubFor(get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Server Error")));

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/breeds/cat", String.class);

        // Then
        assertEquals(500, response.getStatusCode().value());
        verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve tratar resposta vazia da API")
    void shouldHandleEmptyApiResponse() throws Exception {
        // Given - Mock de resposta vazia
        stubFor(get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/breeds/cat", List.class);

        // Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());

        verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/bird - Deve retornar 400 para espécie inválida")
    void shouldReturn400ForInvalidSpecies() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/breeds/bird", String.class);

        // Then
        assertEquals(400, response.getStatusCode().value());
        
        // Não deve chamar a API externa para espécie inválida
        verify(0, getRequestedFor(urlEqualTo("/v1/breeds")));
    }
}