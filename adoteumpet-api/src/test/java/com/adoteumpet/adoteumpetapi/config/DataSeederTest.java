package com.adoteumpet.adoteumpetapi.config;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para verificar o funcionamento do sistema de seeding.
 * Verifica se os dados são carregados corretamente e se o sistema é idempotente.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DataSeederTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private DataSeeder dataSeeder;

    @BeforeEach
    void setUp() {
        // Limpar o banco antes de cada teste
        petRepository.deleteAll();
    }

    @Test
    void shouldLoadPetsFromCsvOnFirstRun() throws Exception {
        // Arrange - banco vazio
        assertThat(petRepository.count()).isZero();

        // Act - executar seeding
        dataSeeder.run();

        // Assert - verificar se pets foram carregados
        List<Pet> pets = petRepository.findAll();
        
        // Deve ter 15 pets do CSV + 1 marcador = 16 total
        assertThat(pets).hasSize(16);
        
        // Verificar se alguns pets específicos foram carregados
        assertThat(pets.stream().anyMatch(p -> "Rex".equals(p.getName()))).isTrue();
        assertThat(pets.stream().anyMatch(p -> "Luna".equals(p.getName()))).isTrue();
        assertThat(pets.stream().anyMatch(p -> "Mimi".equals(p.getName()))).isTrue();
        
        // Verificar se o marcador foi criado
        assertThat(pets.stream().anyMatch(p -> "SEED_MARKER_PET".equals(p.getName()))).isTrue();
    }

    @Test
    void shouldBeIdempotent() throws Exception {
        // Arrange - executar seeding uma primeira vez
        dataSeeder.run();
        long firstRunCount = petRepository.count();
        assertThat(firstRunCount).isEqualTo(16); // 15 pets + 1 marcador

        // Act - executar seeding novamente
        dataSeeder.run();

        // Assert - não deve ter adicionado novos pets
        long secondRunCount = petRepository.count();
        assertThat(secondRunCount).isEqualTo(firstRunCount);
        assertThat(secondRunCount).isEqualTo(16);
    }

    @Test
    void shouldNotDuplicatePetsOnMultipleRuns() throws Exception {
        // Arrange & Act - executar seeding 3 vezes
        dataSeeder.run();
        dataSeeder.run();
        dataSeeder.run();

        // Assert - deve ter apenas 16 pets (15 + marcador)
        assertThat(petRepository.count()).isEqualTo(16);
        
        // Verificar que não há pets duplicados
        List<Pet> pets = petRepository.findAll();
        List<String> petNames = pets.stream()
                .map(Pet::getName)
                .filter(name -> !"SEED_MARKER_PET".equals(name))
                .toList();
        
        // Não deve haver nomes duplicados
        assertThat(petNames).hasSize(15);
        assertThat(petNames.stream().distinct().count()).isEqualTo(15);
    }

    @Test
    void shouldLoadCorrectSpeciesAndStatus() throws Exception {
        // Act
        dataSeeder.run();

        // Assert - verificar se carregou dogs e cats
        List<Pet> pets = petRepository.findAll()
                .stream()
                .filter(p -> !"SEED_MARKER_PET".equals(p.getName()))
                .toList();

        long dogCount = pets.stream()
                .filter(p -> "DOG".equals(p.getSpecies().name()))
                .count();
        
        long catCount = pets.stream()
                .filter(p -> "CAT".equals(p.getSpecies().name()))
                .count();

        assertThat(dogCount).isGreaterThan(0);
        assertThat(catCount).isGreaterThan(0);
        assertThat(dogCount + catCount).isEqualTo(15);

        // Verificar que há pets com status AVAILABLE e ADOPTED
        boolean hasAvailable = pets.stream()
                .anyMatch(p -> "AVAILABLE".equals(p.getStatus().name()));
        boolean hasAdopted = pets.stream()
                .anyMatch(p -> "ADOPTED".equals(p.getStatus().name()));

        assertThat(hasAvailable).isTrue();
        assertThat(hasAdopted).isTrue();
    }
}