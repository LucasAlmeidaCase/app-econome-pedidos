package com.econome.pedidos.exception;

/**
 * Exceção de domínio lançada quando um Pedido não é encontrado.
 * É uma exceção unchecked para simplificar o fluxo de controle na camada de serviço/API.
 */
public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(Long id) {
        super("Pedido não encontrado. ID=" + id);
    }
}
