package org.example.banco;

import org.example.banco.entity.Conta;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.exception.NomeInvalidoException;
import org.example.banco.exception.SaldoInvalidoException;
import org.example.banco.repository.ContaRepository;
import org.example.banco.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    private Conta contaExemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contaExemplo = new Conta(1L, "Maria", 500.00);
    }

    // -----------------------------------------------
    //  incluirConta
    // -----------------------------------------------

    @Test
    @DisplayName("Deve criar conta com nome e saldo válidos")
    void deveIncluirContaComDadosValidos() {
        contaService.incluirConta("Maria", 500.00);
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve aceitar saldo zero ao criar conta")
    void deveAceitarSaldoZero() {
        contaService.incluirConta("João", 0.00);
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    @DisplayName("Deve lançar exceção quando nome for inválido")
    void deveLancarExcecaoNomeInvalido(String nome) {
        assertThatThrownBy(() -> contaService.incluirConta(nome, 100.00))
                .isInstanceOf(NomeInvalidoException.class);
        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo for nulo")
    void deveLancarExcecaoSaldoNulo() {
        assertThatThrownBy(() -> contaService.incluirConta("Ana", null))
                .isInstanceOf(SaldoInvalidoException.class)
                .hasMessageContaining("nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo for negativo")
    void deveLancarExcecaoSaldoNegativo() {
        assertThatThrownBy(() -> contaService.incluirConta("Ana", -1.00))
                .isInstanceOf(SaldoInvalidoException.class)
                .hasMessageContaining("negativo");
    }

    // -----------------------------------------------
    //  consultarTodasContas
    // -----------------------------------------------

    @Test
    @DisplayName("Deve retornar lista com todas as contas")
    void deveRetornarListaDeContas() {
        when(contaRepository.findAll()).thenReturn(List.of(contaExemplo));

        List<Conta> resultado = contaService.consultarTodasContas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há contas")
    void deveRetornarListaVaziaSeNaoHaContas() {
        when(contaRepository.findAll()).thenReturn(List.of());

        List<Conta> resultado = contaService.consultarTodasContas();

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("A lista retornada deve ser imutável")
    void listaRetornadaDeveSerImutavel() {
        when(contaRepository.findAll()).thenReturn(List.of(contaExemplo));

        List<Conta> resultado = contaService.consultarTodasContas();

        assertThatThrownBy(() -> resultado.add(new Conta(2L, "Teste", 100.0)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // -----------------------------------------------
    //  consultarConta
    // -----------------------------------------------

    @Test
    @DisplayName("Deve retornar conta existente pelo ID")
    void deveRetornarContaPorId() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExemplo));

        Conta resultado = contaService.consultarConta(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar ID inexistente")
    void deveLancarExcecaoAoConsultarIdInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.consultarConta(99L))
                .isInstanceOf(ContaNaoEncontradaException.class)
                .hasMessageContaining("99");
    }

    // -----------------------------------------------
    //  alterarSaldo
    // -----------------------------------------------

    @Test
    @DisplayName("Deve alterar saldo de conta existente")
    void deveAlterarSaldoComSucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExemplo));

        contaService.alterarSaldo(1L, 1000.00);

        verify(contaRepository).save(argThat(c -> c.getSaldo().equals(1000.00)));
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar saldo com valor negativo")
    void deveLancarExcecaoAoAlterarSaldoNegativo() {
        assertThatThrownBy(() -> contaService.alterarSaldo(1L, -50.00))
                .isInstanceOf(SaldoInvalidoException.class);
        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar conta inexistente")
    void deveLancarExcecaoAoAlterarContaInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.alterarSaldo(99L, 200.00))
                .isInstanceOf(ContaNaoEncontradaException.class);
    }

    // -----------------------------------------------
    //  excluirConta
    // -----------------------------------------------

    @Test
    @DisplayName("Deve excluir conta existente com sucesso")
    void deveExcluirContaExistente() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExemplo));

        contaService.excluirConta(1L);

        verify(contaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir ID inexistente")
    void deveLancarExcecaoAoExcluirIdInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.excluirConta(99L))
                .isInstanceOf(ContaNaoEncontradaException.class);
        verify(contaRepository, never()).deleteById(any());
    }
}
