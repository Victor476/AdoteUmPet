# AdoteUmPet A- **Documentação**: JavaDoc completo

## ⚡ Quick Start

**Forma mais rápida de testar a API:**

```bash
# Clone e execute com Docker
git clone https://github.com/Victor476/AdoteUmPet.git
cd AdoteUmPet
docker compose up -d

# Teste a API
curl "http://localhost:8090/api/pets"
```

A API estará disponível em: <http://localhost:8090/api/pets>

## 🚀 Como Executar a Aplicação🐾

**AdoteUmPet** é uma API REST completa para gerenciamento de pets para adoção, desenvolvida com Spring Boot. O projeto foi criado como parte de um desafio técnico e demonstra proficiência em desenvolvimento backend, arquitetura de software e boas práticas de programação.

## 🚀 Tecnologias Utilizadas

- **Backend**: Spring Boot 3.5.5 (Java 21)
- **Frontend**: Next.js 15.5.3 (TypeScript) *(em desenvolvimento)*
- **Banco de Dados**: PostgreSQL 15
- **Migrações**: Flyway
- **Containerização**: Docker & Docker Compose
- **ORM**: JPA/Hibernate
- **Testes**: JUnit 5, Mockito, MockMvc
- **Validação**: Bean Validation (Hibernate Validator)
- **Documentação**: JavaDoc completo

## � Como Executar a Aplicação

### �🐳 **Opção 1: Execução com Docker (Recomendado)**

Esta é a forma mais simples e rápida de executar a aplicação completa.

