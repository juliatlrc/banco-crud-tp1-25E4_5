package org.example.banco.validation;

import org.example.banco.exception.NomeInvalidoException;
import org.example.banco.exception.SaldoInvalidoException;
import org.springframework.stereotype.Component;

/**
 * Centraliza as validações de negócio da conta.
 * Extraído no TP4 para eliminar duplicação entre os módulos anteriores.
 */
@Component
public class ValidadorConta {

    public void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new NomeInvalidoException("O nome do titular não pode ser vazio.");
        }
    }

    public void validarSaldo(Double saldo) {
        if (saldo == null) {
            throw new SaldoInvalidoException("O saldo não pode ser nulo.");
        }
        if (saldo < 0) {
            throw new SaldoInvalidoException("O saldo não pode ser negativo: " + saldo);
        }
        if (saldo > 999_999_999.99) {
            throw new SaldoInvalidoException("O saldo excede o limite permitido.");
        }
    }
}
