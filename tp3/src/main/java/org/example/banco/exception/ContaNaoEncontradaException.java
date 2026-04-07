package org.example.banco.exception;

public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(Long id) {
        super("Conta com id " + id + " não encontrada");
    }
}
