package org.example.banco;

import org.example.banco.exception.NomeInvalidoException;
import org.example.banco.exception.SaldoInvalidoException;
import org.example.banco.validation.ValidadorConta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class ValidadorContaTest {

    private ValidadorConta validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorConta();
    }

    // ─── validarNome ─────────────────────────────────────

    @Test
    @DisplayName("Deve aceitar nome válido")
    void deveAceitarNomeValido() {
        assertThatCode(() -> validador.validarNome("Maria Silva"))
            .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve rejeitar nomes inválidos")
    void deveRejeitarNomesInvalidos(String nome) {
        assertThatThrownBy(() -> validador.validarNome(nome))
            .isInstanceOf(NomeInvalidoException.class)
            .hasMessageContaining("vazio");
    }

    // ─── validarSaldo ────────────────────────────────────

    @Test
    @DisplayName("Deve aceitar saldo zero")
    void deveAceitarSaldoZero() {
        assertThatCode(() -> validador.validarSaldo(0.0))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve aceitar saldo positivo normal")
    void deveAceitarSaldoPositivo() {
        assertThatCode(() -> validador.validarSaldo(1500.00))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve rejeitar saldo nulo")
    void deveRejeitarSaldoNulo() {
        assertThatThrownBy(() -> validador.validarSaldo(null))
            .isInstanceOf(SaldoInvalidoException.class)
            .hasMessageContaining("nulo");
    }

    @ParameterizedTest
    @ValueSource(doubles = { -0.01, -1.0, -100.0, -999.99 })
    @DisplayName("Deve rejeitar saldo negativo")
    void deveRejeitarSaldoNegativo(double saldo) {
        assertThatThrownBy(() -> validador.validarSaldo(saldo))
            .isInstanceOf(SaldoInvalidoException.class)
            .hasMessageContaining("negativo");
    }

    @Test
    @DisplayName("Deve rejeitar saldo acima do limite")
    void deveRejeitarSaldoAcimaDoLimite() {
        assertThatThrownBy(() -> validador.validarSaldo(1_000_000_000.00))
            .isInstanceOf(SaldoInvalidoException.class)
            .hasMessageContaining("limite");
    }
}
