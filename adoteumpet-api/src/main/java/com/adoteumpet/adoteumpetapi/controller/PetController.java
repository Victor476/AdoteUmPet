package com.adoteumpet.adoteumpetapi.controller;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param pet os dados do pet a ser criado
     * @return o pet criado com status 201
     */
    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        Pet savedPet = petService.savePet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
    }

    /**
     * Endpoint para buscar todos os pets.
     * @return lista de todos os pets
     */
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    /**
     * Endpoint para buscar um pet pelo ID.
     * @param id o ID do pet
     * @return o pet encontrado ou 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable UUID id) {
        Optional<Pet> pet = petService.getPetById(id);
        return pet.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
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
        Optional<Pet> updatedPet = petService.updatePet(id, pet);
        return updatedPet.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
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