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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ContaServiceIntegracaoTest {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ContaRepository contaRepository;

    @BeforeEach
    void limparBanco() {
        contaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve persistir e recuperar conta corretamente")
    void devePersistirERecuperarConta() {
        contaService.incluirConta(new ContaForm("Felipe", 750.00));

        List<Conta> contas = contaService.consultarTodas();

        assertThat(contas).anyMatch(c ->
                c.getNome().equals("Felipe") && c.getSaldo().equals(750.00));
    }

    @Test
    @DisplayName("Deve atualizar dados no banco de dados")
    void deveAtualizarDadosNoBanco() {
        contaService.incluirConta(new ContaForm("Carla", 200.00));
        Conta carla = contaService.consultarTodas().stream()
                .filter(c -> c.getNome().equals("Carla"))
                .findFirst()
                .orElseThrow();

        contaService.alterarConta(carla.getId(), new ContaForm("Carla Atualizada", 999.00));

        Conta atualizada = contaService.consultarPorId(carla.getId());
        assertThat(atualizada.getNome()).isEqualTo("Carla Atualizada");
        assertThat(atualizada.getSaldo()).isEqualTo(999.00);
    }

    @Test
    @DisplayName("Deve remover conta do banco de dados")
    void deveRemoverContaDoBanco() {
        contaService.incluirConta(new ContaForm("Pedro", 100.00));
        Conta pedro = contaService.consultarTodas().stream()
                .filter(c -> c.getNome().equals("Pedro"))
                .findFirst()
                .orElseThrow();

        contaService.excluirConta(pedro.getId());

        assertThatThrownBy(() -> contaService.consultarPorId(pedro.getId()))
                .isInstanceOf(ContaNaoEncontradaException.class);
    }

    @Test
    @DisplayName("Deve listar múltiplas contas criadas em sequência")
    void deveListarVariasContas() {
        contaService.incluirConta(new ContaForm("Conta A", 100.0));
        contaService.incluirConta(new ContaForm("Conta B", 200.0));
        contaService.incluirConta(new ContaForm("Conta C", 300.0));

        assertThat(contaService.consultarTodas()).hasSize(3);
    }

    @Test
    @DisplayName("Deve buscar por nome parcial ignorando maiúsculas")
    void deveBuscarPorNomeParcial() {
        contaService.incluirConta(new ContaForm("Maria Alice", 100.0));
        contaService.incluirConta(new ContaForm("João", 200.0));
        contaService.incluirConta(new ContaForm("maria Clara", 300.0));

        List<Conta> resultado = contaService.consultarPorNome("maria");
        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("Deve rejeitar saldo negativo sem persistir no banco")
    void deveRejeitarSaldoNegativoSemPersistir() {
        assertThatThrownBy(() -> contaService.incluirConta(new ContaForm("Teste", -50.0)))
                .isInstanceOf(SaldoInvalidoException.class);
        assertThat(contaRepository.count()).isZero();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há contas")
    void deveRetornarListaVaziaQuandoSemContas() {
        assertThat(contaService.consultarTodas()).isEmpty();
    }
}
