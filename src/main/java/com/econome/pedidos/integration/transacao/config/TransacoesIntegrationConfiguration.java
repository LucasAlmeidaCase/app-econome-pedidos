package com.econome.pedidos.integration.transacao.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configuração de beans para integração com o microserviço de Transações.
 */
@Configuration
@EnableConfigurationProperties(TransacoesApiProperties.class)
public class TransacoesIntegrationConfiguration {

    @Bean(name = "transacoesRestClient")
    @ConditionalOnProperty(prefix = "transacoes.api", name = "enabled", havingValue = "true", matchIfMissing = true)
    RestClient transacoesRestClient(TransacoesApiProperties props, RestClient.Builder builder) {
        return builder.baseUrl(props.baseUrl()).build();
    }
}
