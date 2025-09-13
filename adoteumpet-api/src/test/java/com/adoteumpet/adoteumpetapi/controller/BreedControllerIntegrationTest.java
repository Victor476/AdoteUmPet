package com.adoteumpet.adoteumpetapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para BreedController
 * Testa integração completa com APIs externas usando WireMock
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "external.api.thedogapi.url=http://localhost:8089",
    "external.api.thecatapi.url=http://localhost:8089"
})
@DisplayName("BreedController - Testes de Integração")
class BreedControllerIntegrationTest {

    private WireMockServer wireMockServer;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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

        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(catApiResponse)));

        // When & Then
        mockMvc.perform(get("/breeds/cat"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Persian"))
                .andExpect(jsonPath("$[0].origin").value("Iran"))
                .andExpect(jsonPath("$[0].temperament").value("Docile, Quiet, Affectionate"))
                .andExpect(jsonPath("$[0].energy_level").value(1)) // ✅ Gatos TÊM energy_level
                .andExpect(jsonPath("$[0].image_url").value("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"))
                .andExpect(jsonPath("$[1].name").value("Bengal"))
                .andExpect(jsonPath("$[1].energy_level").value(5)); // ✅ Gatos TÊM energy_level

        // Verify API call
        wireMock.verify(getRequestedFor(urlEqualTo("/v1/breeds")));
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

        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(dogApiResponse)));

        // When & Then
        mockMvc.perform(get("/breeds/dog"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Golden Retriever"))
                .andExpect(jsonPath("$[0].origin").value("Scotland, England"))
                .andExpect(jsonPath("$[0].temperament").value("Intelligent, Kind, Reliable"))
                .andExpect(jsonPath("$[0].energy_level").doesNotExist()) // ❌ Cães NÃO TÊM energy_level
                .andExpect(jsonPath("$[0].image_url").value("https://cdn2.thedogapi.com/images/HJ7Pzg5EQ.jpg"))
                .andExpect(jsonPath("$[1].name").value("Labrador Retriever"))
                .andExpect(jsonPath("$[1].energy_level").doesNotExist()); // ❌ Cães NÃO TÊM energy_level

        // Verify API call
        wireMock.verify(getRequestedFor(urlEqualTo("/v1/breeds")));
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

        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(catApiResponse)));

        // When & Then - Filtro aplicado no service
        mockMvc.perform(get("/breeds/cat").param("name", "Persian"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Persian"))
                .andExpect(jsonPath("$[0].energy_level").value(1));

        wireMock.verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve tratar erro 500 da API externa")
    void shouldHandle500ErrorFromExternalApi() throws Exception {
        // Given - Mock de erro da API
        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Server Error")));

        // When & Then
        mockMvc.perform(get("/breeds/cat"))
                .andExpect(status().isInternalServerError());

        wireMock.verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve tratar timeout da API externa")
    void shouldHandleTimeoutFromExternalApi() throws Exception {
        // Given - Mock de timeout
        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(30000) // 30 segundos de delay
                        .withBody("[]")));

        // When & Then
        mockMvc.perform(get("/breeds/cat"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve tratar resposta malformada da API")
    void shouldHandleMalformedApiResponse() throws Exception {
        // Given - Mock de JSON inválido
        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ invalid json }")));

        // When & Then
        mockMvc.perform(get("/breeds/cat"))
                .andExpect(status().isInternalServerError());

        wireMock.verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve tratar resposta vazia da API")
    void shouldHandleEmptyApiResponse() throws Exception {
        // Given - Mock de resposta vazia
        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        // When & Then
        mockMvc.perform(get("/breeds/cat"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        wireMock.verify(getRequestedFor(urlEqualTo("/v1/breeds")));
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

        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .inScenario("Health Check")
                .whenScenarioStateIs("Started")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(catResponse))
                .willSetStateTo("Cat Called"));

        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .inScenario("Health Check")
                .whenScenarioStateIs("Cat Called")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(dogResponse)));

        // When & Then
        mockMvc.perform(get("/breeds/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("✅ APIs externas funcionais - Gatos: 1 raças, Cães: 1 raças"));

        // Verify both APIs were called
        wireMock.verify(2, getRequestedFor(urlEqualTo("/v1/breeds")));
    }

    @Test
    @DisplayName("GET /breeds/cat - Deve funcionar com dados reais das APIs")
    void shouldWorkWithRealistic猫Data() throws Exception {
        // Given - Mock com dados mais realistas
        String realisticCatResponse = """
            [
                {
                    "id": "abys",
                    "name": "Abyssinian",
                    "origin": "Egypt",
                    "temperament": "Active, Energetic, Independent, Intelligent, Gentle",
                    "energy_level": 5,
                    "life_span": "14 - 15",
                    "weight": {
                        "imperial": "7  -  10",
                        "metric": "3 - 5"
                    },
                    "image": {
                        "id": "0XYvRd7oD",
                        "width": 1204,
                        "height": 1445,
                        "url": "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
                    }
                },
                {
                    "id": "aege",
                    "name": "Aegean",
                    "origin": "Greece",
                    "temperament": "Affectionate, Social, Intelligent, Playful, Active",
                    "energy_level": 3,
                    "life_span": "9 - 12",
                    "weight": {
                        "imperial": "7 - 10",
                        "metric": "3 - 5"
                    },
                    "image": {
                        "id": "ozEvzdVM-",
                        "width": 1200,
                        "height": 800,
                        "url": "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg"
                    }
                }
            ]
            """;

        wireMock.stubFor(WireMock.get(urlEqualTo("/v1/breeds"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(realisticCatResponse)));

        // When & Then
        mockMvc.perform(get("/breeds/cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Abyssinian"))
                .andExpect(jsonPath("$[0].origin").value("Egypt"))
                .andExpect(jsonPath("$[0].temperament").value("Active, Energetic, Independent, Intelligent, Gentle"))
                .andExpect(jsonPath("$[0].energy_level").value(5)) // ✅ Energy level presente
                .andExpect(jsonPath("$[0].image_url").value("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"))
                .andExpect(jsonPath("$[1].name").value("Aegean"))
                .andExpect(jsonPath("$[1].energy_level").value(3)); // ✅ Energy level presente

        wireMock.verify(getRequestedFor(urlEqualTo("/v1/breeds")));
    }
}