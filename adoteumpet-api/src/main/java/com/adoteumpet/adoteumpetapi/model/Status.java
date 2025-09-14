package com.adoteumpet.adoteumpetapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum que representa os status possíveis de um pet no sistema.
 * Apenas disponível para adoção e adotado são suportados.
 */
@Schema(description = "Status possíveis de um pet", allowableValues = {"AVAILABLE", "ADOPTED"})
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