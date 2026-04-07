package org.example.banco.exception;

/**
 * Lançada quando uma conta não é encontrada pelo ID informado.
 */
public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(Long id) {
        super("Conta não encontrada para o ID: " + id);
    }
}
