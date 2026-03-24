package org.example.banco.service;

import lombok.RequiredArgsConstructor;
import org.example.banco.entity.Conta;
import org.example.banco.entity.ContaForm;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.exception.SaldoInvalidoException;
import org.example.banco.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Regras de negócio do sistema bancário.
 * Aplica CQS: commands (alteram estado) separados de queries (apenas leitura).
 */
@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    // -------------------------
    //  COMMANDS
    // -------------------------

    public void incluirConta(ContaForm form) {
        validarSaldo(form.getSaldo());
        Conta nova = new Conta(null, form.getNome().trim(), form.getSaldo());
        contaRepository.save(nova);
    }

    public void alterarConta(Long id, ContaForm form) {
        validarSaldo(form.getSaldo());
        Conta existente = buscarPorId(id);
        Conta atualizada = new Conta(existente.getId(), form.getNome().trim(), form.getSaldo());
        contaRepository.save(atualizada);
    }

    public void excluirConta(Long id) {
        buscarPorId(id);
        contaRepository.deleteById(id);
    }

    // -------------------------
    //  QUERIES
    // -------------------------

    public List<Conta> consultarTodas() {
        return Collections.unmodifiableList(contaRepository.findAll());
    }

    public List<Conta> consultarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return consultarTodas();
        }
        return Collections.unmodifiableList(
                contaRepository.findByNomeContainingIgnoreCase(nome.trim()));
    }

    public Conta consultarPorId(Long id) {
        return buscarPorId(id);
    }

    // -------------------------
    //  Auxiliares privados
    // -------------------------

    private Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }

    private void validarSaldo(Double saldo) {
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
