package com.econome.pedidos.integration.transacao;

import com.econome.pedidos.enums.TipoPedido;

import java.time.LocalDate;

import java.math.BigDecimal;

/**
 * Evento de domínio publicado após a criação (persistência) de um Pedido.
 * É utilizado para acionar integrações assíncronas (ex: criação de transação financeira em outro microserviço)
 * respeitando o princípio de baixo acoplamento entre bounded contexts.
 */
public record PedidoCriadoEvent(
        Long idPedido,
        String numeroPedido,
        TipoPedido tipoPedido,
        BigDecimal valorTotal,
        boolean faturado,
        LocalDate dataVencimento,
        Boolean pago,
        LocalDate dataPagamento
) {
}

