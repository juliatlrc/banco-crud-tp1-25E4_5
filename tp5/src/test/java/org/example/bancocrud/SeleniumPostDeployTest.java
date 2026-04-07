package org.example.bancocrud;

import org.example.bancocrud.service.ContaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
class SeleniumPostDeployTest {

    @Autowired
    private ContaService contaService;

    @Test
    void sistemaSobeFuncionando() {
        contaService.incluirContaDb("Teste Deploy", 100.0);
        var contas = contaService.consultarContasDb();
        assertFalse(contas.isEmpty(), "Sistema deve estar operacional apos o deploy");
    }
}