package org.example.banco.exception;

public class SaldoInvalidoException extends RuntimeException {

    public SaldoInvalidoException(String msg) {
        super(msg);
    }
}
