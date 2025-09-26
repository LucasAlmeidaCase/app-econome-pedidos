package com.econome.pedidos.integration.transacao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP responsável por enviar requisições ao microserviço de Transações.
 * Implementado sobre o {@link RestClient} (Spring 6+) para aproveitar API fluente moderna.
 * O envio é suprimido se a integração estiver desabilitada via propriedade (transacoes.api.enabled=false).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransacoesClient {

    private final RestClient transacoesRestClient;
    private final TransacoesApiProperties properties;

    /**
     * Dispara criação de uma transação no serviço Python.
     * Fire-and-forget: falhas são apenas logadas para não impactar o fluxo de negócio do Pedido.
     */
    public void criarTransacao(TransacaoCreateRequest request) {
        if (!properties.enabled()) {
            log.debug("[TransacoesClient] Integração desabilitada. Transação ignorada para descricao='{}'", request.descricao());
            return;
        }
        try {
            log.debug("[TransacoesClient] Enviando criação de transação: descricao='{}', tipo='{}'", request.descricao(), request.tipo_transacao());
            transacoesRestClient.post()
                    .uri("/transacao")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
            log.info("[TransacoesClient] Transação enviada com sucesso para descricao='{}'", request.descricao());
        } catch (Exception ex) {
            log.error("[TransacoesClient] Falha ao criar transação para descricao='{}': {}", request.descricao(), ex.getMessage());
        }
    }
}
