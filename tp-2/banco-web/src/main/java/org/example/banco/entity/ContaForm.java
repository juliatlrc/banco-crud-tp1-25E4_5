package org.example.banco.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado nos formulários web.
 * Separado da entidade para não expor JPA ao Thymeleaf diretamente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaForm {

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O saldo é obrigatório.")
    @DecimalMin(value = "0.0", message = "O saldo não pode ser negativo.")
    private Double saldo;
}
