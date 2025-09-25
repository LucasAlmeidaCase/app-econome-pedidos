package com.econome.pedidos.controller;

import com.econome.pedidos.dto.PedidoRequest;
import com.econome.pedidos.dto.PedidoResponse;
import com.econome.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;

/**
 * Controller REST responsável por expor os endpoints do contexto de "Pedidos".
 */
@RestController
@RequestMapping("/api/pedidos")
@Validated
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Operações de CRUD para o recurso Pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Lista todos os pedidos.
     *
     * @return lista de pedidos no formato de resposta.
     */
    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Retorna a lista de todos os pedidos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<PedidoResponse> listar() {
        return pedidoService.listarTodos();
    }

    /**
     * Busca um pedido pelo identificador.
     *
     * @param id identificador do pedido
     * @return pedido encontrado ou 404 se não existir
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna o pedido correspondente ao ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    public PedidoResponse buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    /**
     * Cria um novo pedido.
     *
     * @param request dados do pedido a ser criado
     * @return pedido criado com cabeçalho Location apontando para o novo recurso
     */
    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse criado = pedidoService.criar(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    /**
     * Atualiza um pedido existente.
     *
     * @param id      identificador do pedido a ser atualizado
     * @param request dados do pedido
     * @return pedido atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    public PedidoResponse atualizar(@PathVariable Long id, @Valid @RequestBody PedidoRequest request) {
        return pedidoService.atualizar(id, request);
    }

    /**
     * Exclui um pedido.
     *
     * @param id identificador do pedido a ser excluído
     * @return 204 em caso de sucesso
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pedido", description = "Remove um pedido pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
