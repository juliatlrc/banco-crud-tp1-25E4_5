package org.example.banco.repository;

import org.example.banco.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Acesso ao banco de dados para a entidade Conta.
 * Métodos básicos são herdados do JpaRepository.
 */
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByNomeContainingIgnoreCase(String nome);
}
