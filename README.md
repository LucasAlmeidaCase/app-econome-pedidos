# 📦 App Econome - Pedidos API

API REST em Java 21 com Spring Boot para gerenciamento de Pedidos. Expõe operações CRUD, validação com Bean Validation, mapeamento com MapStruct, migrações com Liquibase e documentação automática via OpenAPI/Swagger UI.

Integra-se de forma event-driven com o microserviço de Transações (Python). Quando um Pedido é criado ou atualizado com situação `FATURADO`, um Domain Event dispara a criação automática da transação financeira correspondente (referência lógica via `pedido_id`).

---

## 🧰 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- MySQL 8
- Liquibase
- Lombok
- MapStruct
- springdoc-openapi (Swagger UI)
- Maven
- Docker & Docker Compose
- RestClient (cliente HTTP moderno do Spring)
- Domain Events (`@TransactionalEventListener`) para integração pós-commit

---

## ✅ Pré-requisitos

- JDK 21 (obrigatório)
- Maven 3.8+
- MySQL 8 (local ou via Docker Compose)
- Docker e Docker Compose (opcional, para subir o banco rapidamente)

---

## ▶️ Executando a Aplicação Localmente

1. Suba o MySQL (opção recomendada via Docker Compose):
   - O arquivo `docker-compose.yml` orquestra apenas o serviço do MySQL na porta `3306:3306` com o database `econome_db_pedidos` e senha `12345`.
   - Execute o Compose e aguarde o banco inicializar.

2. Execute a aplicação Spring Boot:
   - A aplicação sobe por padrão na porta `8080`.

3. Acesse:
   - API (saúde): `http://localhost:8080/actuator/health` (se exposto)
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - OpenAPI (JSON): `http://localhost:8080/v3/api-docs`

> As migrações do Liquibase são aplicadas automaticamente na inicialização (config em `src/main/resources/config/liquibase`).

---

## 🐳 Docker / Execução Containerizada

### Rede compartilhada entre microserviços

Para integração com o microserviço de Transações rodando em outro repositório/compose, utilize uma rede Docker externa comum:

```bash
docker network create econome-net   # executar uma única vez
```

### Serviços orquestrados neste repositório

- `mysql-econome-pedidos` (MySQL 8)
- `app-econome-pedidos` (Spring Boot)

### Passos de subida

```bash
docker compose up -d --build
```

Isso iniciará MySQL + API Pedidos. O container automaticamente lê variáveis do `.env`.

### Conectar a API de Transações (já subida em outro compose)

Após subir o serviço de transações (Python):

```bash
docker network connect econome-net app-econome-transacoes || true
```

Ou configure a rede no compose do serviço de transações conforme README daquele serviço.

### Variáveis de ambiente principais (.env)

| Variável | Papel | Exemplo |
|----------|-------|---------|
| TRANSACOES_API_BASE_URL | Base URL do microserviço de transações | <http://app-econome-transacoes:5001> |
| SPRING_DATASOURCE_URL | JDBC do MySQL | jdbc:mysql://mysql-econome-pedidos:3306/econome_db_pedidos |
| SPRING_DATASOURCE_USERNAME | Usuário DB | root |
| SPRING_DATASOURCE_PASSWORD | Senha DB | 12345 |
| SPRING_JPA_HIBERNATE_DDL_AUTO | Estratégia DDL | validate |
| SPRING_LIQUIBASE_ENABLED | Ativa Liquibase | true |
| SPRING_PROFILES_ACTIVE | Profile Spring | default |

Fallbacks estão definidos em `application.yml` usando placeholders.

---

## ⚙️ Configuração de Ambiente

As principais configurações estão em `src/main/resources/config/application.yml` e `.env`:

- URL JDBC: `jdbc:mysql://mysql-econome-pedidos:3306/econome_db_pedidos?...`
- Usuário: `root`
- Senha: `12345`
- Liquibase: habilitado e apontando para `classpath:config/liquibase/db.changelog-master.yml`

Ajuste as credenciais conforme seu ambiente, se necessário.

---

## 🌐 Documentação OpenAPI

