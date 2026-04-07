package org.example.banco.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Classe base para todos os Page Objects.
 * Centraliza o driver e o wait para evitar repetição.
 */
public abstract class PaginaBase {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected static final String BASE_URL = "http://localhost:8080";

    protected PaginaBase(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }
}
