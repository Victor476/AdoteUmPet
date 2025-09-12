-- ================================================================================================
-- Migração V1: Criação da tabela pets
-- ================================================================================================
-- Descrição: Cria a tabela principal para armazenar informações dos pets disponíveis para adoção
-- Autor: Sistema AdoteUmPet
-- Data: 2025-09-12
-- ================================================================================================

-- Criação da tabela pets com todas as colunas e constraints necessárias
CREATE TABLE pets (
    -- Identificador único usando UUID
    id UUID PRIMARY KEY,
    
    -- Informações básicas do pet
    name VARCHAR(100) NOT NULL,
    species VARCHAR(255) NOT NULL CHECK (species IN ('DOG', 'CAT')),
    breed VARCHAR(100),
    age_years INTEGER,
    
    -- Localização do abrigo
    shelter_city VARCHAR(100) NOT NULL,
    shelter_lat DECIMAL(10,8),
    shelter_lng DECIMAL(11,8),
    
    -- Status do pet
    status VARCHAR(255) NOT NULL CHECK (status IN ('AVAILABLE', 'ADOPTED')),
    
    -- Auditoria
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhorar performance das consultas mais comuns
CREATE INDEX idx_pets_species ON pets(species);
CREATE INDEX idx_pets_status ON pets(status);
CREATE INDEX idx_pets_shelter_city ON pets(shelter_city);
CREATE INDEX idx_pets_created_at ON pets(created_at);

-- Comentários nas colunas para documentação
COMMENT ON TABLE pets IS 'Tabela principal para armazenar informações dos pets disponíveis para adoção';
COMMENT ON COLUMN pets.id IS 'Identificador único do pet (UUID)';
COMMENT ON COLUMN pets.name IS 'Nome do pet';
COMMENT ON COLUMN pets.species IS 'Espécie do pet (DOG ou CAT)';
COMMENT ON COLUMN pets.breed IS 'Raça do pet (opcional)';
COMMENT ON COLUMN pets.age_years IS 'Idade do pet em anos (opcional)';
COMMENT ON COLUMN pets.shelter_city IS 'Cidade onde o abrigo está localizado';
COMMENT ON COLUMN pets.shelter_lat IS 'Latitude da localização do abrigo';
COMMENT ON COLUMN pets.shelter_lng IS 'Longitude da localização do abrigo';
COMMENT ON COLUMN pets.status IS 'Status atual do pet (AVAILABLE ou ADOPTED)';
COMMENT ON COLUMN pets.created_at IS 'Data e hora de criação do registro';
