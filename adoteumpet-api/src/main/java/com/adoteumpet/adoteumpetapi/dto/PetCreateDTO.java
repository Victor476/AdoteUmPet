package com.adoteumpet.adoteumpetapi.dto;

import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para criação de pets.
 * Contém validações para garantir a integridade dos dados recebidos na API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetCreateDTO {

    /**
     * Nome do pet.
     * Campo obrigatório, não pode estar em branco.
     */
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String name;

    /**
     * Espécie do pet.
     * Campo obrigatório.
     */
    @NotNull(message = "Espécie é obrigatória")
    private Species species;

    /**
     * Raça do pet.
     * Campo opcional.
     */
    @Size(max = 100, message = "Raça deve ter no máximo 100 caracteres")
    private String breed;

    /**
     * Idade do pet em anos.
     * Deve ser maior ou igual a 0 e menor que 30 anos.
     */
    @Min(value = 0, message = "Idade deve ser maior ou igual a 0")
    @Max(value = 30, message = "Idade deve ser menor que 30 anos")
    private Integer ageYears;

    /**
     * Cidade onde o abrigo está localizado.
     * Campo obrigatório.
     */
    @NotBlank(message = "Cidade do abrigo é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String shelterCity;

    /**
     * Latitude da localização do abrigo.
     * Deve estar entre -90 e 90 graus.
     */
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90 graus")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90 graus")
    @Digits(integer = 3, fraction = 8, message = "Latitude deve ter no máximo 3 dígitos inteiros e 8 decimais")
    private BigDecimal shelterLat;

    /**
     * Longitude da localização do abrigo.
     * Deve estar entre -180 e 180 graus.
     */
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180 graus")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180 graus")
    @Digits(integer = 3, fraction = 8, message = "Longitude deve ter no máximo 3 dígitos inteiros e 8 decimais")
    private BigDecimal shelterLng;

    /**
     * Status atual do pet.
     * Se não informado, será definido como AVAILABLE por padrão.
     */
    private Status status;
}