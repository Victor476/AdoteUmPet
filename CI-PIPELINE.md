# CI Pipeline - AdoteUmPet

Este documento descreve o pipeline de Integração Contínua (CI) configurado para o projeto AdoteUmPet.

## 📋 Visão Geral

O pipeline de CI é executado automaticamente a cada `push` ou `pull request` nas branches `main` e `master`, garantindo que:
- ✅ O código compile corretamente
- ✅ Todos os testes passem
- ✅ A qualidade do código seja mantida

## 🏗️ Estrutura do Pipeline

### 📁 Arquivo de Configuração
- **Localização**: `.github/workflows/ci.yml`
- **Ferramenta**: GitHub Actions

### 🚀 Jobs Configurados

#### 1. Backend CI (Java/Spring Boot)
- **Ambiente**: Ubuntu Latest
- **Java**: Version 21 (Temurin)
- **Database**: PostgreSQL 15 (para testes de integração)

**Etapas:**
1. 📥 Checkout do código
2. ☕ Setup Java 21
3. 📦 Cache das dependências Maven
4. 🔍 Validação do Maven Wrapper
5. 🧹 Limpeza do projeto
6. 🔧 Compilação
7. 🧪 Execução dos testes unitários
8. 🏗️ Build final
9. 📋 Upload dos artefatos de teste

#### 2. Frontend CI (Node.js/Next.js)
- **Status**: Preparado mas desabilitado até implementação completa do frontend
- **Ambiente**: Ubuntu Latest
- **Node.js**: Version 18

**Etapas Planejadas:**
1. 📥 Checkout do código
2. 🟢 Setup Node.js 18
3. 📦 Instalação de dependências
4. 🔍 Execução do lint
5. 🧪 Execução dos testes
6. 🏗️ Build do projeto

### 🎯 Verificação Obrigatória
- Job que garante que pelo menos o backend passou em todos os testes
- Falha se o backend não conseguir compilar ou se os testes falharem

## 🔧 Configuração Local

### Pré-requisitos
- Java 21
- Maven 3.9+
- PostgreSQL (para testes de integração completos)

### Executando os Testes Localmente

```bash
# Navegar para o diretório do backend
cd adoteumpet-api

# Executar apenas os testes unitários
./mvnw test

# Executar testes com perfil específico
./mvnw test -Dspring.profiles.active=test

# Executar limpeza completa e testes
./mvnw clean test

# Gerar relatório de cobertura
./mvnw jacoco:report
```

## 🛠️ Plugins Maven Configurados

### 1. Surefire Plugin (Testes Unitários)
- **Versão**: 3.0.0-M9
- **Inclui**: `**/*Test.java`, `**/*Tests.java`
- **Exclui**: `**/*IntegrationTest.java`

### 2. Failsafe Plugin (Testes de Integração)
- **Versão**: 3.0.0-M9
- **Inclui**: `**/*IntegrationTest.java`, `**/*IT.java`

### 3. JaCoCo Plugin (Cobertura de Código)
- **Versão**: 0.8.11
- **Gera**: Relatórios de cobertura em `target/site/jacoco/`

## 📊 Artefatos Gerados

O pipeline gera e armazena os seguintes artefatos:

### Backend
- **Relatórios de Teste**: `target/surefire-reports/`
- **Relatórios de Cobertura**: `target/site/jacoco/`
- **Retenção**: 5 dias

### Frontend (quando ativado)
- **Relatórios de Cobertura**: `coverage/`
- **Build**: `.next/`
- **Retenção**: 5 dias

## 🔍 Monitoramento

### Como Verificar o Status do Pipeline

1. **GitHub Web Interface**:
   - Vá para a aba "Actions" do repositório
   - Visualize o status de cada execução

2. **Badge no README** (opcional):
   ```markdown
   ![CI Pipeline](https://github.com/Victor476/AdoteUmPet/workflows/CI%20Pipeline/badge.svg)
   ```

### Estados do Pipeline
- ✅ **Success**: Todos os testes passaram
- ❌ **Failure**: Algum teste falhou ou erro de compilação
- 🟡 **Running**: Pipeline em execução
- ⏸️ **Cancelled**: Execução cancelada

## 🚨 Troubleshooting

### Problemas Comuns

#### 1. Testes Falhando
```bash
# Verificar logs detalhados
./mvnw test -X

# Executar teste específico
./mvnw test -Dtest=NomeDoTeste
```

#### 2. Problemas de Dependências
```bash
# Limpar cache do Maven
./mvnw dependency:purge-local-repository

# Redownload de dependências
./mvnw clean install -U
```

#### 3. Problemas de Cobertura (JaCoCo)
- Verificar se a versão do JaCoCo é compatível com Java 21
- Atualmente usando versão 0.8.11 que suporta Java 21

## 🎯 Métricas de Qualidade

### Critérios de Aprovação
- ✅ Compilação bem-sucedida
- ✅ Todos os testes unitários passando
- ✅ Cobertura de código > 50% (configurável)
- ✅ Sem erros críticos de lint (quando frontend for ativado)

### Próximos Passos
1. Ativar frontend CI quando implementado
2. Adicionar testes de integração mais robustos
3. Configurar deployment automático após CI verde
4. Adicionar análise de qualidade de código (SonarQube/CodeClimate)

## 📚 Referências

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [JaCoCo Maven Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web)