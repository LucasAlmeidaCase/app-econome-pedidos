package com.econome.pedidos.integration.transacao.client;

import com.econome.pedidos.integration.transacao.config.TransacoesApiProperties;
import com.econome.pedidos.integration.transacao.dto.TransacaoCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP para o microserviço de Transações.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransacoesClient {

    private final RestClient transacoesRestClient;
    private final TransacoesApiProperties properties;

    public void criarTransacao(TransacaoCreateRequest request) {
        if (!properties.enabled()) {
            log.debug("[TransacoesClient] Integração desabilitada. Ignorando criação descricao='{}'",
                    request.descricao());
            return;
        }
        try {
            transacoesRestClient.post()
                    .uri("/transacao")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
            log.info("[TransacoesClient] Criada transação descricao='{}'", request.descricao());
        } catch (Exception ex) {
            log.error("[TransacoesClient] Erro ao criar transação descricao='{}': {}", request.descricao(),
                    ex.getMessage());
        }
    }

    public boolean atualizarTransacaoPorPedido(Long pedidoId, Object payloadParcial) {
        if (!properties.enabled()) {
            log.debug("[TransacoesClient] Integração desabilitada. Ignorando update pedidoId={}", pedidoId);
            return false;
        }
        try {
            transacoesRestClient.put()
                    .uri("/transacoes/pedido/{pedidoId}", pedidoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payloadParcial)
                    .retrieve()
                    .toBodilessEntity();
            log.info("[TransacoesClient] Atualizada transação pedidoId={}", pedidoId);
            return true;
        } catch (Exception ex) {
            log.error("[TransacoesClient] Erro ao atualizar transação pedidoId={}: {}", pedidoId, ex.getMessage());
            return false;
        }
    }
}
