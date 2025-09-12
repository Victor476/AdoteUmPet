package com.adoteumpet.adoteumpetapi.model;

/**
 * Enum que representa as espécies de pets disponíveis no sistema.
 * Apenas gatos e cachorros são suportados.
 */
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