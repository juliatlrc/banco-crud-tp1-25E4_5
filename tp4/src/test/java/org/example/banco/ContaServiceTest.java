package org.example.banco;

import org.example.banco.entity.Conta;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.exception.NomeInvalidoException;
import org.example.banco.exception.SaldoInvalidoException;
import org.example.banco.repository.ContaRepository;
import org.example.banco.service.ContaServiceImpl;
import org.example.banco.validation.ValidadorConta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Spy
    private ValidadorConta validador;

    @InjectMocks
    private ContaServiceImpl contaService;

    private Conta contaExemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contaExemplo = new Conta(1L, "Maria", 500.00);
    }

    // ─── incluirConta ────────────────────────────────────

    @Test
    @DisplayName("Deve criar conta com dados válidos")
    void deveIncluirContaComDadosValidos() {
        contaService.incluirConta("Maria", 500.00);
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve aceitar saldo zero")
    void deveAceitarSaldoZero() {
        contaService.incluirConta("João", 0.00);
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    @DisplayName("Deve lançar exceção com nome inválido")
    void deveLancarExcecaoNomeInvalido(String nome) {
        assertThatThrownBy(() -> contaService.incluirConta(nome, 100.00))
            .isInstanceOf(NomeInvalidoException.class);
        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção com saldo nulo")
    void deveLancarExcecaoSaldoNulo() {
        assertThatThrownBy(() -> contaService.incluirConta("Ana", null))
            .isInstanceOf(SaldoInvalidoException.class)
            .hasMessageContaining("nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção com saldo negativo")
    void deveLancarExcecaoSaldoNegativo() {
        assertThatThrownBy(() -> contaService.incluirConta("Ana", -1.00))
            .isInstanceOf(SaldoInvalidoException.class)
            .hasMessageContaining("negativo");
    }

    @Test
    @DisplayName("Deve lançar exceção com saldo acima do limite")
    void deveLancarExcecaoSaldoAcimaDoLimite() {
        assertThatThrownBy(() -> contaService.incluirConta("Ana", 1_000_000_000.00))
            .isInstanceOf(SaldoInvalidoException.class)
            .hasMessageContaining("limite");
    }

    // ─── consultarTodas ──────────────────────────────────

    @Test
    @DisplayName("Deve retornar todas as contas")
    void deveRetornarTodasAsContas() {
        when(contaRepository.findAll()).thenReturn(List.of(contaExemplo));

        List<Conta> resultado = contaService.consultarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há contas")
    void deveRetornarListaVazia() {
        when(contaRepository.findAll()).thenReturn(List.of());
        assertThat(contaService.consultarTodas()).isEmpty();
    }

    @Test
    @DisplayName("Lista retornada deve ser imutável")
    void listaDeveSerImutavel() {
        when(contaRepository.findAll()).thenReturn(List.of(contaExemplo));

        List<Conta> resultado = contaService.consultarTodas();

        assertThatThrownBy(() -> resultado.add(new Conta(2L, "Teste", 100.0)))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    // ─── consultarPorId ──────────────────────────────────

    @Test
    @DisplayName("Deve retornar conta existente pelo ID")
    void deveRetornarContaPorId() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExemplo));

        Conta resultado = contaService.consultarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar ID inexistente")
    void deveLancarExcecaoIdInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.consultarPorId(99L))
            .isInstanceOf(ContaNaoEncontradaException.class)
            .hasMessageContaining("99");
    }

    // ─── alterarConta ────────────────────────────────────

    @Test
    @DisplayName("Deve alterar nome e saldo com sucesso")
    void deveAlterarContaComSucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExemplo));

        contaService.alterarConta(1L, "Maria Atualizada", 1000.00);

        verify(contaRepository).save(argThat(c ->
            c.getSaldo().equals(1000.00) && c.getNome().equals("Maria Atualizada")));
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar com saldo negativo")
    void deveLancarExcecaoAlterarSaldoNegativo() {
        assertThatThrownBy(() -> contaService.alterarConta(1L, "Maria", -50.00))
            .isInstanceOf(SaldoInvalidoException.class);
        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar conta inexistente")
    void deveLancarExcecaoAlterarInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.alterarConta(99L, "X", 100.00))
            .isInstanceOf(ContaNaoEncontradaException.class);
    }

    // ─── excluirConta ────────────────────────────────────

    @Test
    @DisplayName("Deve excluir conta existente")
    void deveExcluirContaExistente() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExemplo));

        contaService.excluirConta(1L);

        verify(contaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir ID inexistente")
    void deveLancarExcecaoExcluirInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.excluirConta(99L))
            .isInstanceOf(ContaNaoEncontradaException.class);
        verify(contaRepository, never()).deleteById(any());
    }

    // ─── fuzz ────────────────────────────────────────────

    @ParameterizedTest
    @ValueSource(doubles = { -0.01, -1.0, -999.99 })
    @DisplayName("Fuzz: saldos negativos sempre rejeitados")
    void fuzzSaldosNegativos(double saldo) {
        assertThatThrownBy(() -> contaService.incluirConta("Teste", saldo))
            .isInstanceOf(SaldoInvalidoException.class);
    }
}
