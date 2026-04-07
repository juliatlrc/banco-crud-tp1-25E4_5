package org.example.banco.service;

import lombok.RequiredArgsConstructor;
import org.example.banco.entity.Conta;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.exception.NomeInvalidoException;
import org.example.banco.exception.SaldoInvalidoException;
import org.example.banco.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Serviço responsável pelas regras de negócio do sistema bancário.
 *
 * Aplica CQS: métodos que alteram estado (commands) são separados
 * dos métodos que apenas consultam dados (queries).
 */
@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    // -------------------------
    //  COMMANDS (alteram estado)
    // -------------------------

    /**
     * Cria e persiste uma nova conta bancária.
     *
     * @param nome  nome do titular (não pode ser nulo ou vazio)
     * @param saldo saldo inicial (não pode ser negativo)
     * @throws NomeInvalidoException  se o nome for inválido
     * @throws SaldoInvalidoException se o saldo for inválido
     */
    public void incluirConta(String nome, Double saldo) {
        validarNome(nome);
        validarSaldo(saldo);

        Conta novaConta = new Conta(null, nome.trim(), saldo);
        contaRepository.save(novaConta);
    }

    /**
     * Atualiza o saldo de uma conta existente.
     * Usa imutabilidade: cria nova instância com o saldo atualizado.
     *
     * @param id        identificador da conta
     * @param novoSaldo novo saldo a ser aplicado (não pode ser negativo)
     * @throws ContaNaoEncontradaException se a conta não existir
     * @throws SaldoInvalidoException      se o saldo for inválido
     */
    public void alterarSaldo(Long id, Double novoSaldo) {
        validarSaldo(novoSaldo);

        Conta conta = buscarContaPorId(id);
        Conta contaAtualizada = conta.comSaldo(novoSaldo);
        contaRepository.save(contaAtualizada);
    }

    /**
     * Remove uma conta pelo ID informado.
     *
     * @param id identificador da conta a remover
     * @throws ContaNaoEncontradaException se a conta não existir
     */
    public void excluirConta(Long id) {
        // Verifica existência antes de excluir
        buscarContaPorId(id);
        contaRepository.deleteById(id);
    }

    // -------------------------
    //  QUERIES (apenas leitura)
    // -------------------------

    /**
     * Retorna todas as contas cadastradas.
     * Retorna lista vazia se não houver registros.
     *
     * @return lista imutável de contas
     */
    public List<Conta> consultarTodasContas() {
        List<Conta> contas = contaRepository.findAll();
        return Collections.unmodifiableList(contas);
    }

    /**
     * Busca uma conta pelo seu ID.
     *
     * @param id identificador da conta
     * @return conta encontrada
     * @throws ContaNaoEncontradaException se não existir conta com o ID
     */
    public Conta consultarConta(Long id) {
        return buscarContaPorId(id);
    }

    // -------------------------
    //  Métodos auxiliares privados
    // -------------------------

    private Conta buscarContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new NomeInvalidoException("O nome do titular não pode ser vazio.");
        }
    }

    private void validarSaldo(Double saldo) {
        if (saldo == null) {
            throw new SaldoInvalidoException("O saldo não pode ser nulo.");
        }
        if (saldo < 0) {
            throw new SaldoInvalidoException("O saldo não pode ser negativo: " + saldo);
        }
    }
}
