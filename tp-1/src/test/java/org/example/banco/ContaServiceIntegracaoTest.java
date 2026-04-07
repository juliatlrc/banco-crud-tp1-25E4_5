package org.example.banco;

import org.example.banco.entity.Conta;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.service.ContaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de integração que executam operações reais no banco H2.
 */
@SpringBootTest
@Transactional
class ContaServiceIntegracaoTest {

    @Autowired
    private ContaService contaService;

    @Test
    @DisplayName("Deve persistir e recuperar conta corretamente")
    void devePersistirERecuperarConta() {
        contaService.incluirConta("Felipe", 750.00);

        List<Conta> contas = contaService.consultarTodasContas();

        assertThat(contas).anyMatch(c ->
            c.getNome().equals("Felipe") && c.getSaldo().equals(750.00)
        );
    }

    @Test
    @DisplayName("Deve atualizar saldo no banco de dados")
    void deveAtualizarSaldoNoBanco() {
        contaService.incluirConta("Carla", 200.00);
        Conta carla = contaService.consultarTodasContas().stream()
                .filter(c -> c.getNome().equals("Carla"))
                .findFirst()
                .orElseThrow();

        contaService.alterarSaldo(carla.getId(), 999.00);

        Conta atualizada = contaService.consultarConta(carla.getId());
        assertThat(atualizada.getSaldo()).isEqualTo(999.00);
    }

    @Test
    @DisplayName("Deve remover conta do banco de dados")
    void deveRemoverContaDoBanco() {
        contaService.incluirConta("Pedro", 100.00);
        Conta pedro = contaService.consultarTodasContas().stream()
                .filter(c -> c.getNome().equals("Pedro"))
                .findFirst()
                .orElseThrow();

        contaService.excluirConta(pedro.getId());

        assertThatThrownBy(() -> contaService.consultarConta(pedro.getId()))
                .isInstanceOf(ContaNaoEncontradaException.class);
    }
}
