package com.econome.pedidos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI/Swagger usando springdoc-openapi.
 * Expõe a documentação interativa em /swagger-ui.html e o documento OpenAPI em /v3/api-docs.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "API do aplicativo Econome Pedidos",
                version = "v1.0",
                description = "API para gerenciamento do aplicativo Econome Pedidos"
        )
)
@Configuration
public class OpenApiConfiguration {

    /**
     * Personaliza as informações básicas exibidas na documentação OpenAPI.
     * Preferir definições concisas e claras para facilitar a manutenção futura.
     *
     * @return modelo OpenAPI com título, versão, licença e descrição da API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Econome Pedidos API")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0"))
                        .description("API do aplicativo Econome Pedidos"));
    }
}
