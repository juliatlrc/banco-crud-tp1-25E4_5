package org.example.banco.exception;

public class NomeInvalidoException extends RuntimeException {
    public NomeInvalidoException(String mensagem) {
        super(mensagem);
    }
}
