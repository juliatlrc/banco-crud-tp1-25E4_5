package org.example.banco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Centraliza o tratamento de erros da API.
 * Garante que nenhum detalhe interno seja exposto ao usuário (fail gracefully).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleNaoEncontrada(ContaNaoEncontradaException ex) {
        return erro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(SaldoInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleSaldoInvalido(SaldoInvalidoException ex) {
        return erro(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos.");
        return erro(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenerico(Exception ex) {
        // Não expõe mensagem interna — apenas informa que houve erro
        return erro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor.");
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        Map<String, String> corpo = new HashMap<>();
        corpo.put("erro", mensagem);
        return ResponseEntity.status(status).body(corpo);
    }
}
