package com.econome.pedidos.dto;

import com.econome.pedidos.enums.SituacaoPedido;
import com.econome.pedidos.enums.TipoPedido;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PedidoRequest(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") ZonedDateTime dataEmissaoPedido,
        @NotBlank(message = "numeroPedido é obrigatório") @Size(max = 100, message = "numeroPedido deve ter no máximo 100 caracteres") String numeroPedido,
        @NotNull(message = "tipoPedido é obrigatório") TipoPedido tipoPedido,
        @NotNull(message = "situacaoPedido é obrigatório") SituacaoPedido situacaoPedido,
        @NotNull(message = "valorTotal é obrigatório") @Positive(message = "valorTotal deve ser maior que zero") BigDecimal valorTotal
) {
}