Após iniciar a aplicação:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

A configuração do OpenAPI está em `com.econome.pedidos.config.OpenApiConfiguration`.

---

## 🧱 Estrutura do Projeto

```text
app-econome-pedidos/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/econome/
│   │   │       ├── domain/                 # Entidades JPA (Pedido)
│   │   │       └── pedidos/
│   │   │           ├── config/             # Configurações (JPA, OpenAPI)
│   │   │           ├── controller/         # Controllers REST
│   │   │           │   └── advice/         # Tratamento global de erros (ControllerAdvice)
│   │   │           ├── dto/                # DTOs e Mapper (MapStruct)
│   │   │           ├── enums/              # Enums de domínio
│   │   │           ├── exception/          # Exceções específicas e payload de erro
│   │   │           └── service/            # Serviços (regras de negócio)
│   │   └── resources/
│   │       └── config/
│   │           ├── application.yml         # Configuração da aplicação
│   │           └── liquibase/              # Changelogs do Liquibase
│   └── test/
├── .env                                    # Variáveis de ambiente para Docker Compose
├── docker-compose.yml                      # Orquestração dos serviços
├── Dockerfile                              # Imagem Docker da aplicação
├── pom.xml
└── README.md
```

---

## 🧠 Principais Funcionalidades

- CRUD de Pedidos (GET/POST/PUT/DELETE)
- Validação de payload com Bean Validation (@Valid)
- Mapeamento DTO/Entidade com MapStruct
- Tratamento de erros centralizado (@ControllerAdvice) com payload consistente (ProblemDetails)
- Migrações de banco com Liquibase
- Documentação de API com OpenAPI/Swagger UI

---

## 🧪 Build e Execução (Maven)

- Compilar (sem testes):
  - Windows: `mvnw.cmd -DskipTests clean package`
  - Linux/macOS: `./mvnw -DskipTests clean package`

- Rodar a aplicação:
  - Windows: `mvnw.cmd spring-boot:run`
  - Linux/macOS: `./mvnw spring-boot:run`

---

## 🔐 Observações sobre Qualidade e Arquitetura

- Padrões de código e nomenclatura consistentes.
- Organização por domínios/camadas (controller, service, repository, model, config, exception).
- SOLID/DDD: serviços com interfaces, injeção por construtor, entidades enxutas.
- Persistência: transações no serviço, DTOs expostos externamente, entidade sem regra de negócio.
- Uso de APIs modernas (Records em DTOs, Streams) quando agrega clareza.

---

## 🧾 Modelagem (Resumo)

Entidade Pedido (principais campos):

- `id` (Long)
- `numeroPedido` (String) – identificador legível (ex: PED-129)
- `situacaoPedido` (Enum) – `ABERTO`, `FATURADO`, `CANCELADO` (extensível)
- `dataEmissaoPedido` (OffsetDateTime/ZonedDateTime)
- `valorTotalPedido` (BigDecimal)
- Campos financeiros opcionais quando situacao = FATURADO:
   - `dataVencimentoTransacao` (LocalDate)
   - `pagoTransacao` (Boolean)
   - `dataPagamentoTransacao` (LocalDate)

Regras:

- Somente `FATURADO` aciona criação de transação.
- `dataPagamentoTransacao` só é considerada se `pagoTransacao=true`.

---

## 🔄 Integração com Transações

Fluxo resumido:

1. Pedido com `situacaoPedido=FATURADO` é confirmado no banco.
2. Evento de domínio pós-commit dispara o cliente HTTP.
3. Envia POST `/transacao` (serviço Python) com descrição padronizada e `pedido_id`.
4. Transação fica disponível para leitura via `/transacoes/pedido/{pedido_id}`.

Configuração de rede necessária (ambientes containerizados separados):

```bash
docker network create econome-net                # uma única vez
docker network connect econome-net app-econome-transacoes
```

Verificação rápida:
```bash
docker compose exec app-econome-pedidos sh -c "apk add --no-cache curl || true; curl -s http://app-econome-transacoes:5001/openapi | head"
```

