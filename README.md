# Manual de Execução e Manutenção

Este manual descreve como executar a aplicação **CRUD Tarefas** desenvolvida em Java com Spring Boot, como interpretar os workflows de CI/CD do GitHub Actions e as principais refatorações realizadas no código-fonte.

## 1. Execução da Aplicação Integrada

A aplicação consiste em um backend Spring Boot que expõe uma API REST e uma interface web para operações de CRUD. Para execução completa, são necessários:

- **Java 21** (ou superior)
- **Maven 4.0+**
- **Git** (para clonar o repositório)

### 1.1. Clonar o repositório
```bash
git clone https://github.com/JMRwork/PB-EDS.git
cd PB-EDS
```
### 1.2. Executar localmente com Maven (desenvolvimento)

Verifique se há maven integrado em alguma IDE ou instalada na máquina.

Então execute a aplicação:

```bash
mvn dependency:resolve
mvn spring-boot:run 
```
Acesse: http://localhost:8080

## 2. Workflows do GitHub Actions
O repositório contém workflows na pasta .github/workflows para automatizar build e testes.

### 2.1. Estrutura dos Workflows
test_build.yaml – Executa testes e validações em cada push ou pull request, além de ser possível também pela Web UI do github com workflow dispatch.

### 2.2. Como Executar os Workflows
Os workflows são acionados automaticamente pelos eventos configurados. Para visualizar e interpretar:

Acesse o repositório no GitHub.

Vá na aba Actions.

Selecione o workflow desejado (ex.: "PB-EDS - Test and build on Push").

Clique em uma execução específica para ver os logs detalhados.

### 2.3. Interpretando os Resultados
PB-EDS - Test and build on Push (test_build.yml)

Build: verifica se o projeto compila.

Testes: executa testes unitários e de integração.

Se todos os jobs estiverem verdes (✅), a integração está saudável.

Se vermelho (❌), expanda o job falho para ver qual teste ou etapa falhou.

Artefatos: ao final, pode gerar um JAR para download. E gera um relatório de cobertura de código.

## 3. Principais Mudanças Durante a Refatoração
Durante o processo de refatoração, foram implementadas as seguintes melhorias:

### 3.1. Arquitetura em Camadas
Antes: lógica de negócio e acesso a dados misturados nos controllers.

Depois: separação em Controller, Service e Repository, seguindo o padrão REST.

### 3.2. Uso de DTOs
Substituição do uso direto das entidades JPA nos endpoints por DTOs (TarefaDTO, AdicionarTarefaDTO), evitando exposição desnecessária de campos.

### 3.3. Validação de Dados
Adição de anotações de validação (@Min, @Size, @NotBlank) nos DTOs.

### 3.4. Tratamento Global de Exceções
Criação do GlobalExceptionHandler para padronizar respostas de erro (status HTTP e mensagens amigáveis).

### 3.5. Testes Automatizados
Escrita de testes unitários e testes service e controllers.

Cobertura de testes acima de 85% (JaCoCo).

### 3.6. CI/CD com GitHub Actions
Implementação de workflows para build, testes automatizados, garantindo qualidade e integração contínua.

