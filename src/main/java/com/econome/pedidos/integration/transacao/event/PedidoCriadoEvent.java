package com.econome.pedidos.integration.transacao.event;

import com.econome.pedidos.enums.TipoPedido;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Evento de domínio publicado após a criação (persistência) de um Pedido.
 * Utilizado para acionar integração assíncrona de criação de transação
 * financeira.
 */
public record PedidoCriadoEvent(
        Long idPedido,
        String numeroPedido,
        TipoPedido tipoPedido,
        BigDecimal valorTotal,
        boolean faturado,
        LocalDate dataVencimento,
        Boolean pago,
        LocalDate dataPagamento,
        Long participanteId) {
}