Próximos aprimoramentos planejados:
- Outbox + mensageria (resiliência)
- Idempotência baseada em `pedido_id`
- Monitoramento / tracing distribuído

---

## 🌐 Endpoints Principais

| Método | Caminho         | Descrição            |
|--------|-----------------|----------------------|
| GET    | /pedidos        | Lista pedidos        |
| GET    | /pedidos/{id}   | Busca por id         |
| POST   | /pedidos        | Cria novo pedido     |
| PUT    | /pedidos/{id}   | Atualiza pedido      |
| DELETE | /pedidos/{id}   | Remove pedido        |

> Paginação e filtros (situacao, período) planejados no roadmap.

---

## 🧪 Exemplos de Requisição

### Criar Pedido ABERTO

```http
POST /pedidos
Content-Type: application/json

{
   "numeroPedido": "PED-200",
   "situacaoPedido": "ABERTO",
   "dataEmissaoPedido": "2025-09-26T10:05:00-03:00",
   "valorTotalPedido": 450.00
}
```

### Criar Pedido FATURADO (gera transação)

```http
POST /pedidos
Content-Type: application/json

{
   "numeroPedido": "PED-201",
   "situacaoPedido": "FATURADO",
   "dataEmissaoPedido": "2025-09-26T11:12:00-03:00",
   "valorTotalPedido": 999.90,
   "dataVencimentoTransacao": "2025-10-15",
   "pagoTransacao": true,
   "dataPagamentoTransacao": "2025-09-29"
}
```

### Atualizar Pedido para FATURADO

```http
PUT /pedidos/201
Content-Type: application/json

{
   "numeroPedido": "PED-201",
   "situacaoPedido": "FATURADO",
   "dataEmissaoPedido": "2025-09-26T11:12:00-03:00",
   "valorTotalPedido": 999.90,
   "dataVencimentoTransacao": "2025-10-20"
}
```

### Resposta (exemplo)

```json
{
   "id": 42,
   "numeroPedido": "PED-201",
   "situacaoPedido": "FATURADO",
   "dataEmissaoPedido": "2025-09-26T11:12:00-03:00",
   "valorTotalPedido": 999.90
}
```

### Erro de Validação

```json
{
   "status": 400,
   "titulo": "Erro de validação",
   "violacoes": [
      { "campo": "numeroPedido", "mensagem": "não pode estar em branco" }
   ]
}
```

---

## 🛠️ Domain Events

- `PedidoCriadoEvent` / `PedidoAtualizadoEvent` (ou equivalente consolidado) publicados após commit.
- Listener usa RestClient configurado para URL base da API de Transações.
- Falhas atualmente logadas (retry futuro planejado).

Ponto de melhoria: implementar padrão Transactional Outbox para confiabilidade em cenários de indisponibilidade externa.

---

## ❗ Padrão de Erros

ControllerAdvice retorna payload inspirado em RFC 7807 com campos para violações de validação.

Exemplo recurso não encontrado:

```json
{
   "status": 404,
   "titulo": "Recurso não encontrado",
   "detalhe": "Pedido 999 inexistente"
}
```

---

## ⚙️ Variáveis de Ambiente Relevantes

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| SPRING_PROFILES_ACTIVE | Profile ativo | dev |
| APP_TRANSACTIONS_BASE_URL | URL da API de Transações | <http://localhost:5001> |
| TZ | Timezone do container | America/Sao_Paulo |

Fallback: caso `APP_TRANSACTIONS_BASE_URL` não esteja definido, o client pode usar valor padrão interno.

---

## 🚀 Roadmap / Próximas Melhorias

- Paginação e filtros avançados (situação, intervalo datas)
- Idempotência de integração (checar se transação já existe antes de criar)
- Outbox + mensageria (Kafka) para confiabilidade
- Testes de contrato entre serviços (Pact / Spring Cloud Contract)
- Observabilidade: tracing distribuído (OpenTelemetry)
- Versionamento de API (v1, v2)
- Endpoint de busca por número de pedido
- Cache de leitura para GET /pedidos/{id}

---

---

## 👤 Autor

- Desenvolvido por **Lucas Almeida**.
