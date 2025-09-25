package com.econome.pedidos.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Situação do pedido")
public enum SituacaoPedido {
    FATURADO, CANCELADO, PENDENTE
}
