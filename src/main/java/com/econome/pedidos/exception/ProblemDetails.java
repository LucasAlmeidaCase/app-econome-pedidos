package com.econome.pedidos.exception;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Payload de erro padronizado para respostas HTTP.
 * Contém timestamp, status HTTP, mensagem, caminho da requisição e detalhes de erros de campo
 * (quando aplicável). Mantém consistência e facilita o consumo pelos clientes da API.
 */
public record ProblemDetails(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetails> fieldErrors
) {
    /**
     * Detalhe de erro de validação em nível de campo.
     */
    public record FieldErrorDetails(String field, String message) {
    }
}
