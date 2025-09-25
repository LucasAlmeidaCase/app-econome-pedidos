package com.econome.pedidos.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum que representa a situação atual de um Pedido.
 */
@Schema(description = "Situação do pedido")
public enum SituacaoPedido {
    FATURADO, CANCELADO, PENDENTE
}
