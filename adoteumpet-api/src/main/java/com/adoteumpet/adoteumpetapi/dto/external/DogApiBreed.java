package com.adoteumpet.adoteumpetapi.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para resposta da TheDogAPI
 * Estrutura simplificada da API: https://api.thedogapi.com/v1/breeds
 */
public class DogApiBreed {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("temperament")
    private String temperament;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("life_span")
    private String lifeSpan;

    @JsonProperty("bred_for")
    private String bredFor;

    @JsonProperty("breed_group")
    private String breedGroup;

    @JsonProperty("reference_image_id")
    private String referenceImageId;

    // Constructors
    public DogApiBreed() {}

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getBredFor() {
        return bredFor;
    }

    public void setBredFor(String bredFor) {
        this.bredFor = bredFor;
    }

    public String getBreedGroup() {
        return breedGroup;
    }

    public void setBreedGroup(String breedGroup) {
        this.breedGroup = breedGroup;
    }

    public String getReferenceImageId() {
        return referenceImageId;
    }

    public void setReferenceImageId(String referenceImageId) {
        this.referenceImageId = referenceImageId;
    }
}