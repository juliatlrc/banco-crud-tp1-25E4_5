package org.example.bancocrud;

import org.example.bancocrud.entity.Conta;
import org.example.bancocrud.repository.ContaRepository;
import org.example.bancocrud.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ContaServiceTest {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ContaRepository contaRepository;

    // limpa o banco antes de cada teste pra nao ter interferencia
    @BeforeEach
    void limpar() {
        contaRepository.deleteAll();
    }

    @Test
    void deveIncluirConta() {
        contaService.incluirContaDb("Ana", 300.0);

        List<Conta> contas = contaService.consultarContasDb();
        assertEquals(1, contas.size());
        assertEquals("Ana", contas.get(0).getNome());
        assertEquals(300.0, contas.get(0).getSaldo());
    }

    @Test
    void deveListarTodasAsContas() {
        contaService.incluirContaDb("Ana", 100.0);
        contaService.incluirContaDb("Carlos", 200.0);

        List<Conta> contas = contaService.consultarContasDb();
        assertEquals(2, contas.size());
    }

    @Test
    void deveBuscarContaPorId() {
        contaService.incluirContaDb("Bruno", 150.0);
        Long id = contaService.consultarContasDb().get(0).getId();

        Conta encontrada = contaService.consultarContaDb(id);
        assertEquals("Bruno", encontrada.getNome());
    }

    @Test
    void deveAlterarSaldoDaConta() {
        contaService.incluirContaDb("Bruno", 100.0);
        Long id = contaService.consultarContasDb().get(0).getId();

        contaService.alterarConta(id, 500.0);

        Conta atualizada = contaService.consultarContaDb(id);
        assertEquals(500.0, atualizada.getSaldo());
        // nome nao pode ter mudado
        assertEquals("Bruno", atualizada.getNome());
    }

    @Test
    void deveExcluirConta() {
        contaService.incluirContaDb("Carlos", 200.0);
        Long id = contaService.consultarContasDb().get(0).getId();

        contaService.excluirContaDb(id);

        assertTrue(contaService.consultarContasDb().isEmpty());
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoExiste() {
        // nao existe nenhuma conta com id 9999
        assertThrows(RuntimeException.class,
                () -> contaService.consultarContaDb(9999L));
    }

    @Test
    void deveLancarExcecaoAoExcluirContaInexistente() {
        assertThrows(RuntimeException.class,
                () -> contaService.excluirContaDb(9999L));
    }
}
