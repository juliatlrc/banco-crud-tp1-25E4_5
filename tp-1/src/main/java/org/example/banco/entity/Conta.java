package org.example.banco.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Representa uma conta bancária no sistema.
 * Cada conta possui um titular e um saldo associado.
 */
@Entity
@Table(name = "conta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double saldo;

    /**
     * Cria uma cópia da conta com o novo saldo.
     * Garante imutabilidade: nenhum campo é alterado diretamente.
     */
    public Conta comSaldo(Double novoSaldo) {
        return new Conta(this.id, this.nome, novoSaldo);
    }
}
