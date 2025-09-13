package com.adoteumpet.adoteumpetapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO padronizado para resposta de raças de animais
 * Para gatos: energy_level será 1-5 (da TheCatAPI)
 * Para cães: energy_level será null (omitido do JSON)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BreedResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("temperament")
    private String temperament;

    @JsonProperty("energy_level")
    private Integer energyLevel;

    @JsonProperty("image_url")
    private String imageUrl;

    // Constructors
    public BreedResponse() {}

    public BreedResponse(String name, String origin, String temperament, Integer energyLevel, String imageUrl) {
        this.name = name;
        this.origin = origin;
        this.temperament = temperament;
        this.energyLevel = energyLevel;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public Integer getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Integer energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "BreedResponse{" +
                "name='" + name + '\'' +
                ", origin='" + origin + '\'' +
                ", temperament='" + temperament + '\'' +
                ", energyLevel=" + energyLevel +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}