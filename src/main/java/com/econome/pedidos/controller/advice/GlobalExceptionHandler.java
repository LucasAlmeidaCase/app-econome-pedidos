package com.econome.pedidos.controller.advice;

import com.econome.pedidos.exception.PedidoNaoEncontradoException;
import com.econome.pedidos.exception.ProblemDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Handler global de exceções da API.
 * Centraliza a conversão de exceções do domínio e de validação em respostas HTTP padronizadas
 * usando o payload ProblemDetails.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Tradução para 404 quando um pedido não é encontrado.
     */
    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetails> handlePedidoNaoEncontrado(PedidoNaoEncontradoException ex, HttpServletRequest req) {
        return buildProblem(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), req.getRequestURI(), List.of());
    }

    /**
     * Erros de validação de parâmetros e tipos incorretos (400).
     */
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ProblemDetails> handleValidation(Exception ex, HttpServletRequest req) {
        return buildProblem(HttpStatus.BAD_REQUEST, "Requisição inválida", ex.getMessage(), req.getRequestURI(), List.of());
    }

    /**
     * Erros não tratados (500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGeneric(Exception ex, HttpServletRequest req) {
        return buildProblem(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", ex.getMessage(), req.getRequestURI(), List.of());
    }

    /**
     * Erros de validação de body (@Valid) com detalhes por campo.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        List<ProblemDetails.FieldErrorDetails> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ProblemDetails.FieldErrorDetails(fe.getField(), resolveMessage(fe)))
                .toList();
        ProblemDetails body = new ProblemDetails(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                "Erros de validação encontrados",
                path,
                errors
        );
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    private String resolveMessage(FieldError fe) {
        if (fe.getDefaultMessage() != null && !fe.getDefaultMessage().isBlank()) {
            return fe.getDefaultMessage();
        }
        return String.format("Campo '%s' inválido", fe.getField());
    }

    private ResponseEntity<ProblemDetails> buildProblem(HttpStatus status,
                                                        String error,
                                                        String message,
                                                        String path,
                                                        List<ProblemDetails.FieldErrorDetails> fieldErrors) {
        ProblemDetails body = new ProblemDetails(
                OffsetDateTime.now(),
                status.value(),
                error,
                message,
                path,
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}
