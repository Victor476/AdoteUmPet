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

## 🌱 Sistema de Seeding (Dados Iniciais)

### Sobre o Sistema de Seeding
O projeto conta com um sistema automatizado de **seeding** que popula o banco de dados com dados de exemplo na primeira execução da aplicação. O sistema é **idempotente**, garantindo que os dados não sejam duplicados em execuções subsequentes.

### Características do Sistema
- ✅ **Execução automática**: Roda automaticamente na inicialização da aplicação
- ✅ **Idempotente**: Pode ser executado múltiplas vezes sem criar dados duplicados  
- ✅ **Flexível**: Carrega dados a partir de arquivo CSV facilmente editável
- ✅ **Robusto**: Trata erros individualmente, continuando o processamento mesmo se algumas linhas falharem
- ✅ **Auditável**: Gera logs detalhados do processo de seeding

### Arquivos do Sistema
```
adoteumpet-api/src/main/resources/
├── data/
│   └── pets-seed.csv                    # Dados dos pets para seeding
└── java/.../config/
    └── DataSeeder.java                  # Classe que executa o seeding
```

### Estrutura do CSV
O arquivo `pets-seed.csv` deve seguir a estrutura:
```csv
name,species,breed,age_years,shelter_city,shelter_lat,shelter_lng,status
Rex,DOG,Pastor Alemão,3,São Paulo,-23.5505200,-46.6333090,AVAILABLE
Luna,CAT,Siamês,2,Rio de Janeiro,-22.9068467,-43.1728965,AVAILABLE
```

**Campos:**
- `name`: Nome do pet (obrigatório)
- `species`: DOG ou CAT (obrigatório)
- `breed`: Raça do pet (opcional, pode estar vazio)
- `age_years`: Idade em anos (opcional, pode estar vazio)
- `shelter_city`: Cidade do abrigo (obrigatório)
- `shelter_lat`: Latitude do abrigo (opcional, pode estar vazio)
- `shelter_lng`: Longitude do abrigo (opcional, pode estar vazio)
- `status`: AVAILABLE ou ADOPTED (obrigatório)

### Como Funciona a Idempotência
O sistema utiliza um **pet marcador** especial com nome `SEED_MARKER_PET` para verificar se o seeding já foi executado:

1. **Primeira execução**: Não encontra o marcador → executa seeding → cria marcador
2. **Execuções subsequentes**: Encontra o marcador → pula seeding

### Logs do Sistema
Durante a execução, você verá logs como:
```
🌱 Iniciando processo de seeding do banco de dados...
📂 Carregando pets do arquivo: data/pets-seed.csv
🐾 Pet salvo: Rex (DOG)
🐾 Pet salvo: Luna (CAT)
🎉 Seeding concluído com sucesso! 15 pets foram inseridos no banco de dados.
```

### Monitoramento do Seeding
```bash
# Ver logs específicos do seeding
docker compose logs backend | grep -E "🌱|🎉|❌"

# Verificar se o seeding foi executado (conectar ao banco)
docker exec -it adoteumpet-postgres psql -U postgres -d adoteumpet_bd

# Contar pets no banco (deve incluir os do seeding + marcador)
SELECT COUNT(*) FROM pets;

# Ver apenas pets reais (excluindo o marcador)
SELECT COUNT(*) FROM pets WHERE name != 'SEED_MARKER_PET';
```

### Modificar Dados do Seeding
Para alterar os dados iniciais:

1. **Edite o arquivo CSV**:
   ```bash
   # Abra o arquivo para edição
   code adoteumpet-api/src/main/resources/data/pets-seed.csv
   ```

2. **Force nova execução do seeding**:
   ```bash
   # Método 1: Remover o marcador do banco
   docker exec -it adoteumpet-postgres psql -U postgres -d adoteumpet_bd
   DELETE FROM pets WHERE name = 'SEED_MARKER_PET';
   
   # Método 2: Limpar todos os dados e reiniciar
   docker compose down -v  # Remove volumes (dados)
   docker compose up -d    # Recria tudo do zero
   ```

3. **Reinicie a aplicação**:
   ```bash
   docker compose restart backend
   ```

### Desabilitar o Seeding
Para desabilitar temporariamente o seeding, renomeie ou remova o arquivo CSV:
```bash
# Renomear o arquivo (desabilita o seeding)
mv adoteumpet-api/src/main/resources/data/pets-seed.csv \
   adoteumpet-api/src/main/resources/data/pets-seed.csv.disabled
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
