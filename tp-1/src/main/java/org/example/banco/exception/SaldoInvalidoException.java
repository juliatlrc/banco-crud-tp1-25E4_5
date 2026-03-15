package org.example.banco.exception;

/**
 * Lançada quando um valor de saldo inválido é fornecido
 * (ex: nulo ou negativo).
 */
public class SaldoInvalidoException extends RuntimeException {

    public SaldoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
