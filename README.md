# AdoteUmPet
PetHub desafio técnico para estágio da Appmoove. PetHub é uma aplicação full-stack para adoção de pets, utilizando Spring Boot, Next.js e PostgreSQL. A plataforma permite gerenciar pets, aplicar filtros, e integrar dados de raças de cães e gatos de APIs externas. O objetivo é demonstrar proficiência em desenvolvimento e boas práticas.

## 🚀 Tecnologias Utilizadas

- **Backend**: Spring Boot 3.5.5 (Java 21)
- **Frontend**: Next.js 15.5.3 (TypeScript)
- **Banco de Dados**: PostgreSQL 15
- **Migrações**: Flyway
- **Containerização**: Docker & Docker Compose
- **ORM**: JPA/Hibernate

## 🐳 Execução com Docker

### Pré-requisitos
- Docker
- Docker Compose

### Como executar
```bash
# Clonar o repositório
git clone https://github.com/Victor476/AdoteUmPet.git
cd AdoteUmPet

# Executar a aplicação completa
docker compose up -d
```

### Serviços disponíveis
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8090
- **Banco PostgreSQL**: localhost:5433

## 🗃️ Gerenciamento de Banco de Dados com Flyway

### Sobre o Flyway
O projeto utiliza **Flyway** para gerenciamento de migrações do banco de dados, garantindo:
- ✅ Versionamento controlado do esquema
- ✅ Execução automática das migrações
- ✅ Idempotência (migrações não são executadas duas vezes)
- ✅ Histórico completo de mudanças

### Estrutura das Migrações
As migrações estão localizadas em:
```
adoteumpet-api/src/main/resources/db/migration/
├── V1__create_pets_table.sql     # Criação da tabela pets
└── V2__insert_sample_pets.sql    # Dados de exemplo
```

### Como funcionam as migrações

1. **Execução automática**: As migrações são executadas automaticamente na inicialização da aplicação
2. **Controle de versão**: Cada migração tem um número de versão sequencial (V1, V2, V3...)
3. **Tabela de controle**: O Flyway cria uma tabela `flyway_schema_history` para rastrear migrações executadas

### Verificar status das migrações
```bash
# Conectar ao banco para verificar migrações executadas
docker exec -it adoteumpet-postgres psql -U postgres -d adoteumpet_bd

# Ver histórico de migrações
SELECT * FROM flyway_schema_history;

# Ver tabelas criadas
\dt

# Ver estrutura da tabela pets
\d pets
```

### Criar nova migração
Para adicionar uma nova migração:

1. Crie um novo arquivo em `db/migration/` seguindo o padrão:
   ```
   V{número}__{descrição}.sql
   ```
   Exemplo: `V3__add_shelters_table.sql`

2. Reinicie a aplicação para que a migração seja executada:
   ```bash
   docker compose restart backend
   ```

### Exemplo de migração
```sql
-- V3__add_age_group_column.sql
ALTER TABLE pets ADD COLUMN age_group VARCHAR(20);
UPDATE pets SET age_group = CASE 
    WHEN age_years <= 1 THEN 'PUPPY'
    WHEN age_years <= 7 THEN 'ADULT'
    ELSE 'SENIOR'
END;
```

### Logs das migrações
Para acompanhar a execução das migrações:
```bash
# Ver logs do backend
docker compose logs backend

# Filtrar apenas logs do Flyway
docker compose logs backend | grep -i flyway
```

## 📋 Estrutura do Banco de Dados

### Tabela `pets`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID | Identificador único |
| `name` | VARCHAR(100) | Nome do pet |
| `species` | VARCHAR(255) | Espécie (DOG, CAT) |
| `breed` | VARCHAR(100) | Raça (opcional) |
| `age_years` | INTEGER | Idade em anos |
| `shelter_city` | VARCHAR(100) | Cidade do abrigo |
| `shelter_lat` | NUMERIC(10,8) | Latitude do abrigo |
| `shelter_lng` | NUMERIC(11,8) | Longitude do abrigo |
| `status` | VARCHAR(255) | Status (AVAILABLE, ADOPTED) |
| `created_at` | TIMESTAMP | Data de criação |

### Índices criados
- `idx_pets_species` - Otimiza buscas por espécie
- `idx_pets_status` - Otimiza buscas por status
- `idx_pets_shelter_city` - Otimiza buscas por cidade
- `idx_pets_created_at` - Otimiza ordenação por data
