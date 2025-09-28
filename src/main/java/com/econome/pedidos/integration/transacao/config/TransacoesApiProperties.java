package com.econome.pedidos.integration.transacao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades de configuração da integração com o microserviço de Transações.
 * Prefixo: transacoes.api
 */
@ConfigurationProperties(prefix = "transacoes.api")
public record TransacoesApiProperties(
        String baseUrl,
        // Aceita valores problemáticos como "=true", "true", "false", "1", "0", etc.
        String enabledRaw) {

    /**
     * Interpreta o valor de enabled com tolerância a formatos inválidos.
     * Default: true quando vazio/nulo.
     */
    public boolean enabled() {
        if (enabledRaw == null || enabledRaw.isBlank()) return true; // default
        String v = enabledRaw.trim();
        while (v.startsWith("=")) { // remove '=' extras (ex: '=true')
            v = v.substring(1).trim();
        }
        if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("1") || v.equalsIgnoreCase("yes") || v.equalsIgnoreCase("on")) {
            return true;
        }
        if (v.equalsIgnoreCase("false") || v.equalsIgnoreCase("0") || v.equalsIgnoreCase("no") || v.equalsIgnoreCase("off")) {
            return false;
        }
        // Valor não reconhecido: log poderia ser adicionado; assume true para comportamento mais seguro
        return true;
    }
}
