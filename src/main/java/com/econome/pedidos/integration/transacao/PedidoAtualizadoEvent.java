package com.econome.pedidos.integration.transacao;

import com.econome.pedidos.enums.TipoPedido;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Evento de domínio publicado após a atualização de um Pedido.
 * Permite integração assíncrona para criação ou atualização da transação
 * financeira vinculada.
 * Contém informação se anteriormente o pedido já estava faturado para decidir
 * entre criar ou atualizar.
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
