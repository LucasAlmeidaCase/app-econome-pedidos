package com.econome.pedidos.integration.transacao.event;

import com.econome.pedidos.enums.TipoPedido;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Evento de domínio publicado após atualização de Pedido.
 * Indica estado anterior e atual de faturamento para decidir entre criar ou
 * atualizar transação.
 */
public record PedidoAtualizadoEvent(
        Long idPedido,
        String numeroPedido,
        TipoPedido tipoPedido,
        BigDecimal valorTotal,
        boolean faturadoAtual,
        boolean faturadoAnterior,
        LocalDate dataVencimento,
        Boolean pago,
        LocalDate dataPagamento) {
}
