package org.example.banco.repository;

import org.example.banco.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Acesso ao banco para a entidade Conta.
 * Métodos básicos herdados do JpaRepository.
 * findByNomeContainingIgnoreCase usado pelo filtro ?nome= da API.
 */
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByNomeContainingIgnoreCase(String nome);
}
