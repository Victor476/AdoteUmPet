package com.adoteumpet.adoteumpetapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    /**
     * Identificador único do pet usando UUID para garantir unicidade global.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Nome do pet.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Espécie do pet (cachorro, gato, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "species", nullable = false)
    private Species species;

    /**
     * Raça do pet.
     */
    @Column(name = "breed", length = 100)
    private String breed;

    /**
     * Idade do pet em anos.
     */
    @Column(name = "age_years")
    private Integer ageYears;

    /**
     * Cidade onde o abrigo está localizado.
     */
    @Column(name = "shelter_city", nullable = false, length = 100)
    private String shelterCity;

    /**
     * Latitude da localização do abrigo.
     */
    @Column(name = "shelter_lat", precision = 10, scale = 8)
    private BigDecimal shelterLat;

    /**
     * Longitude da localização do abrigo.
     */
    @Column(name = "shelter_lng", precision = 11, scale = 8)
    private BigDecimal shelterLng;

    /**
     * Status atual do pet (disponível, adotado, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * Data e hora de criação do registro.
     * Automaticamente preenchido pelo Hibernate.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}