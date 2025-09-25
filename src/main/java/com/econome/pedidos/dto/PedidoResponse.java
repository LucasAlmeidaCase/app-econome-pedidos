package com.econome.pedidos.dto;

import com.econome.pedidos.enums.SituacaoPedido;
import com.econome.pedidos.enums.TipoPedido;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PedidoResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") ZonedDateTime dataEmissaoPedido,
        String numeroPedido,
        TipoPedido tipoPedido,
        SituacaoPedido situacaoPedido,
        BigDecimal valorTotal
) {
}
