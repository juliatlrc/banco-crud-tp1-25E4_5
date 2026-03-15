package org.example.banco.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Classe base dos testes Selenium.
 * Configura e fecha o WebDriver automaticamente para cada teste.
 */
public abstract class TesteBase {

    protected WebDriver driver;

    @BeforeAll
    static void configurarDriver() {
        // WebDriverManager baixa o ChromeDriver compatível automaticamente
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void iniciarDriver() {
        ChromeOptions opcoes = new ChromeOptions();
        opcoes.addArguments("--headless");       // roda sem abrir janela
        opcoes.addArguments("--no-sandbox");
        opcoes.addArguments("--disable-dev-shm-usage");
        opcoes.addArguments("--window-size=1280,800");

        driver = new ChromeDriver(opcoes);
    }

    @AfterEach
    void fecharDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
