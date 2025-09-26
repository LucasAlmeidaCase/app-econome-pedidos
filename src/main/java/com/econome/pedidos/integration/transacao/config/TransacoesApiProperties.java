package com.econome.pedidos.integration.transacao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Propriedades de configuração da integração com o microserviço de Transações.
 * Prefixo: transacoes.api
 */
@ConfigurationProperties(prefix = "transacoes.api")
public record TransacoesApiProperties(
        String baseUrl,
        @DefaultValue("true") boolean enabled) {
}
