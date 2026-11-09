package com.raposo.gerenciador_autores_obras.api.controller;

import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.BusinessException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.ConflictException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.ResourceNotFoundException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.UnauthorizedException;
import com.raposo.gerenciador_autores_obras.infrastructure.record.ErroResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                               HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErroResponseDTO> handleConflictException(ConflictException ex,
                                                          HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponseDTO> handleBusinessException(BusinessException ex,
                                                                HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErroResponseDTO> handleUnauthorizedException(UnauthorizedException ex,
                                                               HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidacao(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request) {
        Map<String, String> erros = new LinkedHashMap<>();
        // putIfAbsent: se um campo falhar em duas regras (@NotBlank e @Email), guarda só a primeira
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> erros.putIfAbsent(erro.getField(), erro.getDefaultMessage()));

        return build(HttpStatus.BAD_REQUEST, "Dados inválidos", request, erros);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErroResponseDTO> handleValidacaoParametros(HandlerMethodValidationException ex,
                                                                     HttpServletRequest request) {
        String mensagem = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("; "));

        return build(HttpStatus.BAD_REQUEST, mensagem, request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> handleCorpoInvalido(HttpMessageNotReadableException ex,
                                                               HttpServletRequest request) {
        // Não repassa ex.getMessage(): ele expõe nomes internos de classes
        return build(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido ou mal formatado", request, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponseDTO> handleTipoInvalido(MethodArgumentTypeMismatchException ex,
                                                              HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro '" + ex.getName() + "'", request, null);
    }

    // ===== Banco de dados =====

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponseDTO> handleIntegridade(DataIntegrityViolationException ex,
                                                             HttpServletRequest request) {
        log.warn("Violação de integridade em {}", request.getRequestURI(), ex);
        return build(HttpStatus.CONFLICT,
                "Operação viola a integridade dos dados (valor duplicado ou registro em uso)",
                request, null);
    }

    // ===== Qualquer outra coisa =====

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGenerico(Exception ex, HttpServletRequest request) {

        // Exceções padrão do Spring MVC (405, rota inexistente, parâmetro ausente...)
        // já carregam o status correto. Sem este if, o "Exception" as transformaria em 500.
        if (ex instanceof ErrorResponse erroSpring) {
            HttpStatus status = HttpStatus.valueOf(erroSpring.getStatusCode().value());
            String detalhe = Objects.requireNonNullElse(
                    erroSpring.getBody().getDetail(), status.getReasonPhrase());
            return build(status, detalhe, request, null);
        }

        // O erro real vai para o LOG (para você investigar), nunca para o cliente
        log.error("Erro inesperado em {} {}", request.getMethod(), request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", request, null);
    }


    private ResponseEntity<ErroResponseDTO> build(HttpStatus status, String mensagem,
                                                  HttpServletRequest request,
                                                  Map<String, String> erros) {
        ErroResponseDTO corpo = ErroResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(mensagem)
                .path(request.getRequestURI())
                .erros(erros)
                .build();

        return ResponseEntity.status(status).body(corpo);
    }
}
