package com.adoteumpet.adoteumpetapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum que representa as espécies de pets disponíveis no sistema.
 * Apenas gatos e cachorros são suportados.
 */
@Schema(description = "Espécies de pets disponíveis", allowableValues = {"DOG", "CAT"})
public enum Species {
    DOG("Cachorro"),
    CAT("Gato");

    private final String displayName;

    Species(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}