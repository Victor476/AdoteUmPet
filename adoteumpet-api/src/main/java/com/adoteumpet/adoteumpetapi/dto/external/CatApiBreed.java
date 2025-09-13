package com.adoteumpet.adoteumpetapi.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para resposta da TheCatAPI
 * Estrutura simplificada da API: https://api.thecatapi.com/v1/breeds
 */
public class CatApiBreed {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("temperament")
    private String temperament;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("life_span")
    private String lifeSpan;

    @JsonProperty("description")
    private String description;

    @JsonProperty("reference_image_id")
    private String referenceImageId;

    // Campo específico que queremos - nível de energia (1-5)
    @JsonProperty("energy_level")
    private Integer energyLevel;

    // Constructors
    public CatApiBreed() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(String lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceImageId() {
        return referenceImageId;
    }

    public void setReferenceImageId(String referenceImageId) {
        this.referenceImageId = referenceImageId;
    }

    public Integer getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Integer energyLevel) {
        this.energyLevel = energyLevel;
    }
}