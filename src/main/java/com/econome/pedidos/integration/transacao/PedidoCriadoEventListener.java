package com.econome.pedidos.integration.transacao;

import com.econome.pedidos.enums.TipoPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Listener responsável por reagir ao {@link PedidoCriadoEvent} e acionar a criação de uma transação
 * no microserviço financeiro (Transações) após o commit da transação JPA do Pedido.
 * Implementa o padrão Domain Event + Eventual Consistency.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(TransacoesClient.class)
public class PedidoCriadoEventListener {

    private final TransacoesClient transacoesClient;

    /**
     * Processa o evento somente após o commit da transação (garantindo que o Pedido foi persistido).
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPedidoCriado(PedidoCriadoEvent event) {
        if (!event.faturado()) {
            log.debug("[PedidoCriadoEventListener] Pedido {} não está FATURADO. Nenhuma transação será criada.", event.idPedido());
            return;
        }

        String tipoTransacao = mapTipoPedidoParaTipoTransacao(event.tipoPedido());
        String descricao = buildDescricao(event);
        BigDecimal valor = Objects.requireNonNullElse(event.valorTotal(), BigDecimal.ZERO);

        TransacaoCreateRequest request = new TransacaoCreateRequest(
                descricao,
                tipoTransacao,
                valor,
                Boolean.TRUE.equals(event.pago()),
                event.dataVencimento(),
                Boolean.TRUE.equals(event.pago()) ? event.dataPagamento() : null,
                event.idPedido()
        );

        log.debug("[PedidoCriadoEventListener] Disparando criação de transação (FATURADO) para pedidoId={} descricao='{}' tipoTransacao='{}' valor={} vencimento={} pago={} pagamento={}",
                event.idPedido(), descricao, tipoTransacao, valor, event.dataVencimento(), event.pago(), event.dataPagamento());
        transacoesClient.criarTransacao(request);
    }

    private String mapTipoPedidoParaTipoTransacao(TipoPedido tipoPedido) {
        if (tipoPedido == null) return "Despesa"; // fallback
        return switch (tipoPedido) {
            case ENTRADA -> "Despesa"; // Pedido de entrada gera saída de caixa
            case SAIDA -> "Receita";  // Pedido de saída gera entrada de caixa
        };
    }

    private String buildDescricao(PedidoCriadoEvent event) {
        String numero = event.numeroPedido() != null ? event.numeroPedido() : "SEM-NUMERO";
        return "Pedido " + numero + " (#" + event.idPedido() + ")";
    }
}
