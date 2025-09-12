package com.adoteumpet.adoteumpetapi.repository;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositório JPA para gerenciar operações de banco de dados da entidade Pet.
 * Fornece métodos básicos CRUD e consultas customizadas.
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    /**
     * Busca pets por espécie.
     * @param species a espécie do pet
     * @return lista de pets da espécie especificada
     */
    List<Pet> findBySpecies(Species species);

    /**
     * Busca pets por status.
     * @param status o status do pet
     * @return lista de pets com o status especificado
     */
    List<Pet> findByStatus(Status status);

    /**
     * Busca pets por cidade do abrigo.
     * @param shelterCity a cidade do abrigo
     * @return lista de pets na cidade especificada
     */
    List<Pet> findByShelterCity(String shelterCity);

    /**
     * Busca pets disponíveis para adoção.
     * @return lista de pets disponíveis
     */
    @Query("SELECT p FROM Pet p WHERE p.status = 'AVAILABLE'")
    List<Pet> findAvailablePets();

    /**
     * Busca pets por espécie e status.
     * @param species a espécie do pet
     * @param status o status do pet
     * @return lista de pets que atendem aos critérios
     */
    List<Pet> findBySpeciesAndStatus(Species species, Status status);

    /**
     * Busca pets por cidade e status.
     * @param shelterCity a cidade do abrigo
     * @param status o status do pet
     * @return lista de pets que atendem aos critérios
     */
    List<Pet> findByShelterCityAndStatus(String shelterCity, Status status);

    /**
     * Busca pets por nome.
     * @param name o nome do pet
     * @return lista de pets com o nome especificado
     */
    List<Pet> findByName(String name);

    /**
     * Busca pets por faixa etária.
     * @param minAge idade mínima
     * @param maxAge idade máxima
     * @return lista de pets na faixa etária especificada
     */
    @Query("SELECT p FROM Pet p WHERE p.ageYears BETWEEN :minAge AND :maxAge")
    List<Pet> findByAgeBetween(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
}