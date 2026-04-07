package org.example.banco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object da tela de listagem de contas (/contas).
 * Encapsula todos os elementos e ações disponíveis nessa página.
 */
public class PaginaListaContas extends PaginaBase {

    @FindBy(id = "btn-nova-conta")
    private WebElement botaoNovaConta;

    @FindBy(id = "tabela-contas")
    private WebElement tabelaContas;

    @FindBy(id = "sem-contas")
    private WebElement mensagemSemContas;

    @FindBy(id = "mensagem-sucesso")
    private WebElement mensagemSucesso;

    public PaginaListaContas(WebDriver driver) {
        super(driver);
    }

    public void abrir() {
        driver.get(BASE_URL + "/contas");
    }

    public PaginaFormulario clicarNovaConta() {
        botaoNovaConta.click();
        return new PaginaFormulario(driver);
    }

    /**
     * Retorna todas as linhas da tabela (exceto o cabeçalho).
     */
    public List<WebElement> linhasDaTabela() {
        return driver.findElements(By.cssSelector("#tabela-contas tbody tr"));
    }

    public boolean tabelaEstaVisivel() {
        return !driver.findElements(By.id("tabela-contas")).isEmpty();
    }

    public boolean mensagemVaziaEstaVisivel() {
        return !driver.findElements(By.id("sem-contas")).isEmpty();
    }

    public String textoDaMensagemSucesso() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensagem-sucesso")));
        return mensagemSucesso.getText();
    }

    /**
     * Clica no botão Editar da linha correspondente ao índice (0 = primeiro).
     */
    public PaginaFormulario clicarEditarNaLinha(int indice) {
        List<WebElement> linhas = linhasDaTabela();
        linhas.get(indice).findElement(By.cssSelector(".btn-editar")).click();
        return new PaginaFormulario(driver);
    }

    /**
     * Clica em Excluir na linha do índice informado e confirma o alert.
     */
    public void clicarExcluirNaLinha(int indice) {
        List<WebElement> linhas = linhasDaTabela();
        linhas.get(indice).findElement(By.cssSelector(".btn-excluir")).click();
        driver.switchTo().alert().accept();
    }

    public int quantidadeDeLinhas() {
        return linhasDaTabela().size();
    }

    public String nomeDaLinha(int indice) {
        return linhasDaTabela().get(indice)
                .findElement(By.cssSelector(".nome-conta")).getText();
    }

    public String saldoDaLinha(int indice) {
        return linhasDaTabela().get(indice)
                .findElement(By.cssSelector(".saldo-conta")).getText();
    }
}
