package com.econome.pedidos.exception;

import java.time.OffsetDateTime;
import java.util.List;

public record ProblemDetails(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetails> fieldErrors
) {
    public record FieldErrorDetails(String field, String message) {
    }
}

