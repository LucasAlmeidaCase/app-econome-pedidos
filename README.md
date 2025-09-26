# 📦 App Econome - Pedidos API

API REST em Java 21 com Spring Boot para gerenciamento de Pedidos. Expõe operações CRUD, validação com Bean Validation, mapeamento com MapStruct, migrações com Liquibase e documentação automática via OpenAPI/Swagger UI.

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

## 🐳 Docker Compose

O `docker-compose.yml` deste repositório agora orquestra tanto o serviço do banco quanto a aplicação Spring Boot:

- `mysql-econome-pedidos` (imagem `mysql:8.0`)
  - Porta: `3306:3306`
  - Variáveis: `MYSQL_DATABASE=econome_db_pedidos`, `MYSQL_ROOT_PASSWORD=12345`, `TZ=America/Sao_Paulo`
  - Volume: `./mysql-data-pedidos:/var/lib/mysql`

- `app-econome-pedidos` (builda a partir do Dockerfile)
  - Porta: `8080:8080`
  - Depende do MySQL
  - Variáveis de ambiente lidas do arquivo `.env` (já fornecido)
  - Build automático do JAR via Maven multi-stage

Passos:
1. Subir todos os serviços (banco e aplicação):
   - `docker-compose up -d`
2. Parar os serviços:
   - `docker-compose down`

> O arquivo `.env` centraliza as variáveis de ambiente para ambos os serviços. Ajuste conforme necessário.

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
```
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

## 👤 Autor
- Desenvolvido por **Lucas Almeida**.
