package com.econome.pedidos.dto;

import com.econome.domain.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper do contexto de Pedidos baseado em MapStruct.
 * Responsável por converter entre DTOs e entidade JPA sem adicionar lógica de negócio.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMapper {

    /**
     * Converte os dados de requisição em uma nova entidade Pedido.
     */
    @Mapping(target = "id", ignore = true)
    Pedido toEntity(PedidoRequest request);

    /**
     * Aplica os dados de requisição em uma entidade existente (atualização parcial total).
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(PedidoRequest request, @MappingTarget Pedido entity);

    /**
     * Converte a entidade para o DTO de resposta público da API.
     * Campo participanteResumo é enriquecido posteriormente (ignorado aqui).
     */
    @Mapping(target = "participanteResumo", ignore = true)
    PedidoResponse toResponse(Pedido entity);

    /**
     * Converte uma lista de entidades para a lista correspondente de DTOs de resposta.
     */
    @Mapping(target = "participanteResumo", ignore = true)
    List<PedidoResponse> toResponseList(List<Pedido> entities);

    // Nota: participanteId é mapeado automaticamente por possuir o mesmo nome em DTO e entidade.
}