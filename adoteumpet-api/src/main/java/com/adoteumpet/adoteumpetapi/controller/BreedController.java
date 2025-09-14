package com.adoteumpet.adoteumpetapi.controller;

import com.adoteumpet.adoteumpetapi.dto.BreedResponse;
import com.adoteumpet.adoteumpetapi.service.BreedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para endpoints de raças de animais
 * 
 * Integração com APIs externas:
 * - Gatos: TheCatAPI (COM energy_level 1-5)
 * - Cães: TheDogAPI (SEM energy_level)
 */
@Tag(name = "Breeds", description = "Operações relacionadas a raças de cães e gatos")
@RestController
@RequestMapping("/breeds")
@CrossOrigin(origins = "*")
public class BreedController {

    private static final Logger logger = LoggerFactory.getLogger(BreedController.class);

    @Autowired
    private BreedService breedService;

    /**
     * Busca raças por espécie (cat/dog)
     * 
     * @param species Espécie do animal ('cat' ou 'dog')
     * @param name Filtro opcional por nome da raça
     * @return Lista de raças encontradas
     */
    @Operation(summary = "Buscar raças por espécie", 
               description = "Retorna lista de raças de cães ou gatos com informações detalhadas. " +
                           "Para gatos inclui nível de energia (1-5), para cães não inclui essa informação.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de raças retornada com sucesso",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = BreedResponse.class))),
        @ApiResponse(responseCode = "400", description = "Espécie inválida (deve ser 'cat' ou 'dog')"),
        @ApiResponse(responseCode = "500", description = "Erro interno ao consultar APIs externas")
    })
    @GetMapping("/{species}")
    public ResponseEntity<List<BreedResponse>> getBreedsBySpecies(
            @Parameter(description = "Espécie do animal", example = "cat", schema = @Schema(allowableValues = {"cat", "dog"}))
            @PathVariable String species,
            @Parameter(description = "Filtro opcional por nome da raça", example = "Persian")
            @RequestParam(required = false) String name) {

        logger.info("🌐 GET /breeds/{} - Filtro nome: {}", species, name);

        try {
            // Validação básica da espécie
            if (!isValidSpecies(species)) {
                logger.warn("⚠️ Espécie inválida recebida: {}", species);
                return ResponseEntity.badRequest().build();
            }

            // Busca as raças via service
            List<BreedResponse> breeds = breedService.getBreedsBySpecies(species, name);
            
            logger.info("✅ Retornando {} raças para species: {}", breeds.size(), species);
            return ResponseEntity.ok(breeds);

        } catch (IllegalArgumentException e) {
            logger.error("❌ Parâmetro inválido: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
            
        } catch (Exception e) {
            logger.error("❌ Erro inesperado ao buscar raças para species: {}", species, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint de saúde para verificar integração com APIs externas
     */
    @Operation(summary = "Health check das APIs externas", 
               description = "Verifica se as APIs externas (TheCatAPI e TheDogAPI) estão funcionais")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "APIs externas funcionais"),
        @ApiResponse(responseCode = "500", description = "Erro nas APIs externas")
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        logger.info("🔍 Health check das APIs externas");
        
        try {
            // Testa busca básica de 1 gato e 1 cão
            List<BreedResponse> testCats = breedService.getBreedsBySpecies("cat", null);
            List<BreedResponse> testDogs = breedService.getBreedsBySpecies("dog", null);
            
            String status = String.format(
                "✅ APIs externas funcionais - Gatos: %d raças, Cães: %d raças", 
                testCats.size(), testDogs.size()
            );
            
            logger.info(status);
            return ResponseEntity.ok(status);
            
        } catch (Exception e) {
            String errorMsg = "❌ Erro nas APIs externas: " + e.getMessage();
            logger.error(errorMsg, e);
            return ResponseEntity.internalServerError().body(errorMsg);
        }
    }

    /**
     * Valida se a espécie é suportada
     */
    private boolean isValidSpecies(String species) {
        if (species == null) {
            return false;
        }
        
        String lowerSpecies = species.toLowerCase().trim();
        return "cat".equals(lowerSpecies) || "dog".equals(lowerSpecies);
    }
}