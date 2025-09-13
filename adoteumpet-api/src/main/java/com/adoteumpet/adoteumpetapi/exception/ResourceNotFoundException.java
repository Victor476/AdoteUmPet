package com.adoteumpet.adoteumpetapi.exception;

/**
 * Exceção customizada para quando um recurso não é encontrado.
 * Utilizada principalmente para casos onde um pet específico não é encontrado no banco de dados.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construtor com mensagem personalizada.
     * @param message a mensagem de erro descritiva
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Construtor com mensagem e causa.
     * @param message a mensagem de erro descritiva
     * @param cause a causa da exceção
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}