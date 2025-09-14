package com.adoteumpet.adoteumpetapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO padronizado para resposta de raças de animais
 * Para gatos: energy_level será 1-5 (da TheCatAPI)
 * Para cães: energy_level será null (omitido do JSON)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Informações sobre uma raça de animal")
public class BreedResponse {

    @Schema(description = "Nome da raça", example = "Golden Retriever")
    @JsonProperty("name")
    private String name;

    @Schema(description = "País de origem da raça", example = "Scotland")
    @JsonProperty("origin")
    private String origin;

    @Schema(description = "Temperamento da raça", example = "Friendly, Intelligent, Devoted")
    @JsonProperty("temperament")
    private String temperament;

    @Schema(description = "Nível de energia da raça (1-5, apenas para gatos)", example = "3", minimum = "1", maximum = "5")
    @JsonProperty("energy_level")
    private Integer energyLevel;

    @Schema(description = "URL da imagem da raça", example = "https://cdn2.thedogapi.com/images/BJa4kxc4X.jpg")
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