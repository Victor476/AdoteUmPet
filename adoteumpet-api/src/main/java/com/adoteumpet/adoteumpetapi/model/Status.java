package com.adoteumpet.adoteumpetapi.model;

/**
 * Enum que representa os status possíveis de um pet no sistema.
 */
public enum Status {
    AVAILABLE("Disponível para adoção"),
    ADOPTED("Adotado"),
    PENDING("Adoção pendente"),
    UNAVAILABLE("Indisponível");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}