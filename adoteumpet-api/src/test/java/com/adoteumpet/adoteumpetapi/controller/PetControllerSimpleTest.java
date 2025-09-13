package com.adoteumpet.adoteumpetapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adoteumpet.adoteumpetapi.exception.ResourceNotFoundException;
import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.service.PetService;

/**
 * Testes unitários simples para o PetController.
 * Testa diretamente os métodos do controller sem usar Spring Boot Test.
 */
@ExtendWith(MockitoExtension.class)
class PetControllerSimpleTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    private Pet samplePet;
    private UUID petId;

    @BeforeEach
    void setUp() {
        petId = UUID.randomUUID();
        samplePet = new Pet();
        samplePet.setId(petId);
        samplePet.setName("Buddy");
        samplePet.setSpecies(Species.DOG);
        samplePet.setBreed("Golden Retriever");
        samplePet.setAgeYears(3);
        samplePet.setShelterCity("São Paulo");
        samplePet.setStatus(Status.AVAILABLE);
    }

    @Test
    void getPetById_WithValidId_ShouldReturnPet() {
        // Given
        when(petService.getPetById(petId)).thenReturn(samplePet);

        // When
        var response = petController.getPetById(petId);
        Pet result = response.getBody();

        // Then
        assertNotNull(result);
        assertEquals(petId, result.getId());
        assertEquals("Buddy", result.getName());
        assertEquals(Species.DOG, result.getSpecies());
        assertEquals("Golden Retriever", result.getBreed());
        assertEquals(3, result.getAgeYears());
        assertEquals("São Paulo", result.getShelterCity());
        assertEquals(Status.AVAILABLE, result.getStatus());
        
        verify(petService).getPetById(petId);
    }

    @Test
    void getPetById_WithNonExistentId_ShouldThrowResourceNotFoundException() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(petService.getPetById(nonExistentId))
            .thenThrow(new ResourceNotFoundException("Pet não encontrado com ID: " + nonExistentId));

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> petController.getPetById(nonExistentId)
        );
        
        assertTrue(exception.getMessage().contains("Pet não encontrado com ID: " + nonExistentId));
        verify(petService).getPetById(nonExistentId);
    }

    @Test
    void getPetById_WithCatPet_ShouldReturnCatData() {
        // Given
        Pet catPet = new Pet();
        catPet.setId(petId);
        catPet.setName("Whiskers");
        catPet.setSpecies(Species.CAT);
        catPet.setBreed("Persian");
        catPet.setAgeYears(2);
        catPet.setShelterCity("Rio de Janeiro");
        catPet.setStatus(Status.AVAILABLE);

        when(petService.getPetById(petId)).thenReturn(catPet);

        // When
        var response = petController.getPetById(petId);
        Pet result = response.getBody();

        // Then
        assertNotNull(result);
        assertEquals(petId, result.getId());
        assertEquals("Whiskers", result.getName());
        assertEquals(Species.CAT, result.getSpecies());
        assertEquals("Persian", result.getBreed());
        assertEquals(2, result.getAgeYears());
        assertEquals("Rio de Janeiro", result.getShelterCity());
        assertEquals(Status.AVAILABLE, result.getStatus());
        
        verify(petService).getPetById(petId);
    }

    @Test
    void getPetById_WithPetWithoutOptionalFields_ShouldReturnPetData() {
        // Given
        Pet simplePet = new Pet();
        simplePet.setId(petId);
        simplePet.setName("Rex");
        simplePet.setSpecies(Species.DOG);
        simplePet.setShelterCity("Belo Horizonte");
        simplePet.setStatus(Status.AVAILABLE);
        // breed, ageYears são null

        when(petService.getPetById(petId)).thenReturn(simplePet);

        // When
        var response = petController.getPetById(petId);
        Pet result = response.getBody();

        // Then
        assertNotNull(result);
        assertEquals(petId, result.getId());
        assertEquals("Rex", result.getName());
        assertEquals(Species.DOG, result.getSpecies());
        assertEquals("Belo Horizonte", result.getShelterCity());
        assertEquals(Status.AVAILABLE, result.getStatus());
        assertNull(result.getBreed());
        assertNull(result.getAgeYears());
        
        verify(petService).getPetById(petId);
    }

    @Test
    void getPetById_ServiceMethodCalled_VerifyInteraction() {
        // Given
        when(petService.getPetById(petId)).thenReturn(samplePet);

        // When
        petController.getPetById(petId);

        // Then
        verify(petService, times(1)).getPetById(petId);
        verifyNoMoreInteractions(petService);
    }
}