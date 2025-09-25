package com.econome.pedidos.service;

import com.econome.pedidos.dto.PedidoRequest;
import com.econome.pedidos.dto.PedidoResponse;

import java.util.List;

/**
 * Contrato de serviço para o contexto de Pedidos.
 * Responsável por encapsular regras de negócio e coordenar acesso ao repositório.
 * Segue SOLID (SRP e DIP), é exposto como interface para favorecer testabilidade e extensibilidade.
 */
public interface PedidoService {

    /**
     * Cria um novo pedido a partir dos dados informados.
     *
     * @param request dados do pedido (validados no nível da API)
     * @return dados do pedido criado
     */
    PedidoResponse criar(PedidoRequest request);

    /**
     * Atualiza um pedido existente.
     *
     * @param id      identificador do pedido
     * @param request dados a atualizar
     * @return dados do pedido atualizado
     * @throws com.econome.pedidos.exception.PedidoNaoEncontradoException se o pedido não existir
     */
    PedidoResponse atualizar(Long id, PedidoRequest request);

    /**
     * Busca um pedido pelo identificador.
     *
     * @param id identificador do pedido
     * @return dados do pedido encontrado
     * @throws com.econome.pedidos.exception.PedidoNaoEncontradoException se o pedido não existir
     */
    PedidoResponse buscarPorId(Long id);

    /**
     * Lista todos os pedidos.
     *
     * @return lista de pedidos
     */
    List<PedidoResponse> listarTodos();

    /**
     * Exclui um pedido pelo identificador.
     *
     * @param id identificador do pedido
     * @throws com.econome.pedidos.exception.PedidoNaoEncontradoException se o pedido não existir
     */
    void excluir(Long id);
}
