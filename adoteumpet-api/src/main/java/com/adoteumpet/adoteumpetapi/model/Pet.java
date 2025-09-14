package com.adoteumpet.adoteumpetapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade JPA que representa um pet no sistema de adoção.
 * Contém todas as informações necessárias sobre o animal,
 * incluindo dados pessoais, localização do abrigo e status de adoção.
 */
@Entity
@Table(name = "pets")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidade que representa um pet no sistema")
public class Pet {

    /**
     * Identificador único do pet usando UUID para garantir unicidade global.
     */
    @Schema(description = "Identificador único do pet", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Nome do pet.
     */
    @Schema(description = "Nome do pet", example = "Rex")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Espécie do pet (cachorro, gato, etc.).
     */
    @Schema(description = "Espécie do pet", example = "DOG", allowableValues = {"DOG", "CAT"})
    @Enumerated(EnumType.STRING)
    @Column(name = "species", nullable = false)
    private Species species;

    /**
     * Raça do pet.
     */
    @Schema(description = "Raça do pet", example = "Golden Retriever")
    @Column(name = "breed", length = 100)
    private String breed;

    /**
     * Idade do pet em anos.
     */
    @Schema(description = "Idade do pet em anos", example = "3")
    @Column(name = "age_years")
    private Integer ageYears;

    /**
     * Cidade onde o abrigo está localizado.
     */
    @Schema(description = "Cidade onde o abrigo está localizado", example = "São Paulo")
    @Column(name = "shelter_city", nullable = false, length = 100)
    private String shelterCity;

    /**
     * Latitude da localização do abrigo.
     */
    @Schema(description = "Latitude da localização do abrigo", example = "-23.5505199")
    @Column(name = "shelter_lat", precision = 10, scale = 8)
    private BigDecimal shelterLat;

    /**
     * Longitude da localização do abrigo.
     */
    @Schema(description = "Longitude da localização do abrigo", example = "-46.6333094")
    @Column(name = "shelter_lng", precision = 11, scale = 8)
    private BigDecimal shelterLng;

    /**
     * Status atual do pet (disponível, adotado, etc.).
     */
    @Schema(description = "Status atual do pet", example = "AVAILABLE", allowableValues = {"AVAILABLE", "ADOPTED"})
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * Data e hora de criação do registro.
     * Automaticamente preenchido pelo Spring Data JPA.
     */
    @Schema(description = "Data e hora de criação do registro", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}