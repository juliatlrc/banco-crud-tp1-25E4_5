package org.example.banco.service;

import lombok.RequiredArgsConstructor;
import org.example.banco.entity.Conta;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.repository.ContaRepository;
import org.example.banco.validation.ValidadorConta;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Implementação das regras de negócio do sistema bancário.
 *
 * Aplica CQS: métodos que alteram estado (commands) separados
 * dos que apenas leem dados (queries).
 *
 * Validações ficam no ValidadorConta para não poluir o service.
 */
@Service
@RequiredArgsConstructor
public class ContaServiceImpl implements ContaServicePort {

    private final ContaRepository contaRepository;
    private final ValidadorConta validador;

    // ─── COMMANDS ────────────────────────────────────────

    @Override
    public void incluirConta(String nome, Double saldo) {
        validador.validarNome(nome);
        validador.validarSaldo(saldo);
        contaRepository.save(new Conta(null, nome.trim(), saldo));
    }

    @Override
    public void alterarConta(Long id, String nome, Double saldo) {
        validador.validarNome(nome);
        validador.validarSaldo(saldo);
        Conta existente = buscarOuFalhar(id);
        contaRepository.save(existente.comDados(nome.trim(), saldo));
    }

    @Override
    public void excluirConta(Long id) {
        buscarOuFalhar(id);
        contaRepository.deleteById(id);
    }

    // ─── QUERIES ─────────────────────────────────────────

    @Override
    public Conta consultarPorId(Long id) {
        return buscarOuFalhar(id);
    }

    @Override
    public List<Conta> consultarTodas() {
        return Collections.unmodifiableList(contaRepository.findAll());
    }

    @Override
    public List<Conta> consultarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return consultarTodas();
        }
        return Collections.unmodifiableList(
            contaRepository.findByNomeContainingIgnoreCase(nome.trim())
        );
    }

    // ─── AUXILIAR ────────────────────────────────────────

    private Conta buscarOuFalhar(Long id) {
        return contaRepository.findById(id)
            .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }
}
