package com.econome.pedidos.integration.participante.client;

import com.econome.pedidos.integration.participante.config.ParticipantesApiProperties;
import com.econome.pedidos.integration.participante.dto.ParticipanteResumo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

/**
 * Client simples (RestClient) para buscar participante individual.
 * Pode ser evoluído futuramente para batch.
 */
@Component
@RequiredArgsConstructor
public class ParticipantesClient {

    private static final Logger log = LoggerFactory.getLogger(ParticipantesClient.class);
    private final ParticipantesApiProperties properties;

    private RestClient restClient;

    private RestClient client() {
        if (restClient == null) {
            String base = properties.baseUrl();
            // Remove trailing slash para consistência
            if (base.endsWith("/")) base = base.substring(0, base.length() - 1);
            restClient = RestClient.builder().baseUrl(base).build();
        }
        return restClient;
    }

    public Optional<ParticipanteResumo> buscarPorId(Long id) {
        if (id == null || !properties.enabled()) return Optional.empty();
        try {
        ParticipanteResumo body = client().get()
            .uri("/api/participantes/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> log.warn("Participante {} não encontrado (status {})", id, res.getStatusCode()))
                    .body(ParticipanteResumo.class);
            return Optional.ofNullable(body);
        } catch (Exception ex) {
            log.error("Erro ao consultar participante {}: {}", id, ex.getMessage());
            return Optional.empty();
        }
    }
}
