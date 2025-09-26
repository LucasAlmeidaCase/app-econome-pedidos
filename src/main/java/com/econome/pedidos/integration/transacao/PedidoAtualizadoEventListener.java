package com.econome.pedidos.integration.transacao;

import com.econome.pedidos.enums.TipoPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Listener para {@link PedidoAtualizadoEvent}. Executa lógica de upsert da
 * transação associada.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(TransacoesClient.class)
public class PedidoAtualizadoEventListener {

    private final TransacoesClient transacoesClient;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPedidoAtualizado(PedidoAtualizadoEvent event) {
        if (!event.faturadoAtual()) {
            log.debug("[PedidoAtualizadoEventListener] Pedido {} não está FATURADO após atualização. Nenhuma ação.",
                    event.idPedido());
            return;
        }

        String descricao = buildDescricao(event.idPedido(), event.numeroPedido());
        String tipoTransacao = mapTipoPedidoParaTipoTransacao(event.tipoPedido());
        BigDecimal valor = Objects.requireNonNullElse(event.valorTotal(), BigDecimal.ZERO);

        if (!event.faturadoAnterior()) {
            // Transição para FATURADO -> criar transação
            log.debug("[PedidoAtualizadoEventListener] Pedido {} passou a FATURADO. Criando transação.",
                    event.idPedido());
            transacoesClient.criarTransacao(new TransacaoCreateRequest(
                    descricao,
                    tipoTransacao,
                    valor,
                    Boolean.TRUE.equals(event.pago()),
                    event.dataVencimento(),
                    Boolean.TRUE.equals(event.pago()) ? event.dataPagamento() : null,
                    event.idPedido()));
            return;
        }

        // Já era FATURADO: tentar atualização parcial; se falhar, fallback criação
        // (idempotência fraca)
        Map<String, Object> parcial = new HashMap<>();
        parcial.put("descricao", descricao);
        parcial.put("tipo_transacao", tipoTransacao);
        parcial.put("valor", valor);
        if (event.dataVencimento() != null)
            parcial.put("data_vencimento", event.dataVencimento());
        if (event.pago() != null)
            parcial.put("pago", event.pago());
        if (Boolean.TRUE.equals(event.pago()) && event.dataPagamento() != null) {
            parcial.put("data_pagamento", event.dataPagamento());
        }

        boolean atualizado = transacoesClient.atualizarTransacaoPorPedido(event.idPedido(), parcial);
        if (!atualizado) {
            log.warn(
                    "[PedidoAtualizadoEventListener] Falha ao atualizar transação de pedidoId={}. Tentando criar (fallback).",
                    event.idPedido());
            transacoesClient.criarTransacao(new TransacaoCreateRequest(
                    descricao,
                    tipoTransacao,
                    valor,
                    Boolean.TRUE.equals(event.pago()),
                    event.dataVencimento(),
                    Boolean.TRUE.equals(event.pago()) ? event.dataPagamento() : null,
                    event.idPedido()));
        }
    }

    private String mapTipoPedidoParaTipoTransacao(TipoPedido tipoPedido) {
        if (tipoPedido == null)
            return "Despesa"; // fallback
        return switch (tipoPedido) {
            case ENTRADA -> "Despesa"; // Entrada de itens representa saída de caixa
            case SAIDA -> "Receita"; // Saída de itens representa entrada de caixa
        };
    }

    private String buildDescricao(Long id, String numero) {
        String num = numero != null ? numero : "SEM-NUMERO";
        return "Pedido " + num + " (#" + id + ")";
    }
}
