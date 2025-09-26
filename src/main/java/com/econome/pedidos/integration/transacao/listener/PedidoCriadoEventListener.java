package com.econome.pedidos.integration.transacao.listener;

import com.econome.pedidos.integration.transacao.client.TransacoesClient;
import com.econome.pedidos.integration.transacao.dto.TransacaoCreateRequest;
import com.econome.pedidos.integration.transacao.event.PedidoCriadoEvent;
import com.econome.pedidos.enums.TipoPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(TransacoesClient.class)
public class PedidoCriadoEventListener {

    private final TransacoesClient transacoesClient;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPedidoCriado(PedidoCriadoEvent event) {
        if (!event.faturado()) {
            log.debug("[PedidoCriadoEventListener] Pedido {} não FATURADO. Nenhuma transação.", event.idPedido());
            return;
        }

        String tipoTransacao = mapTipoPedido(event.tipoPedido());
        String descricao = buildDescricao(event.idPedido(), event.numeroPedido());
        BigDecimal valor = Objects.requireNonNullElse(event.valorTotal(), BigDecimal.ZERO);

        transacoesClient.criarTransacao(new TransacaoCreateRequest(
                descricao,
                tipoTransacao,
                valor,
                Boolean.TRUE.equals(event.pago()),
                event.dataVencimento(),
                Boolean.TRUE.equals(event.pago()) ? event.dataPagamento() : null,
                event.idPedido()));
    }

    private String mapTipoPedido(TipoPedido tipoPedido) {
        if (tipoPedido == null)
            return "Despesa";
        return switch (tipoPedido) {
            case ENTRADA -> "Despesa";
            case SAIDA -> "Receita";
        };
    }

    private String buildDescricao(Long id, String numero) {
        String num = numero != null ? numero : "SEM-NUMERO";
        return "Pedido " + num + " (#" + id + ")";
    }
}
