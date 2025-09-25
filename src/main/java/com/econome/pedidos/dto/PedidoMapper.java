package com.econome.pedidos.dto;

import com.econome.domain.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper do contexto de Pedidos baseado em MapStruct.
 * Responsável por converter entre DTOs e entidade JPA sem adicionar lógica de negócio.
 */
@Mapper(componentModel = "spring")
public interface PedidoMapper {

    /**
     * Converte os dados de requisição em uma nova entidade Pedido.
     */
    Pedido toEntity(PedidoRequest request);

    /**
     * Aplica os dados de requisição em uma entidade existente (atualização parcial total).
     */
    void updateEntityFromRequest(PedidoRequest request, @MappingTarget Pedido entity);

    /**
     * Converte a entidade para o DTO de resposta público da API.
     */
    PedidoResponse toResponse(Pedido entity);

    /**
     * Converte uma lista de entidades para a lista correspondente de DTOs de resposta.
     */
    List<PedidoResponse> toResponseList(List<Pedido> entities);
}