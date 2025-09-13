package com.adoteumpet.adoteumpetapi.config;

import com.adoteumpet.adoteumpetapi.model.Pet;
import com.adoteumpet.adoteumpetapi.model.Species;
import com.adoteumpet.adoteumpetapi.model.Status;
import com.adoteumpet.adoteumpetapi.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Classe responsável por popular o banco de dados com dados iniciais de pets.
 * 
 * Esta classe implementa CommandLineRunner para ser executada automaticamente
 * na inicialização da aplicação Spring Boot. O seeding é idempotente, ou seja,
 * pode ser executado múltiplas vezes sem criar dados duplicados.
 * 
 * Os dados são carregados a partir do arquivo CSV localizado em:
 * src/main/resources/data/pets-seed.csv
 * 
 * @author Sistema AdoteUmPet
 * @since 1.0
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    private static final String CSV_FILE_PATH = "pets-seed.csv";
    private static final String SEED_MARKER_NAME = "SEED_MARKER_PET";

    @Autowired
    private PetRepository petRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("🌱 Iniciando processo de seeding do banco de dados...");
        
        if (isSeedingAlreadyExecuted()) {
            logger.info("✅ Seeding já foi executado anteriormente. Pulando para evitar duplicação.");
            return;
        }

        try {
            int petsLoaded = loadPetsFromCsv();
            createSeedMarker();
            logger.info("🎉 Seeding concluído com sucesso! {} pets foram inseridos no banco de dados.", petsLoaded);
        } catch (Exception e) {
            logger.error("❌ Erro durante o processo de seeding: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Verifica se o seeding já foi executado anteriormente.
     * 
     * A verificação é feita procurando por um pet com nome específico que serve
     * como marcador de que o seeding já foi executado.
     * 
     * @return true se o seeding já foi executado, false caso contrário
     */
    private boolean isSeedingAlreadyExecuted() {
        return petRepository.findByName(SEED_MARKER_NAME).size() > 0;
    }

    /**
     * Cria um pet marcador para indicar que o seeding foi executado.
     * 
     * Este pet não é visível para os usuários finais e serve apenas como
     * controle interno para garantir idempotência.
     */
    private void createSeedMarker() {
        Pet marker = new Pet();
        marker.setName(SEED_MARKER_NAME);
        marker.setSpecies(Species.DOG);
        marker.setBreed("Sistema");
        marker.setAgeYears(0);
        marker.setShelterCity("Sistema");
        marker.setShelterLat(BigDecimal.ZERO);
        marker.setShelterLng(BigDecimal.ZERO);
        marker.setStatus(Status.ADOPTED); // ADOPTED para não aparecer nas buscas de pets disponíveis
        marker.setCreatedAt(LocalDateTime.now());
        
        petRepository.save(marker);
        logger.debug("🏷️ Marcador de seeding criado com sucesso.");
    }

    /**
     * Carrega os pets a partir do arquivo CSV.
     * 
     * @return número de pets carregados
     * @throws IOException se houver erro na leitura do arquivo
     */
    private int loadPetsFromCsv() throws IOException {
        ClassPathResource resource = new ClassPathResource(CSV_FILE_PATH);
        
        if (!resource.exists()) {
            logger.warn("⚠️ Arquivo CSV não encontrado: {}. Seeding não será executado.", CSV_FILE_PATH);
            return 0;
        }

        int petsLoaded = 0;
        logger.info("📂 Carregando pets do arquivo: {}", CSV_FILE_PATH);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Pular cabeçalho
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                try {
                    Pet pet = parsePetFromCsvLine(line);
                    petRepository.save(pet);
                    petsLoaded++;
                    logger.debug("🐾 Pet salvo: {} ({})", pet.getName(), pet.getSpecies());
                } catch (Exception e) {
                    logger.error("❌ Erro ao processar linha do CSV: '{}'. Erro: {}", line, e.getMessage());
                    // Continua processando outras linhas mesmo se uma falhar
                }
            }
        }

        return petsLoaded;
    }

    /**
     * Converte uma linha do CSV em um objeto Pet.
     * 
     * Formato esperado: name,species,breed,age_years,shelter_city,shelter_lat,shelter_lng,status
     * 
     * @param csvLine linha do CSV para processar
     * @return objeto Pet criado a partir da linha
     * @throws IllegalArgumentException se a linha estiver mal formatada
     */
    private Pet parsePetFromCsvLine(String csvLine) {
        String[] fields = csvLine.split(",");
        
        if (fields.length != 8) {
            throw new IllegalArgumentException("Linha CSV deve ter exatamente 8 campos, mas tem " + fields.length);
        }

        Pet pet = new Pet();
        pet.setName(fields[0].trim());
        pet.setSpecies(Species.valueOf(fields[1].trim()));
        pet.setBreed(fields[2].trim().isEmpty() ? null : fields[2].trim());
        
        // Age pode ser vazio
        String ageStr = fields[3].trim();
        pet.setAgeYears(ageStr.isEmpty() ? null : Integer.parseInt(ageStr));
        
        pet.setShelterCity(fields[4].trim());
        
        // Coordenadas podem ser vazias
        String latStr = fields[5].trim();
        pet.setShelterLat(latStr.isEmpty() ? null : new BigDecimal(latStr));
        
        String lngStr = fields[6].trim();
        pet.setShelterLng(lngStr.isEmpty() ? null : new BigDecimal(lngStr));
        
        pet.setStatus(Status.valueOf(fields[7].trim()));
        pet.setCreatedAt(LocalDateTime.now());

        return pet;
    }
}