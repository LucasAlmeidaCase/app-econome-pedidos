package com.econome.pedidos.integration.participante.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "participantes.api")
public record ParticipantesApiProperties(
        String baseUrl,
        @DefaultValue("true") boolean enabled
) {
}
