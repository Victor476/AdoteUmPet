package com.adoteumpet.adoteumpetapi.controller;

import com.adoteumpet.adoteumpetapi.dto.PagedResponse;
import com.adoteumpet.adoteumpetapi.dto.PetCreateDTO;
import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller REST para gerenciar operações relacionadas aos pets.
 * Fornece endpoints para CRUD e consultas específicas.
 */
@Tag(name = "Pets", description = "Operações relacionadas ao gerenciamento de pets para adoção")
@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * Endpoint para criar um novo pet.
     * @param petCreateDTO os dados do pet a ser criado
     * @return o pet criado com status 201
     */
    @Operation(summary = "Criar um novo pet", 
               description = "Cadastra um novo pet no sistema para disponibilizar para adoção")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pet criado com sucesso",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pet.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "422", description = "Erro de validação nos campos")
    })
    @PostMapping
    public ResponseEntity<Pet> createPet(@Valid @RequestBody PetCreateDTO petCreateDTO) {
        Pet pet = convertToEntity(petCreateDTO);
        Pet savedPet = petService.savePet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
    }

    /**
     * Converte um DTO de criação para uma entidade Pet.
     * @param dto o DTO com os dados do pet
     * @return a entidade Pet convertida
     */
    private Pet convertToEntity(PetCreateDTO dto) {
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setAgeYears(dto.getAgeYears());
        pet.setShelterCity(dto.getShelterCity());
        pet.setShelterLat(dto.getShelterLat());
        pet.setShelterLng(dto.getShelterLng());
        
        // Define status como AVAILABLE por padrão se não especificado
        pet.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.AVAILABLE);
        
        return pet;
    }

    /**
     * Endpoint para buscar pets com filtros, paginação e ordenação.
     * @param name filtro por nome (opcional)
     * @param species filtro por espécie (opcional)
     * @param breed filtro por raça (opcional)
     * @param shelterCity filtro por cidade do abrigo (opcional)
     * @param status filtro por status (opcional)
     * @param pageable configuração de paginação e ordenação (padrão: page=0, size=10, sort=name,asc)
     * @return resposta paginada com os pets encontrados
     */
    @Operation(summary = "Buscar pets", 
               description = "Busca pets com filtros opcionais, paginação e ordenação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pets retornada com sucesso",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = PagedResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PagedResponse<Pet>> getPets(
            @Parameter(description = "Filtro por nome do pet") @RequestParam(required = false) String name,
            @Parameter(description = "Filtro por espécie (CAT ou DOG)") @RequestParam(required = false) Species species,
            @Parameter(description = "Filtro por raça do pet") @RequestParam(required = false) String breed,
            @Parameter(description = "Filtro por cidade do abrigo") @RequestParam(required = false) String shelterCity,
            @Parameter(description = "Filtro por status (AVAILABLE ou ADOPTED)") @RequestParam(required = false) Status status,
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        
        PagedResponse<Pet> pets = petService.findPets(name, species, breed, shelterCity, status, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Endpoint para buscar um pet pelo ID.
     * @param id o ID do pet
     * @return o pet encontrado ou 404 se não existir
     */
    @Operation(summary = "Buscar pet por ID", 
               description = "Retorna um pet específico pelo seu identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pet encontrado",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = Pet.class))),
        @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@Parameter(description = "ID único do pet") @PathVariable UUID id) {
        Pet pet = petService.getPetById(id);
        return ResponseEntity.ok(pet);
    }

    /**
     * Endpoint para buscar pets por espécie.
     * @param species a espécie desejada
     * @return lista de pets da espécie especificada
     */
    @GetMapping("/species/{species}")
    public ResponseEntity<List<Pet>> getPetsBySpecies(@PathVariable Species species) {
        List<Pet> pets = petService.getPetsBySpecies(species);
        return ResponseEntity.ok(pets);
    }

    /**
     * Endpoint para buscar pets por status.
     * @param status o status desejado
     * @return lista de pets com o status especificado
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pet>> getPetsByStatus(@PathVariable Status status) {
        List<Pet> pets = petService.getPetsByStatus(status);
        return ResponseEntity.ok(pets);
    }

    /**
     * Endpoint para buscar pets disponíveis para adoção.
     * @return lista de pets disponíveis
     */
    @GetMapping("/available")
    public ResponseEntity<List<Pet>> getAvailablePets() {
        List<Pet> pets = petService.getAvailablePets();
        return ResponseEntity.ok(pets);
    }

    /**
     * Endpoint para buscar pets por cidade do abrigo.
     * @param city a cidade do abrigo
     * @return lista de pets na cidade especificada
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Pet>> getPetsByCity(@PathVariable String city) {
        List<Pet> pets = petService.getPetsByCity(city);
        return ResponseEntity.ok(pets);
    }

    /**
     * Endpoint para buscar pets por faixa etária.
     * @param minAge idade mínima
     * @param maxAge idade máxima
     * @return lista de pets na faixa etária
     */
    @GetMapping("/age")
    public ResponseEntity<List<Pet>> getPetsByAgeRange(
            @RequestParam Integer minAge, 
            @RequestParam Integer maxAge) {
        List<Pet> pets = petService.getPetsByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(pets);
    }

    /**
     * Endpoint para atualizar um pet existente.
     * @param id o ID do pet a ser atualizado
     * @param pet os dados atualizados
     * @return o pet atualizado ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable UUID id, @RequestBody Pet pet) {
        Pet updatedPet = petService.updatePet(id, pet);
        return ResponseEntity.ok(updatedPet);
    }

    /**
     * Endpoint para marcar um pet como adotado.
     * @param id o ID do pet
     * @return 200 se bem-sucedido, 404 se pet não encontrado, 400 se não disponível
     */
    @PatchMapping("/{id}/adopt")
    public ResponseEntity<Void> adoptPet(@PathVariable UUID id) {
        boolean adopted = petService.adoptPet(id);
        if (adopted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para remover um pet.
     * @param id o ID do pet a ser removido
     * @return 204 se bem-sucedido, 404 se não encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable UUID id) {
        boolean deleted = petService.deletePet(id);
        return deleted ? ResponseEntity.noContent().build() 
                       : ResponseEntity.notFound().build();
    }
}