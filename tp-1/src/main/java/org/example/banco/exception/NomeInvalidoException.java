package org.example.banco.exception;

/**
 * Lançada quando um nome inválido é fornecido
 * (ex: nulo ou vazio).
 */
public class NomeInvalidoException extends RuntimeException {

    public NomeInvalidoException(String mensagem) {
        super(mensagem);
    }
}
