package org.example.banco;

import org.example.banco.entity.Conta;
import org.example.banco.entity.ContaForm;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.exception.SaldoInvalidoException;
import org.example.banco.repository.ContaRepository;
import org.example.banco.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    @DisplayName("Deve criar conta com dados válidos")
    void deveIncluirContaComDadosValidos() {
        contaService.incluirConta(new ContaForm("Maria", 500.00));
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve aceitar saldo zero ao criar conta")
    void deveAceitarSaldoZero() {
        contaService.incluirConta(new ContaForm("João", 0.00));
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo for nulo")
    void deveLancarExcecaoSaldoNulo() {
        assertThatThrownBy(() -> contaService.incluirConta(new ContaForm("Ana", null)))
                .isInstanceOf(SaldoInvalidoException.class)
                .hasMessageContaining("nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo for negativo")
    void deveLancarExcecaoSaldoNegativo() {
        assertThatThrownBy(() -> contaService.incluirConta(new ContaForm("Ana", -1.00)))
                .isInstanceOf(SaldoInvalidoException.class)
                .hasMessageContaining("negativo");
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo exceder o limite")
    void deveLancarExcecaoSaldoAcimaDoLimite() {
        assertThatThrownBy(() -> contaService.incluirConta(new ContaForm("Ana", 1_000_000_000.00)))
                .isInstanceOf(SaldoInvalidoException.class)
                .hasMessageContaining("limite");
    }

    // -----------------------------------------------
    //  consultarTodas
    // -----------------------------------------------

    @Test
    @DisplayName("Deve retornar lista com todas as contas")
    void deveRetornarListaDeContas() {
        when(contaRepository.findAll()).thenReturn(List.of(contaExemplo));

        List<Conta> resultado = contaService.consultarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há contas")
    void deveRetornarListaVaziaSeNaoHaContas() {
        when(contaRepository.findAll()).thenReturn(List.of());
        assertThat(contaService.consultarTodas()).isEmpty();
    }

    @Test
    @DisplayName("A lista retornada deve ser imutável")
    void listaRetornadaDeveSerImutavel() {
        when(contaRepository.findAll()).thenReturn(List.of(contaExemplo));

        List<Conta> resultado = contaService.consultarTodas();

        assertThatThrownBy(() -> resultado.add(new Conta(2L, "Teste", 100.0)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // -----------------------------------------------
    //  consultarPorId
    // -----------------------------------------------

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
    void deveLancarExcecaoAoConsultarIdInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.consultarPorId(99L))
                .isInstanceOf(ContaNaoEncontradaException.class)
                .hasMessageContaining("99");
    }

    // -----------------------------------------------
    //  alterarConta
    // -----------------------------------------------

    @Test
    @DisplayName("Deve alterar dados de conta existente")
    void deveAlterarContaComSucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExemplo));

        contaService.alterarConta(1L, new ContaForm("Maria Atualizada", 1000.00));

        verify(contaRepository).save(argThat(c ->
                c.getSaldo().equals(1000.00) && c.getNome().equals("Maria Atualizada")));
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar conta com saldo negativo")
    void deveLancarExcecaoAlterarSaldoNegativo() {
        assertThatThrownBy(() -> contaService.alterarConta(1L, new ContaForm("Maria", -50.00)))
                .isInstanceOf(SaldoInvalidoException.class);
        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar conta inexistente")
    void deveLancarExcecaoAlterarContaInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.alterarConta(99L, new ContaForm("X", 200.00)))
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

    // -----------------------------------------------
    //  Testes parametrizados e fuzz
    // -----------------------------------------------

    @ParameterizedTest
    @CsvSource({
        "Alice, 100.0",
        "Bob Carlos, 0.0",
        "Fernanda de Souza, 999.99"
    })
    @DisplayName("Deve criar corretamente com várias combinações válidas")
    void deveCriarVariasCombinacoes(String nome, double saldo) {
        assertThatCode(() -> contaService.incluirConta(new ContaForm(nome, saldo)))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(doubles = { -0.01, -1.0, -999.99 })
    @DisplayName("Fuzz: saldos negativos devem sempre ser rejeitados")
    void fuzzSaldosNegativos(double saldo) {
        assertThatThrownBy(() -> contaService.incluirConta(new ContaForm("Teste", saldo)))
                .isInstanceOf(SaldoInvalidoException.class);
    }

    @Test
    @DisplayName("Simulação de falha no banco: deve propagar a exceção")
    void simulaFalhaNoBanco() {
        when(contaRepository.save(any())).thenThrow(new RuntimeException("Timeout de conexão"));

        assertThatThrownBy(() -> contaService.incluirConta(new ContaForm("Maria", 100.0)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Timeout");
    }
}
