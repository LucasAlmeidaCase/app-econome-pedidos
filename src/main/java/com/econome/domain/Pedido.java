package com.econome.domain;

import com.econome.pedidos.enums.SituacaoPedido;
import com.econome.pedidos.enums.TipoPedido;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "pedidos")
public class Pedido implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_emissao_pedido")
    private ZonedDateTime dataEmissaoPedido;

    @Column(name = "numero_pedido")
    private String numeroPedido;

    @Column(name = "tipo_pedido")
    @Enumerated(EnumType.STRING)
    private TipoPedido tipoPedido;

    @Column(name = "situacao_pedido")
    @Enumerated(EnumType.STRING)
    private SituacaoPedido situacaoPedido;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

}
