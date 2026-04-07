package org.example.banco.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Representa uma conta bancária.
 * Campos são imutáveis por fora — sem setters expostos.
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

    @NotBlank(message = "O nome não pode ser vazio.")
    @Column(nullable = false)
    private String nome;

    @DecimalMin(value = "0.0", message = "O saldo não pode ser negativo.")
    @Column(nullable = false)
    private Double saldo;

    public Conta comSaldo(Double novoSaldo) {
        return new Conta(this.id, this.nome, novoSaldo);
    }
}
