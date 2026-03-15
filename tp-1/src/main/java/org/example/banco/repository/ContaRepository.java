package org.example.banco.repository;

import org.example.banco.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pelas operações de banco de dados
 * relacionadas à entidade Conta.
 *
 * Herda automaticamente os métodos: save, findById, findAll, deleteById, etc.
 */
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
}
