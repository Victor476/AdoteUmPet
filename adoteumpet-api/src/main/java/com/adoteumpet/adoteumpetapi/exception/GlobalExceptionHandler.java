package com.adoteumpet.adoteumpetapi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de exceções para a API.
 * Trata erros de validação e outros erros da aplicação.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata erros de validação de campos.
     * @param ex exceção de validação
     * @return resposta com status 400 e lista de erros
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Erro de validação");
        response.put("message", "Dados inválidos fornecidos");
        response.put("errors", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata erros de formato JSON inválido e valores de enum inválidos.
     * @param ex exceção de mensagem não legível
     * @return resposta com status 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        
        Map<String, Object> response = new HashMap<>();
        String message = "Formato JSON inválido ou valores inválidos";
        
        // Verifica se é um erro de enum inválido
        if (ex.getCause() instanceof InvalidFormatException invalidFormatEx) {
            if (invalidFormatEx.getTargetType().isEnum()) {
                String fieldName = invalidFormatEx.getPath().get(0).getFieldName();
                Object[] enumValues = invalidFormatEx.getTargetType().getEnumConstants();
                StringBuilder validValues = new StringBuilder();
                
                for (int i = 0; i < enumValues.length; i++) {
                    validValues.append(enumValues[i]);
                    if (i < enumValues.length - 1) {
                        validValues.append(", ");
                    }
                }
                
                message = String.format("Valor inválido para o campo '%s'. Valores aceitos: %s", 
                    fieldName, validValues.toString());
            }
        }
        
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Dados inválidos");
        response.put("message", message);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata erros gerais não específicos.
     * @param ex exceção geral
     * @return resposta com status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Erro interno do servidor");
        response.put("message", "Ocorreu um erro inesperado");
        
        // Log do erro para depuração (em produção, usar um logger)
        System.err.println("Erro não tratado: " + ex.getMessage());
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}