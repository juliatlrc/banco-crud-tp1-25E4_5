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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SeleniumPostDeployTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    void iniciarDriver() {
        ChromeOptions options = new ChromeOptions();
        // modo headless pra rodar no github actions sem precisar de tela
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @Test
    void aplicacaoDeveEstarOnlineAposODeploy() {
        driver.get("http://localhost:" + port + "/actuator/health");
        String resposta = driver.findElement(By.tagName("body")).getText();
        // se aparece UP, o sistema subiu certinho
        assertTrue(resposta.contains("UP"), "A aplicacao deveria estar UP apos o deploy");
    }

    @AfterEach
    void fecharDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
