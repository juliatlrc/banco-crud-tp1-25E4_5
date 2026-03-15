package org.example.banco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object do formulário de criação e edição de conta.
 * Serve para as rotas /contas/nova e /contas/{id}/editar.
 */
public class PaginaFormulario extends PaginaBase {

    @FindBy(id = "nome")
    private WebElement campoNome;

    @FindBy(id = "saldo")
    private WebElement campoSaldo;

    @FindBy(id = "btn-salvar")
    private WebElement botaoSalvar;

    public PaginaFormulario(WebDriver driver) {
        super(driver);
    }

    public void abrirFormularioNovo() {
        driver.get(BASE_URL + "/contas/nova");
    }

    public void preencherNome(String nome) {
        campoNome.clear();
        campoNome.sendKeys(nome);
    }

    public void preencherSaldo(String saldo) {
        campoSaldo.clear();
        campoSaldo.sendKeys(saldo);
    }

    /**
     * Preenche nome e saldo e envia o formulário.
     */
    public PaginaListaContas salvar(String nome, String saldo) {
        preencherNome(nome);
        preencherSaldo(saldo);
        botaoSalvar.click();
        return new PaginaListaContas(driver);
    }

    public void clicarSalvar() {
        botaoSalvar.click();
    }

    public boolean erroNomeEstaVisivel() {
        return !driver.findElements(By.id("erro-nome")).isEmpty();
    }

    public boolean erroSaldoEstaVisivel() {
        return !driver.findElements(By.id("erro-saldo")).isEmpty();
    }

    public String textoErroNome() {
        return driver.findElement(By.id("erro-nome")).getText();
    }

    public String textoErroSaldo() {
        return driver.findElement(By.id("erro-saldo")).getText();
    }

    public String valorCampoNome() {
        return campoNome.getAttribute("value");
    }

    public String urlAtual() {
        return driver.getCurrentUrl();
    }
}
