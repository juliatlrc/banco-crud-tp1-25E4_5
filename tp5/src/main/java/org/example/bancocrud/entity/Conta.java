package org.example.bancocrud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// refatorei pra tirar os setters, agora a conta so pode ser criada pelo construtor
// isso evita alguem mudar o id ou nome sem querer la no service
@Entity
@Table(name = "conta")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double saldo;
}
