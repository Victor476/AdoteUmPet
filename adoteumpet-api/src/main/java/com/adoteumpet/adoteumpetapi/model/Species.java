package com.adoteumpet.adoteumpetapi.model;

/**
 * Enum que representa as espécies de pets disponíveis no sistema.
 */
public enum Species {
    DOG("Cachorro"),
    CAT("Gato"),
    BIRD("Pássaro"),
    RABBIT("Coelho"),
    FISH("Peixe"),
    HAMSTER("Hamster"),
    OTHER("Outro");

    private final String displayName;

    Species(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}