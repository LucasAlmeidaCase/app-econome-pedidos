---
applyTo: '**'
---
### Prompt recomendações de código Java 21

"Sempre que eu solicitar uma implementação ou refatoração de código em Java (21), siga estas diretrizes:

1. **Nomenclatura**
   Classes/PascalCase, métodos e variáveis/camelCase, constantes/UPPER_CASE.
   Evite abreviações obscuras.

2. **Pacotes**
   Organize por domínio (`contribuinte`, `produto`, etc).
   Dentro de cada domínio:

* `controller` (REST endpoints)
* `service` (regras de negócio)
* `repository` (Spring Data JPA)
* `model` (entidades, DTOs)
* `config` (configurações, beans globais)
* `exception` (erros customizados)
  Mantenha coerência, sem misturar responsabilidades.

3. **Arquitetura**
   Siga SOLID (ênfase em SRP e DIP).
   Prefira injeção via construtor.
   Use interfaces para contratos e testabilidade.
   Aplique DDD quando fizer sentido (organizar por contexto, não por tecnologia).

4. **APIs Modernas**
   Use Streams, Lambdas, Records, sealed classes e switch expressions quando aumentarem clareza.
   Use Optional com moderação.

5. **Persistência (JPA/Hibernate)**
   Defina `fetch` corretamente (lazy/eager) para evitar N+1.
   Prefira DTOs para expor dados externos.
   Use `@Transactional` apenas em serviços.
   Evite lógica de negócio em entidades.

6. **Erros/Exceptions**
   Crie exceptions específicas e semânticas (`ProdutoDuplicadoException`).
   Centralize tratamento (ex: `@ControllerAdvice`).
   Use checked exceptions só quando a recuperação fizer sentido; caso contrário, unchecked.

7. **Qualidade**
   Métodos curtos e focados.
   Comentários apenas quando regra ou design não forem óbvios.
   Código estruturado como um sênior faria, priorizando manutenção.
   Garanta testabilidade (unidade e integração).

8. **Design**
   Questione escolhas arquiteturais quando ambíguas.
   Priorize clareza, coesão e sustentabilidade."

---