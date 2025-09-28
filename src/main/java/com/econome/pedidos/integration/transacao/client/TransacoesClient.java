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

    private RestClient transacoesRestClient;
    private final TransacoesApiProperties properties;

    private RestClient client() {
        if (transacoesRestClient == null) {
            String base = properties.baseUrl();
            if (base.endsWith("/")) base = base.substring(0, base.length() - 1);
            transacoesRestClient = RestClient.builder().baseUrl(base).build();
        }
        return transacoesRestClient;
    }

    public void criarTransacao(TransacaoCreateRequest request) {
        if (!properties.enabled()) {
            log.debug("[TransacoesClient] Integração desabilitada. Ignorando criação descricao='{}'",
                    request.descricao());
            return;
        }
        try {
            client().post()
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
            // 1) Buscar a transação pelo pedido_id
            TransacaoResponse transacao = client().get()
                    .uri("/transacoes/pedido/{pedidoId}", pedidoId)
                    .retrieve()
                    .body(TransacaoResponse.class);
            if (transacao == null || transacao.id() == null) {
                log.warn("[TransacoesClient] Nenhuma transação encontrada para pedidoId={}", pedidoId);
                return false;
            }
            // 2) Atualizar usando endpoint de update por id
            client().put()
                    .uri("/transacao/{id}", transacao.id())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payloadParcial)
                    .retrieve()
                    .toBodilessEntity();
            log.info("[TransacoesClient] Atualizada transação id={} (pedidoId={})", transacao.id(), pedidoId);
            return true;
        } catch (Exception ex) {
            log.error("[TransacoesClient] Erro ao atualizar transação pedidoId={}: {}", pedidoId, ex.getMessage());
            return false;
        }
    }

    /**
     * Record interno para deserializar resposta mínima da API de Transações.
     */
    private record TransacaoResponse(Long id) {
    }
}
