package com.adoteumpet.adoteumpetapi.service;

import com.adoteumpet.adoteumpetapi.dto.PagedResponse;
import com.adoteumpet.adoteumpetapi.exception.ResourceNotFoundException;
import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.repository.PetRepository;
import com.adoteumpet.adoteumpetapi.specification.PetSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
     * Busca pets com filtros, paginação e ordenação.
     * @param name filtro por nome (opcional)
     * @param species filtro por espécie (opcional)
     * @param breed filtro por raça (opcional)
     * @param shelterCity filtro por cidade do abrigo (opcional)
     * @param status filtro por status (opcional)
     * @param pageable configuração de paginação e ordenação
     * @return resposta paginada com os pets encontrados
     */
    public PagedResponse<Pet> findPets(String name, Species species, String breed, 
                                      String shelterCity, Status status, Pageable pageable) {
        Specification<Pet> spec = Specification.where(null);
        
        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and(PetSpecifications.hasName(name));
        }
        
        if (species != null) {
            spec = spec.and(PetSpecifications.hasSpecies(species));
        }
        
        if (breed != null && !breed.trim().isEmpty()) {
            spec = spec.and(PetSpecifications.hasBreed(breed));
        }
        
        if (shelterCity != null && !shelterCity.trim().isEmpty()) {
            spec = spec.and(PetSpecifications.hasShelterCity(shelterCity));
        }
        
        if (status != null) {
            spec = spec.and(PetSpecifications.hasStatus(status));
        }
        
        Page<Pet> page = petRepository.findAll(spec, pageable);
        return PagedResponse.from(page);
    }

    /**
     * Busca um pet pelo ID.
     * @param id o ID do pet
     * @return o pet encontrado
     * @throws ResourceNotFoundException se o pet não for encontrado
     */
    public Pet getPetById(UUID id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Pet com ID '%s' não encontrado.", id)));
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
     * @return o pet atualizado
     * @throws ResourceNotFoundException se o pet não for encontrado
     */
    public Pet updatePet(UUID id, Pet updatedPet) {
        Pet existingPet = getPetById(id); // Usa o método que já lança exceção
        existingPet.setName(updatedPet.getName());
        existingPet.setSpecies(updatedPet.getSpecies());
        existingPet.setBreed(updatedPet.getBreed());
        existingPet.setAgeYears(updatedPet.getAgeYears());
        existingPet.setShelterCity(updatedPet.getShelterCity());
        existingPet.setShelterLat(updatedPet.getShelterLat());
        existingPet.setShelterLng(updatedPet.getShelterLng());
        existingPet.setStatus(updatedPet.getStatus());
        return petRepository.save(existingPet);
    }

    /**
     * Marca um pet como adotado.
     * @param id o ID do pet
     * @return true se o pet foi encontrado e atualizado, false se já estava adotado
     * @throws ResourceNotFoundException se o pet não for encontrado
     */
    public boolean adoptPet(UUID id) {
        Pet pet = getPetById(id); // Usa o método que já lança exceção
        if (pet.getStatus() == Status.AVAILABLE) {
            pet.setStatus(Status.ADOPTED);
            petRepository.save(pet);
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