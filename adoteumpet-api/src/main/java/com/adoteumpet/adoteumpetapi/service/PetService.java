package com.adoteumpet.adoteumpetapi.service;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service responsável pela lógica de negócio relacionada aos pets.
 * Gerencia operações CRUD e regras de negócio específicas.
 */
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    /**
     * Salva um novo pet no banco de dados.
     * @param pet o pet a ser salvo
     * @return o pet salvo com ID gerado
     */
    public Pet savePet(Pet pet) {
        // Define status como AVAILABLE por padrão se não especificado
        if (pet.getStatus() == null) {
            pet.setStatus(Status.AVAILABLE);
        }
        return petRepository.save(pet);
    }

    /**
     * Busca todos os pets cadastrados.
     * @return lista de todos os pets
     */
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    /**
     * Busca um pet pelo ID.
     * @param id o ID do pet
     * @return Optional contendo o pet se encontrado
     */
    public Optional<Pet> getPetById(UUID id) {
        return petRepository.findById(id);
    }

    /**
     * Busca pets por espécie.
     * @param species a espécie desejada
     * @return lista de pets da espécie especificada
     */
    public List<Pet> getPetsBySpecies(Species species) {
        return petRepository.findBySpecies(species);
    }

    /**
     * Busca pets por status.
     * @param status o status desejado
     * @return lista de pets com o status especificado
     */
    public List<Pet> getPetsByStatus(Status status) {
        return petRepository.findByStatus(status);
    }

    /**
     * Busca pets disponíveis para adoção.
     * @return lista de pets disponíveis
     */
    public List<Pet> getAvailablePets() {
        return petRepository.findAvailablePets();
    }

    /**
     * Busca pets por cidade do abrigo.
     * @param city a cidade do abrigo
     * @return lista de pets na cidade especificada
     */
    public List<Pet> getPetsByCity(String city) {
        return petRepository.findByShelterCity(city);
    }

    /**
     * Busca pets por faixa etária.
     * @param minAge idade mínima
     * @param maxAge idade máxima
     * @return lista de pets na faixa etária
     */
    public List<Pet> getPetsByAgeRange(Integer minAge, Integer maxAge) {
        return petRepository.findByAgeBetween(minAge, maxAge);
    }

    /**
     * Atualiza um pet existente.
     * @param id o ID do pet a ser atualizado
     * @param updatedPet os dados atualizados do pet
     * @return Optional contendo o pet atualizado se encontrado
     */
    public Optional<Pet> updatePet(UUID id, Pet updatedPet) {
        Optional<Pet> existingPet = petRepository.findById(id);
        if (existingPet.isPresent()) {
            Pet pet = existingPet.get();
            pet.setName(updatedPet.getName());
            pet.setSpecies(updatedPet.getSpecies());
            pet.setBreed(updatedPet.getBreed());
            pet.setAgeYears(updatedPet.getAgeYears());
            pet.setShelterCity(updatedPet.getShelterCity());
            pet.setShelterLat(updatedPet.getShelterLat());
            pet.setShelterLng(updatedPet.getShelterLng());
            pet.setStatus(updatedPet.getStatus());
            return Optional.of(petRepository.save(pet));
        }
        return Optional.empty();
    }

    /**
     * Marca um pet como adotado.
     * @param id o ID do pet
     * @return true se o pet foi encontrado e atualizado, false caso contrário
     */
    public boolean adoptPet(UUID id) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent() && pet.get().getStatus() == Status.AVAILABLE) {
            pet.get().setStatus(Status.ADOPTED);
            petRepository.save(pet.get());
            return true;
        }
        return false;
    }

    /**
     * Remove um pet do banco de dados.
     * @param id o ID do pet a ser removido
     * @return true se o pet foi encontrado e removido, false caso contrário
     */
    public boolean deletePet(UUID id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
            return true;
        }
        return false;
    }
}