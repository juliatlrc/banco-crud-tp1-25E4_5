package org.example.bancocrud;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

// esse teste roda depois do deploy pra garantir que a aplicacao subiu certinho
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class SeleniumPostDeployTest {

    @Autowired
    private ContaService contaService;

    @Test
    void sistemaSobeFuncionando() {
        // verifica que o sistema inicializou e consegue executar operacoes basicas
        contaService.incluirContaDb("Teste Deploy", 100.0);
        var contas = contaService.consultarContasDb();
        assertFalse(contas.isEmpty(), "Sistema deve estar operacional apos o deploy");
    }
}