#### **Pré-requisitos**
- [Docker](https://www.docker.com/get-started) instalado
- [Docker Compose](https://docs.docker.com/compose/install/) instalado

#### **Passos para execução**

```bash
# 1. Clonar o repositório
git clone https://github.com/Victor476/AdoteUmPet.git
cd AdoteUmPet

# 2. Executar todos os serviços
docker compose up -d

# 3. Verificar se os serviços estão rodando
docker compose ps

# 4. Ver logs (opcional)
docker compose logs -f backend
```

#### **Serviços disponíveis**
- **Backend API**: <http://localhost:8090/api/pets> 
- **Frontend**: <http://localhost:3000> *(em desenvolvimento)*
- **Banco PostgreSQL**: `localhost:5433`
  - **Usuário**: `postgres`
  - **Senha**: `postgres123`
  - **Database**: `adoteumpet_bd`

#### **Comandos úteis Docker**

```bash
# Parar todos os serviços
docker compose down

# Parar e remover volumes (limpa banco de dados)
docker compose down -v

# Rebuildar imagens e subir
docker compose up -d --build

# Ver logs de um serviço específico
docker compose logs backend
docker compose logs postgres

# Acessar shell do container backend
docker compose exec backend bash

# Acessar PostgreSQL diretamente
docker compose exec postgres psql -U postgres -d adoteumpet_bd
```

---

### 💻 **Opção 2: Desenvolvimento Local (Sem Docker)**

Para desenvolvimento e debugging mais detalhado.

#### **Pré-requisitos**

##### **Backend (Spring Boot)**
- **Java 21** ou superior ([OpenJDK](https://openjdk.org/) ou [Oracle JDK](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.6+** (ou usar o wrapper `./mvnw` incluído)
- **PostgreSQL 15+** instalado localmente

##### **Frontend (Next.js)**
- **Node.js 18+** ([Download](https://nodejs.org/))
- **npm** ou **yarn**

#### **Configuração do Banco de Dados**

```bash
# 1. Instalar PostgreSQL (Ubuntu/Debian)
sudo apt update
sudo apt install postgresql postgresql-contrib

# 2. Configurar usuário e banco
sudo -u postgres psql
```

```sql
-- No terminal do PostgreSQL
CREATE USER postgres WITH PASSWORD 'postgres123';
CREATE DATABASE adoteumpet_bd OWNER postgres;
GRANT ALL PRIVILEGES ON DATABASE adoteumpet_bd TO postgres;
\q
```

#### **Executar Backend**

```bash
# 1. Navegar para o diretório backend
cd adoteumpet-api

# 2. Configurar variáveis de ambiente (opcional)
export SPRING_PROFILES_ACTIVE=dev
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=adoteumpet_bd
export DB_USER=postgres
export DB_PASSWORD=postgres123

# 3. Compilar e executar com Maven Wrapper
./mvnw clean compile
./mvnw spring-boot:run

# OU usar Maven instalado
mvn clean compile
mvn spring-boot:run

# OU executar JAR após build
./mvnw clean package -DskipTests
java -jar target/adoteumpet-api-0.0.1-SNAPSHOT.jar
```

#### **Executar Frontend**

```bash
# 1. Navegar para o diretório frontend
cd adoteumpet-frontend

# 2. Instalar dependências
npm install
# OU
yarn install

# 3. Executar servidor de desenvolvimento
npm run dev
# OU
yarn dev
```

#### **Verificar execução local**

```bash
# Verificar se backend está rodando
curl http://localhost:8080/api/pets

# Verificar se frontend está rodando (quando disponível)
curl http://localhost:3000
```

---

### 🔧 **Troubleshooting**

#### **Problemas comuns com Docker**

```bash
# Porta já em uso
docker compose down
sudo lsof -i :8090  # Ver processo usando a porta
sudo kill -9 <PID>  # Matar processo se necessário

# Problemas de permissão
sudo docker compose up -d

# Limpar cache do Docker
docker system prune -a
```

#### **Problemas comuns desenvolvimento local**

```bash
# Java não encontrado
java -version
export JAVA_HOME=/path/to/java

# PostgreSQL não conecta
sudo systemctl status postgresql
sudo systemctl start postgresql

# Maven não encontrado
# Use o wrapper: ./mvnw ao invés de mvn

# Porta já em uso
sudo lsof -i :8080
# Ou altere a porta em application.properties
```

### 📋 **URLs de Acesso Rápido**

| Serviço | Docker | Local |
|---------|--------|-------|
| **API Backend** | <http://localhost:8090/api/pets> | <http://localhost:8080/api/pets> |
| **Frontend** | <http://localhost:3000> | <http://localhost:3000> |
| **PostgreSQL** | `localhost:5433` | `localhost:5432` |
| **Swagger UI** | <http://localhost:8090/swagger-ui.html> | <http://localhost:8080/swagger-ui.html> |

## 📡 API REST - Endpoints Disponíveis

A API AdoteUmPet fornece endpoints RESTful completos para gerenciar pets para adoção. Todos os endpoints retornam JSON e seguem as convenções REST.

### 🔍 **GET /api/pets** - Listagem Avançada com Filtros

Endpoint principal para buscar pets com suporte a **filtros dinâmicos**, **paginação** e **ordenação**.

**Parâmetros de Query:**
- `name` (string, opcional): Busca parcial por nome do pet
- `species` (enum, opcional): `DOG` ou `CAT`
- `breed` (string, opcional): Busca parcial por raça
- `shelterCity` (string, opcional): Busca parcial por cidade do abrigo
- `status` (enum, opcional): `AVAILABLE` ou `ADOPTED`
- `page` (int, opcional): Número da página (padrão: 0)
- `size` (int, opcional): Tamanho da página (padrão: 10)
- `sort` (string, opcional): Campo e direção (ex: `name,asc` ou `ageYears,desc`)

**Exemplos de uso:**
```bash
# Buscar todos os pets (primeira página)
GET /api/pets

# Buscar cães disponíveis em São Paulo
GET /api/pets?species=DOG&status=AVAILABLE&shelterCity=São Paulo

# Buscar com paginação e ordenação por idade decrescente
GET /api/pets?page=1&size=5&sort=ageYears,desc

# Buscar pets por nome
GET /api/pets?name=Luna
```

**Resposta (PagedResponse):**
```json
{
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "Luna",
      "species": "CAT",
      "breed": "Siamês",
      "ageYears": 2,
      "shelterCity": "São Paulo",
      "shelterLat": -23.5505200,
      "shelterLng": -46.6333090,
      "status": "AVAILABLE",
      "createdAt": "2024-01-15T10:30:00Z"
    }
  ],
  "page": 0,
  "size": 10,
  "total": 25,
  "totalPages": 3
}
```

### 🆕 **POST /api/pets** - Criar Novo Pet

Cria um novo pet no sistema com validações completas.

**Corpo da requisição:**
```json
{
  "name": "Rex",
  "species": "DOG",
  "breed": "Pastor Alemão",
  "ageYears": 3,
  "shelterCity": "Rio de Janeiro",
  "shelterLat": -22.9068467,
  "shelterLng": -43.1728965,
  "status": "AVAILABLE"
}
```

**Validações aplicadas:**
- `name`: Obrigatório, 1-100 caracteres
- `species`: Obrigatório, deve ser `DOG` ou `CAT`
- `breed`: Opcional, máximo 100 caracteres
- `ageYears`: Opcional, deve ser entre 0 e 30
- `shelterCity`: Obrigatório, 1-100 caracteres
- `shelterLat`: Opcional, latitude válida (-90 a 90)
- `shelterLng`: Opcional, longitude válida (-180 a 180)
- `status`: Opcional, padrão `AVAILABLE`

### 📋 **Outros Endpoints CRUD**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/pets/{id}` | Buscar pet por ID específico |
| `PUT` | `/api/pets/{id}` | Atualizar pet existente |
| `DELETE` | `/api/pets/{id}` | Remover pet |
| `PATCH` | `/api/pets/{id}/adopt` | Marcar pet como adotado |

### 🎯 **Endpoints de Consulta Específica**

| Endpoint | Descrição |
|----------|-----------|
| `GET /api/pets/species/{species}` | Pets por espécie (DOG/CAT) |
| `GET /api/pets/status/{status}` | Pets por status |
| `GET /api/pets/available` | Pets disponíveis para adoção |
| `GET /api/pets/city/{city}` | Pets por cidade do abrigo |
| `GET /api/pets/age?minAge=X&maxAge=Y` | Pets por faixa etária |

### 🛡️ **Tratamento de Erros**

A API possui **tratamento robusto de erros** com o `GlobalExceptionHandler`:

**Validação de campos (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Erro de validação",
  "message": "Dados inválidos fornecidos",
  "errors": {
    "name": "Nome é obrigatório",
    "ageYears": "Idade deve estar entre 0 e 30"
  }
}
```

**Enum inválido (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Parâmetro inválido",
  "message": "Valor inválido 'BIRD' para o parâmetro 'species'. Valores válidos: DOG, CAT"
}
```

### 🧪 **Testado Extensivamente**

- ✅ **30 testes unitários** cobrindo todos os cenários
- ✅ **Testes de integração** com contexto completo
- ✅ **Testes de validação** para todos os campos
- ✅ **Testes de filtros e paginação**
- ✅ **Testes de tratamento de erros**

## 🏗️ Arquitetura e Estrutura do Projeto

### **Arquitetura em Camadas (Layered Architecture)**

O projeto segue uma **arquitetura em camadas bem definida** com separação clara de responsabilidades:

```
📦 com.adoteumpet.adoteumpetapi
├── 🎮 controller/          # Camada de Apresentação (REST Controllers)
├── 💼 service/             # Camada de Negócio (Business Logic)
├── 🗄️ repository/          # Camada de Dados (Data Access)
├── 📋 model/               # Entidades JPA
├── 🔄 dto/                 # Data Transfer Objects
├── 🔍 specification/       # JPA Specifications (Filtros Dinâmicos)
├── ⚙️ config/              # Configurações e Beans
└── 🛡️ exception/           # Tratamento Global de Exceções
```

### **Principais Características Técnicas**

#### 🔍 **Sistema de Filtros Dinâmicos**
- **JPA Specifications** para consultas dinâmicas flexíveis
- **Criteria API** para construção de queries type-safe
- Filtros combináveis por nome, espécie, raça, cidade e status
- Busca parcial e case-insensitive para campos texto

#### 📄 **Paginação e Ordenação Avançada**
- **Spring Data Pageable** integrado aos endpoints
- Suporte a múltiplos critérios de ordenação
- **PagedResponse<T>** personalizado para respostas consistentes
- Metadados completos (total, páginas, tamanho)

#### 🛡️ **Validação e Tratamento de Erros**
- **Bean Validation** com anotações declarativas
- **GlobalExceptionHandler** para tratamento centralizado
- Mensagens de erro descritivas e padronizadas
- Status HTTP semanticamente corretos

#### 🌱 **Sistema de Seeding Inteligente**
- **Carregamento automático** de dados iniciais via CSV
- **Idempotência** - não duplica dados em execuções subsequentes
- **Robust error handling** - continua processamento mesmo com falhas parciais
- **Logging detalhado** do processo de seeding

#### 🗃️ **Migração de Banco com Flyway**
- **Versionamento** automático do esquema do banco
- **Execução automática** na inicialização da aplicação
- **Rollback** seguro e **repeatability**
- **Histórico completo** de mudanças no banco

### **Padrões de Design Implementados**

#### 🏭 **Repository Pattern**
```java
@Repository
public interface PetRepository extends JpaRepository<Pet, UUID>, 
                                      JpaSpecificationExecutor<Pet> {
    // Métodos de consulta personalizados
}
```

#### 🔧 **Builder Pattern (PagedResponse)**
```java
public static <T> PagedResponse<T> from(Page<T> page) {
    return new PagedResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages()
    );
}
```

#### 🎯 **Strategy Pattern (Specifications)**
```java
public static Specification<Pet> hasName(String name) {
    return (root, query, criteriaBuilder) -> {
        // Implementação da estratégia de filtro por nome
    };
}
```

### **Qualidade de Código**

#### 📚 **Documentação**
- **JavaDoc completo** em todas as classes e métodos
- **README detalhado** com exemplos de uso
- **Comentários explicativos** nos pontos complexos

#### 🧪 **Cobertura de Testes**
- **30 testes unitários** organizados por funcionalidade
- **Testes de integração** com contexto Spring completo
- **Testes de contrato** para validação de DTOs
- **Cenários de erro** testados extensivamente

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

## 🧪 Como Testar a API

### **Executar Testes Unitários**

```bash
# Executar todos os testes
cd adoteumpet-api
./mvnw test

# Executar testes com relatório de cobertura
./mvnw test jacoco:report
```

### **Testar API Manualmente**

Com a aplicação rodando (`docker compose up -d`), você pode testar os endpoints:

#### **1. Criar um novo pet**

```bash
curl -X POST http://localhost:8090/api/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bolt",
    "species": "DOG",
    "breed": "Golden Retriever",
    "ageYears": 2,
    "shelterCity": "São Paulo",
    "shelterLat": -23.5505200,
    "shelterLng": -46.6333090,
    "status": "AVAILABLE"
  }'
```

#### **2. Buscar pets com filtros**

```bash
# Todos os pets (primeira página)
curl "http://localhost:8090/api/pets"

# Cães disponíveis em São Paulo
curl "http://localhost:8090/api/pets?species=DOG&status=AVAILABLE&shelterCity=São Paulo"

# Paginação e ordenação
curl "http://localhost:8090/api/pets?page=0&size=5&sort=ageYears,desc"

# Busca por nome
curl "http://localhost:8090/api/pets?name=Luna"
```

#### **3. Testar validações (deve retornar erro 400)**

```bash
# Nome em branco
curl -X POST http://localhost:8090/api/pets \
  -H "Content-Type: application/json" \
  -d '{"name": "", "species": "DOG", "shelterCity": "São Paulo"}'

# Espécie inválida
curl "http://localhost:8090/api/pets?species=BIRD"
```

### **Ferramentas Recomendadas**

- **Postman**: Importe a collection de endpoints
- **Insomnia**: Para testes de API REST
- **curl**: Para testes rápidos via terminal
- **HTTPie**: Alternativa mais amigável ao curl

## 📊 Resumo das Funcionalidades Implementadas

### ✅ **Backend Completo**

- [x] **API REST** com 12+ endpoints funcionais
- [x] **CRUD completo** de pets (Create, Read, Update, Delete)
- [x] **Filtros dinâmicos** por nome, espécie, raça, cidade, status
- [x] **Paginação e ordenação** avançada
- [x] **Validação robusta** com mensagens descritivas
- [x] **Tratamento de erros** padronizado e profissional
- [x] **Persistência** com PostgreSQL e JPA/Hibernate
- [x] **Migrações automáticas** com Flyway
- [x] **Seeding inteligente** com dados de exemplo
- [x] **Testes extensivos** (30 testes unitários + integração)
- [x] **Documentação JavaDoc** completa
- [x] **Dockerização** para fácil execução

### 🚧 **Frontend (Em Desenvolvimento)**

- [ ] Interface React/Next.js para listagem de pets
- [ ] Formulários para cadastro e edição
- [ ] Sistema de filtros na UI
- [ ] Integração com API backend
- [ ] Design responsivo com Tailwind CSS

### 🎯 **Diferenciais Técnicos**

- **Arquitetura bem estruturada** seguindo boas práticas
- **Código limpo** com responsabilidades bem definidas
- **Performance otimizada** com índices de banco
- **Tratamento de erros robusto** para melhor UX
- **Testabilidade** com alta cobertura de testes
- **Documentação profissional** para facilitar manutenção
- **DevOps ready** com Docker e docker-compose

## 👨‍💻 Autor

**Victor** - [@Victor476](https://github.com/Victor476)

---

### 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

*Desenvolvido como parte de um desafio técnico, demonstrando proficiência em Spring Boot, arquitetura de software e boas práticas de desenvolvimento.*
