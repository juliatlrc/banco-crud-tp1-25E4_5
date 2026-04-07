package org.example.banco;

import org.example.banco.entity.Conta;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.exception.SaldoInvalidoException;
import org.example.banco.repository.ContaRepository;
import org.example.banco.service.ContaServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ContaServiceIntegracaoTest {

    @Autowired
    private ContaServicePort contaService;

    @Autowired
    private ContaRepository contaRepository;

    @BeforeEach
    void limparBanco() {
        contaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve persistir e recuperar conta corretamente")
    void devePersistirERecuperar() {
        contaService.incluirConta("Felipe", 750.00);

        List<Conta> contas = contaService.consultarTodas();

        assertThat(contas).anyMatch(c ->
            c.getNome().equals("Felipe") && c.getSaldo().equals(750.00));
    }

    @Test
    @DisplayName("Deve atualizar nome e saldo no banco")
    void deveAtualizarNoBanco() {
        contaService.incluirConta("Carla", 200.00);
        Conta carla = contaService.consultarTodas().stream()
            .filter(c -> c.getNome().equals("Carla"))
            .findFirst().orElseThrow();

        contaService.alterarConta(carla.getId(), "Carla Atualizada", 999.00);

        Conta atualizada = contaService.consultarPorId(carla.getId());
        assertThat(atualizada.getNome()).isEqualTo("Carla Atualizada");
        assertThat(atualizada.getSaldo()).isEqualTo(999.00);
    }

    @Test
    @DisplayName("Deve remover conta do banco")
    void deveRemoverDoBanco() {
        contaService.incluirConta("Pedro", 100.00);
        Conta pedro = contaService.consultarTodas().stream()
            .filter(c -> c.getNome().equals("Pedro"))
            .findFirst().orElseThrow();

        contaService.excluirConta(pedro.getId());

        assertThatThrownBy(() -> contaService.consultarPorId(pedro.getId()))
            .isInstanceOf(ContaNaoEncontradaException.class);
    }

    @Test
    @DisplayName("Deve listar múltiplas contas")
    void deveListarVariasContas() {
        contaService.incluirConta("Conta A", 100.0);
        contaService.incluirConta("Conta B", 200.0);
        contaService.incluirConta("Conta C", 300.0);

        assertThat(contaService.consultarTodas()).hasSize(3);
    }

    @Test
    @DisplayName("Deve buscar por nome parcial ignorando maiúsculas")
    void deveBuscarPorNomeParcial() {
        contaService.incluirConta("Maria Alice", 100.0);
        contaService.incluirConta("João", 200.0);
        contaService.incluirConta("maria Clara", 300.0);

        List<Conta> resultado = contaService.consultarPorNome("maria");

        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("Deve rejeitar saldo negativo sem persistir no banco")
    void deveRejeitarSaldoNegativoSemPersistir() {
        assertThatThrownBy(() -> contaService.incluirConta("Teste", -50.0))
            .isInstanceOf(SaldoInvalidoException.class);

        assertThat(contaRepository.count()).isZero();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há contas")
    void deveRetornarListaVazia() {
        assertThat(contaService.consultarTodas()).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar lista imutável")
    void deveRetornarListaImutavel() {
        contaService.incluirConta("Ana", 100.0);

        List<Conta> lista = contaService.consultarTodas();

        assertThatThrownBy(() -> lista.add(new Conta(99L, "Invasor", 0.0)))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
