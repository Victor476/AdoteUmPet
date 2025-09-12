package com.adoteumpet.adoteumpetapi.model;

/**
 * Enum que representa os status possíveis de um pet no sistema.
 * Apenas disponível para adoção e adotado são suportados.
 */
public enum Status {
    AVAILABLE("Disponível para adoção"),
    ADOPTED("Adotado");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}