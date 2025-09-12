package com.adoteumpet.adoteumpetapi.config;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

/**
 * Configuração para carregar dados de exemplo no banco de dados.
 * Executa apenas em perfis de desenvolvimento e teste.
 * Para usar dados reais, descomente a anotação @Profile ou remova esta classe.
 */
@Configuration
@Profile({"dev", "test"}) // Removido "default" para não executar por padrão
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(PetRepository repository) {
        return args -> {
            // Criando alguns pets de exemplo
            Pet pet1 = new Pet();
            pet1.setName("Rex");
            pet1.setSpecies(Species.DOG);
            pet1.setBreed("Labrador");
            pet1.setAgeYears(3);
            pet1.setShelterCity("São Paulo");
            pet1.setShelterLat(new BigDecimal("-23.5505199"));
            pet1.setShelterLng(new BigDecimal("-46.6333094"));
            pet1.setStatus(Status.AVAILABLE);

            Pet pet2 = new Pet();
            pet2.setName("Mimi");
            pet2.setSpecies(Species.CAT);
            pet2.setBreed("Siamês");
            pet2.setAgeYears(2);
            pet2.setShelterCity("Rio de Janeiro");
            pet2.setShelterLat(new BigDecimal("-22.9068467"));
            pet2.setShelterLng(new BigDecimal("-43.1728965"));
            pet2.setStatus(Status.AVAILABLE);

            Pet pet3 = new Pet();
            pet3.setName("Buddy");
            pet3.setSpecies(Species.DOG);
            pet3.setBreed("Golden Retriever");
            pet3.setAgeYears(5);
            pet3.setShelterCity("Belo Horizonte");
            pet3.setShelterLat(new BigDecimal("-19.9166813"));
            pet3.setShelterLng(new BigDecimal("-43.9344931"));
            pet3.setStatus(Status.ADOPTED);

            Pet pet4 = new Pet();
            pet4.setName("Luna");
            pet4.setSpecies(Species.CAT);
            pet4.setBreed("Persa");
            pet4.setAgeYears(1);
            pet4.setShelterCity("Porto Alegre");
            pet4.setShelterLat(new BigDecimal("-30.0346471"));
            pet4.setShelterLng(new BigDecimal("-51.2176584"));
            pet4.setStatus(Status.AVAILABLE);

            Pet pet5 = new Pet();
            pet5.setName("Max");
            pet5.setSpecies(Species.DOG);
            pet5.setBreed("Beagle");
            pet5.setAgeYears(4);
            pet5.setShelterCity("Curitiba");
            pet5.setShelterLat(new BigDecimal("-25.4284700"));
            pet5.setShelterLng(new BigDecimal("-49.2733200"));
            pet5.setStatus(Status.AVAILABLE);

            // Salvando no banco
            repository.save(pet1);
            repository.save(pet2);
            repository.save(pet3);
            repository.save(pet4);
            repository.save(pet5);

            System.out.println("Base de dados inicializada com pets de exemplo:");
            repository.findAll().forEach(pet -> 
                System.out.println("- " + pet.getName() + " (" + pet.getSpecies() + ") - " + pet.getStatus())
            );
        };
    }
}