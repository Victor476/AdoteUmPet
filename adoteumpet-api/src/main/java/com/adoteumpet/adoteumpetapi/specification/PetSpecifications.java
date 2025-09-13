package com.adoteumpet.adoteumpetapi.specification;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications para filtros dinâmicos de Pet.
 * Utiliza JPA Criteria API para construir consultas dinâmicas baseadas nos filtros fornecidos.
 */
public class PetSpecifications {

    /**
     * Filtra pets por nome (busca parcial, case-insensitive).
     * 
     * @param name o nome ou parte do nome do pet
     * @return specification para filtro por nome
     */
    public static Specification<Pet> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")), 
                "%" + name.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filtra pets por espécie.
     * 
     * @param species a espécie do pet
     * @return specification para filtro por espécie
     */
    public static Specification<Pet> hasSpecies(Species species) {
        return (root, query, criteriaBuilder) -> {
            if (species == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("species"), species);
        };
    }

    /**
     * Filtra pets por espécie usando string.
     * 
     * @param speciesStr a espécie do pet como string
     * @return specification para filtro por espécie
     */
    public static Specification<Pet> hasSpecies(String speciesStr) {
        return (root, query, criteriaBuilder) -> {
            if (speciesStr == null || speciesStr.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            try {
                Species species = Species.valueOf(speciesStr.toUpperCase());
                return criteriaBuilder.equal(root.get("species"), species);
            } catch (IllegalArgumentException e) {
                // Se a espécie não for válida, retorna uma condição que nunca é verdadeira
                return criteriaBuilder.disjunction();
            }
        };
    }

    /**
     * Filtra pets por raça (busca parcial, case-insensitive).
     * 
     * @param breed a raça ou parte da raça do pet
     * @return specification para filtro por raça
     */
    public static Specification<Pet> hasBreed(String breed) {
        return (root, query, criteriaBuilder) -> {
            if (breed == null || breed.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("breed")), 
                "%" + breed.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filtra pets por cidade do abrigo (busca parcial, case-insensitive).
     * 
     * @param shelterCity a cidade ou parte da cidade do abrigo
     * @return specification para filtro por cidade do abrigo
     */
    public static Specification<Pet> hasShelterCity(String shelterCity) {
        return (root, query, criteriaBuilder) -> {
            if (shelterCity == null || shelterCity.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("shelterCity")), 
                "%" + shelterCity.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filtra pets por status.
     * 
     * @param status o status do pet
     * @return specification para filtro por status
     */
    public static Specification<Pet> hasStatus(Status status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    /**
     * Filtra pets por status usando string.
     * 
     * @param statusStr o status do pet como string
     * @return specification para filtro por status
     */
    public static Specification<Pet> hasStatus(String statusStr) {
        return (root, query, criteriaBuilder) -> {
            if (statusStr == null || statusStr.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            try {
                Status status = Status.valueOf(statusStr.toUpperCase());
                return criteriaBuilder.equal(root.get("status"), status);
            } catch (IllegalArgumentException e) {
                // Se o status não for válido, retorna uma condição que nunca é verdadeira
                return criteriaBuilder.disjunction();
            }
        };
    }

    /**
     * Combina múltiplas specifications com operador AND.
     * 
     * @param name filtro por nome
     * @param species filtro por espécie
     * @param breed filtro por raça
     * @param shelterCity filtro por cidade do abrigo
     * @param status filtro por status
     * @return specification combinada
     */
    public static Specification<Pet> withFilters(String name, String species, String breed, 
                                                String shelterCity, String status) {
        return Specification.where(hasName(name))
                .and(hasSpecies(species))
                .and(hasBreed(breed))
                .and(hasShelterCity(shelterCity))
                .and(hasStatus(status));
    }
}