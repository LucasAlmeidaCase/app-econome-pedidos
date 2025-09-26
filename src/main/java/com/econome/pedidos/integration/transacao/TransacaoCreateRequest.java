package com.econome.pedidos.integration.transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Payload enviado ao microserviço de Transações (API Flask) para criação de uma transação.
 * Os campos seguem o contrato exposto pelo endpoint POST /transacao da API Python.
 */
public record TransacaoCreateRequest(
        String descricao,
        String tipo_transacao, // RECEITA ou DESPESA
        BigDecimal valor,
        boolean pago,
        LocalDate data_vencimento,
        LocalDate data_pagamento,
        Long pedido_id
) {
}

