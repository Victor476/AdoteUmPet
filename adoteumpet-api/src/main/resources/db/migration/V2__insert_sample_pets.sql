-- ================================================================================================
-- Migração V2: Inserir dados de exemplo na tabela pets
-- ================================================================================================
-- Descrição: Adiciona pets de exemplo para desenvolvimento e testes
-- Autor: Sistema AdoteUmPet
-- Data: 2025-09-12
-- ================================================================================================

-- Inserir pets de exemplo
INSERT INTO pets (
    id,
    name,
    species,
    breed,
    age_years,
    shelter_city,
    shelter_lat,
    shelter_lng,
    status,
    created_at
) VALUES
    -- Cachorros
    (
        gen_random_uuid(),
        'Rex',
        'DOG',
        'Pastor Alemão',
        3,
        'São Paulo',
        -23.5505200,
        -46.6333090,
        'AVAILABLE',
        CURRENT_TIMESTAMP
    ),
    (
        gen_random_uuid(),
        'Luna',
        'DOG',
        'Labrador',
        2,
        'Rio de Janeiro',
        -22.9068467,
        -43.1728965,
        'AVAILABLE',
        CURRENT_TIMESTAMP
    ),
    (
        gen_random_uuid(),
        'Buddy',
        'DOG',
        'Golden Retriever',
        5,
        'Belo Horizonte',
        -19.9166813,
        -43.9344931,
        'AVAILABLE',
        CURRENT_TIMESTAMP
    ),
    -- Gatos
    (
        gen_random_uuid(),
        'Mimi',
        'CAT',
        'Siamês',
        1,
        'São Paulo',
        -23.5505200,
        -46.6333090,
        'AVAILABLE',
        CURRENT_TIMESTAMP
    ),
    (
        gen_random_uuid(),
        'Garfield',
        'CAT',
        'Persa',
        4,
        'Curitiba',
        -25.4284000,
        -49.2733000,
        'ADOPTED',
        CURRENT_TIMESTAMP
    ),
    (
        gen_random_uuid(),
        'Whiskers',
        'CAT',
        'Vira-lata',
        2,
        'Porto Alegre',
        -30.0346000,
        -51.2177000,
        'AVAILABLE',
        CURRENT_TIMESTAMP
    );

-- Verificar se os dados foram inseridos corretamente
-- (Comentário para documentação - esta linha não será executada)
-- SELECT COUNT(*) FROM pets;
