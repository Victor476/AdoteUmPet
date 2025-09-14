package com.adoteumpet.adoteumpetapi.dto;

import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Dados para criação de um novo pet")
public class PetCreateDTO {

    /**
     * Nome do pet.
     * Campo obrigatório, não pode estar em branco.
     */
    @Schema(description = "Nome do pet", example = "Rex", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @NotBlank(message = "O nome não pode ser vazio.")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String name;

    /**
     * Espécie do pet.
     * Campo obrigatório.
     */
    @Schema(description = "Espécie do pet", example = "DOG", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"DOG", "CAT"})
    @NotNull(message = "Espécie inválida. Valores válidos são 'dog' ou 'cat'.")
    private Species species;

    /**
     * Raça do pet.
     * Campo opcional.
     */
    @Schema(description = "Raça do pet", example = "Golden Retriever", maxLength = 100)
    @Size(max = 100, message = "Raça deve ter no máximo 100 caracteres")
    private String breed;

    /**
     * Idade do pet em anos.
     * Deve ser maior ou igual a 0 e menor que 30 anos.
     */
    @Schema(description = "Idade do pet em anos", example = "3", minimum = "0", maximum = "30")
    @Min(value = 0, message = "A idade deve ser um número positivo.")
    @Max(value = 30, message = "Idade deve ser menor que 30 anos")
    private Integer ageYears;

    /**
     * Cidade onde o abrigo está localizado.
     * Campo obrigatório.
     */
    @Schema(description = "Cidade onde o abrigo está localizado", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @NotBlank(message = "Cidade do abrigo é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String shelterCity;

    /**
     * Latitude da localização do abrigo.
     * Deve estar entre -90 e 90 graus.
     */
    @Schema(description = "Latitude da localização do abrigo", example = "-23.5505199", minimum = "-90.0", maximum = "90.0")
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90 graus")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90 graus")
    @Digits(integer = 3, fraction = 8, message = "Latitude deve ter no máximo 3 dígitos inteiros e 8 decimais")
    private BigDecimal shelterLat;

    /**
     * Longitude da localização do abrigo.
     * Deve estar entre -180 e 180 graus.
     */
    @Schema(description = "Longitude da localização do abrigo", example = "-46.6333094", minimum = "-180.0", maximum = "180.0")
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180 graus")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180 graus")
    @Digits(integer = 3, fraction = 8, message = "Longitude deve ter no máximo 3 dígitos inteiros e 8 decimais")
    private BigDecimal shelterLng;

    /**
     * Status atual do pet.
     * Se não informado, será definido como AVAILABLE por padrão.
     */
    @Schema(description = "Status atual do pet", example = "AVAILABLE", allowableValues = {"AVAILABLE", "ADOPTED"})
    private Status status;
}