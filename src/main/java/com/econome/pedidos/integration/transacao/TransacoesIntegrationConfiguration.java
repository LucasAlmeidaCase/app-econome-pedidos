package com.econome.pedidos.integration.transacao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configuração de beans para integração com o microserviço de Transações.
 * Cria um {@link RestClient} dedicado baseado na URL configurada em propriedades.
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

