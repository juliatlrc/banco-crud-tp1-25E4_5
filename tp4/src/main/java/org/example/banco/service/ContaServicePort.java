package org.example.banco.service;

import org.example.banco.entity.Conta;

import java.util.List;

/**
 * Contrato do serviço bancário.
 * Criado no TP4 para desacoplar controller e testes da implementação concreta.
 */
public interface ContaServicePort {

    // --- commands ---
    void incluirConta(String nome, Double saldo);
    void alterarConta(Long id, String nome, Double saldo);
    void excluirConta(Long id);

    // --- queries ---
    Conta consultarPorId(Long id);
    List<Conta> consultarTodas();
    List<Conta> consultarPorNome(String nome);
}
