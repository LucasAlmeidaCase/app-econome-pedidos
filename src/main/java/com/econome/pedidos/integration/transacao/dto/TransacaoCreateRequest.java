package com.econome.pedidos.integration.transacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Payload enviado ao microserviço de Transações (API Flask) para criação de uma
 * transação.
 */
public record TransacaoCreateRequest(
        String descricao,
        String tipo_transacao,
        BigDecimal valor,
        boolean pago,
        LocalDate data_vencimento,
        LocalDate data_pagamento,
        Long pedido_id) {
}
