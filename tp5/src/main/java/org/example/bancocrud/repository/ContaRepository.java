package org.example.bancocrud.repository;

import org.example.bancocrud.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

// o JpaRepository ja traz tudo que preciso: save, findById, findAll, deleteById
// nao precisei escrever nenhum SQL aqui
public interface ContaRepository extends JpaRepository<Conta, Long> {
}
