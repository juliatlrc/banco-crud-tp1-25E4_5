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
 * Nenhum detalhe interno é exposto — fail gracefully.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleNaoEncontrada(ContaNaoEncontradaException ex) {
        return erro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({SaldoInvalidoException.class, NomeInvalidoException.class})
    public ResponseEntity<Map<String, String>> handleNegocio(RuntimeException ex) {
        return erro(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidacao(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getDefaultMessage())
            .findFirst()
            .orElse("Dados inválidos.");
        return erro(HttpStatus.BAD_REQUEST, msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenerico(Exception ex) {
        return erro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor.");
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        Map<String, String> corpo = new HashMap<>();
        corpo.put("erro", mensagem);
        return ResponseEntity.status(status).body(corpo);
    }
}
