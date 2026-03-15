package org.example.banco.exception;

public class ContaNaoEncontradaException extends RuntimeException {
    public ContaNaoEncontradaException(Long id) {
        super("Conta não encontrada para o ID: " + id);
    }
}
