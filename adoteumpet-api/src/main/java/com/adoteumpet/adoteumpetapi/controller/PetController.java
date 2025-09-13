package com.adoteumpet.adoteumpetapi.controller;

import com.adoteumpet.adoteumpetapi.dto.PagedResponse;
import com.adoteumpet.adoteumpetapi.dto.PetCreateDTO;
import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.service.PetService;
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
@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * Endpoint para criar um novo pet.
     * @param petCreateDTO os dados do pet a ser criado
     * @return o pet criado com status 201
     */
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
    @GetMapping
    public ResponseEntity<PagedResponse<Pet>> getPets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Species species,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String shelterCity,
            @RequestParam(required = false) Status status,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        
        PagedResponse<Pet> pets = petService.findPets(name, species, breed, shelterCity, status, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Endpoint para buscar um pet pelo ID.
     * @param id o ID do pet
     * @return o pet encontrado ou 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable UUID id) {
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