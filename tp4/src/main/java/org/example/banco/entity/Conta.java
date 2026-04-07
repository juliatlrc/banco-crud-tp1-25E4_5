package org.example.banco.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma conta bancária.
 * Sem setters — alterações criam nova instância (imutabilidade).
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

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @DecimalMin("0.0")
    @Column(nullable = false)
    private Double saldo;

    // Atualiza só o saldo (mantém o nome)
    public Conta comSaldo(Double novoSaldo) {
        return new Conta(this.id, this.nome, novoSaldo);
    }

    // Atualiza nome e saldo juntos
    public Conta comDados(String novoNome, Double novoSaldo) {
        return new Conta(this.id, novoNome, novoSaldo);
    }
}
