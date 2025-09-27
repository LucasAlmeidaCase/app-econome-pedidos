package com.econome.pedidos.integration.transacao.listener;

import com.econome.pedidos.integration.transacao.client.TransacoesClient;
import com.econome.pedidos.integration.transacao.dto.TransacaoCreateRequest;
import com.econome.pedidos.integration.transacao.event.PedidoAtualizadoEvent;
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

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(TransacoesClient.class)
public class PedidoAtualizadoEventListener {

    private final TransacoesClient transacoesClient;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPedidoAtualizado(PedidoAtualizadoEvent event) {
        if (!event.faturadoAtual()) {
            log.debug("[PedidoAtualizadoEventListener] Pedido {} não FATURADO após update.", event.idPedido());
            return;
        }

        String descricao = buildDescricao(event.idPedido(), event.numeroPedido());
        String tipoTransacao = mapTipoPedido(event.tipoPedido());
        BigDecimal valor = Objects.requireNonNullElse(event.valorTotal(), BigDecimal.ZERO);

        if (!event.faturadoAnterior()) {
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

        boolean ok = transacoesClient.atualizarTransacaoPorPedido(event.idPedido(), parcial);
        if (!ok) {
            log.warn("[PedidoAtualizadoEventListener] Update falhou. Tentando criar (fallback) pedidoId={}",
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

    private String mapTipoPedido(TipoPedido tipoPedido) {
        if (tipoPedido == null)
            return "Despesa";
        // Regras de mapeamento atuais:
        // Pedido ENTRADA (compra) gera transação de Despesa.
        // Pedido SAIDA (venda) gera transação de Receita.
        return switch (tipoPedido) {
            case ENTRADA -> "Despesa";
            case SAIDA -> "Receita";
        };
    }

    private String buildDescricao(Long id, String numero) {
        String num = numero != null ? numero : "SEM-NUMERO";
        return "Pedido " + num + " (#" + id + ")";
    }
}
