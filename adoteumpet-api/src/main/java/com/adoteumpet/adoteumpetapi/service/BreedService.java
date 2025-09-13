package com.adoteumpet.adoteumpetapi.service;

import com.adoteumpet.adoteumpetapi.dto.BreedResponse;
import com.adoteumpet.adoteumpetapi.dto.external.CatApiBreed;
import com.adoteumpet.adoteumpetapi.dto.external.DogApiBreed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Serviço simplificado para consumir APIs externas de raças
 * - Gatos: TheCatAPI (COM energy_level 1-5)
 * - Cães: TheDogAPI (SEM energy_level)
 */
@Service
public class BreedService {

    private static final Logger logger = LoggerFactory.getLogger(BreedService.class);

    // URLs das APIs externas
    private static final String THE_DOG_API_URL = "https://api.thedogapi.com/v1/breeds";
    private static final String THE_CAT_API_URL = "https://api.thecatapi.com/v1/breeds";
    private static final String DOG_IMAGE_BASE_URL = "https://cdn2.thedogapi.com/images/";
    private static final String CAT_IMAGE_BASE_URL = "https://cdn2.thecatapi.com/images/";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Busca raças por espécie com filtro opcional por nome
     */
    public List<BreedResponse> getBreedsBySpecies(String species, String nameFilter) {
        logger.info("🔍 Buscando raças para species: {} com filtro: {}", species, nameFilter);

        try {
            switch (species.toLowerCase()) {
                case "cat":
                    return getCatBreeds(nameFilter);
                case "dog":
                    return getDogBreeds(nameFilter);
                default:
                    throw new IllegalArgumentException("Espécie inválida: " + species + ". Use 'cat' ou 'dog'.");
            }
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar raças para species: {}", species, e);
            throw new RuntimeException("Erro ao consultar API externa", e);
        }
    }

    /**
     * Busca raças de gatos na TheCatAPI (COM energy_level)
     */
    private List<BreedResponse> getCatBreeds(String nameFilter) {
        logger.info("🐱 Consultando TheCatAPI para gatos...");

        try {
            CatApiBreed[] catBreeds = restTemplate.getForObject(THE_CAT_API_URL, CatApiBreed[].class);

            if (catBreeds == null) {
                logger.warn("⚠️ TheCatAPI retornou resposta vazia");
                return Collections.emptyList();
            }

            List<BreedResponse> result = Arrays.stream(catBreeds)
                    .filter(Objects::nonNull)
                    .filter(breed -> isMatchingName(breed.getName(), nameFilter))
                    .map(this::mapCatBreedToResponse)
                    .collect(Collectors.toList());

            logger.info("✅ Encontradas {} raças de gatos", result.size());
            return result;

        } catch (RestClientException e) {
            logger.error("❌ Erro ao consultar TheCatAPI", e);
            throw new RuntimeException("Serviço TheCatAPI indisponível", e);
        }
    }

    /**
     * Busca raças de cães na TheDogAPI (SEM energy_level)
     */
    private List<BreedResponse> getDogBreeds(String nameFilter) {
        logger.info("🐶 Consultando TheDogAPI para cães...");

        try {
            DogApiBreed[] dogBreeds = restTemplate.getForObject(THE_DOG_API_URL, DogApiBreed[].class);

            if (dogBreeds == null) {
                logger.warn("⚠️ TheDogAPI retornou resposta vazia");
                return Collections.emptyList();
            }

            List<BreedResponse> result = Arrays.stream(dogBreeds)
                    .filter(Objects::nonNull)
                    .filter(breed -> isMatchingName(breed.getName(), nameFilter))
                    .map(this::mapDogBreedToResponse)
                    .collect(Collectors.toList());

            logger.info("✅ Encontradas {} raças de cães", result.size());
            return result;

        } catch (RestClientException e) {
            logger.error("❌ Erro ao consultar TheDogAPI", e);
            throw new RuntimeException("Serviço TheDogAPI indisponível", e);
        }
    }

    /**
     * Converte CatApiBreed para BreedResponse (COM energy_level)
     */
    private BreedResponse mapCatBreedToResponse(CatApiBreed catBreed) {
        String imageUrl = buildCatImageUrl(catBreed.getReferenceImageId());
        
        return new BreedResponse(
                catBreed.getName(),
                catBreed.getOrigin(),
                catBreed.getTemperament(),
                catBreed.getEnergyLevel(), // ✅ GATOS TÊM energy_level
                imageUrl
        );
    }

    /**
     * Converte DogApiBreed para BreedResponse (SEM energy_level)
     */
    private BreedResponse mapDogBreedToResponse(DogApiBreed dogBreed) {
        String imageUrl = buildDogImageUrl(dogBreed.getReferenceImageId());
        
        return new BreedResponse(
                dogBreed.getName(),
                dogBreed.getOrigin(),
                dogBreed.getTemperament(),
                null, // ❌ CÃES NÃO TÊM energy_level (será omitido do JSON)
                imageUrl
        );
    }

    /**
     * Verifica se o nome da raça corresponde ao filtro
     */
    private boolean isMatchingName(String breedName, String nameFilter) {
        // Sempre filtra nomes null/empty
        if (breedName == null || breedName.trim().isEmpty()) {
            return false;
        }
        
        if (nameFilter == null || nameFilter.trim().isEmpty()) {
            return true; // Sem filtro, retorna todos com nome válido
        }

        return breedName.toLowerCase().contains(nameFilter.toLowerCase().trim());
    }

    /**
     * Constrói URL da imagem do cão
     */
    private String buildDogImageUrl(String referenceImageId) {
        if (referenceImageId == null || referenceImageId.trim().isEmpty()) {
            return null;
        }
        return DOG_IMAGE_BASE_URL + referenceImageId + ".jpg";
    }

    /**
     * Constrói URL da imagem do gato
     */
    private String buildCatImageUrl(String referenceImageId) {
        if (referenceImageId == null || referenceImageId.trim().isEmpty()) {
            return null;
        }
        return CAT_IMAGE_BASE_URL + referenceImageId + ".jpg";
    }
}