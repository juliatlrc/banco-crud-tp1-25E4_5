package org.example.banco.tests;

import org.example.banco.pages.PaginaFormulario;
import org.example.banco.pages.PaginaListaContas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes Selenium para as operações CRUD da interface web.
 * Utiliza Page Object Model para organizar as interações.
 *
 * Pré-requisito: a aplicação deve estar rodando em http://localhost:8080
 */
class ContaCrudSeleniumTest extends TesteBase {

    private PaginaListaContas paginaLista;

    @BeforeEach
    void abrirPagina() {
        paginaLista = new PaginaListaContas(driver);
        paginaLista.abrir();
    }

    // -----------------------------------------------
    //  CREATE
    // -----------------------------------------------

    @Test
    @DisplayName("Deve cadastrar nova conta e exibir na listagem")
    void deveCadastrarNovaConta() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formulario.salvar("Maria Teste", "750.00");

        assertThat(lista.quantidadeDeLinhas()).isGreaterThan(0);
        assertThat(lista.nomeDaLinha(0)).isEqualTo("Maria Teste");
    }

    @Test
    @DisplayName("Deve exibir mensagem de sucesso após cadastro")
    void deveExibirMensagemSucessoAposCadastro() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formulario.salvar("João Sucesso", "100.00");

        assertThat(lista.textoDaMensagemSucesso()).contains("sucesso");
    }

    @Test
    @DisplayName("Deve exibir erro ao tentar salvar com nome vazio")
    void deveExibirErroNomeVazio() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        formulario.preencherNome("");
        formulario.preencherSaldo("100.00");
        formulario.clicarSalvar();

        assertThat(formulario.erroNomeEstaVisivel()).isTrue();
        assertThat(formulario.textoErroNome()).isNotBlank();
    }

    @Test
    @DisplayName("Deve exibir erro ao tentar salvar com saldo negativo")
    void deveExibirErroSaldoNegativo() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        formulario.preencherNome("Teste Negativo");
        formulario.preencherSaldo("-50");
        formulario.clicarSalvar();

        assertThat(formulario.erroSaldoEstaVisivel()).isTrue();
        assertThat(formulario.textoErroSaldo()).isNotBlank();
    }

    @Test
    @DisplayName("Deve aceitar saldo zero como valor válido")
    void deveAceitarSaldoZero() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formulario.salvar("Conta Zero", "0");

        assertThat(lista.quantidadeDeLinhas()).isGreaterThan(0);
    }

    // -----------------------------------------------
    //  READ
    // -----------------------------------------------

    @Test
    @DisplayName("Deve exibir mensagem quando não há contas cadastradas")
    void deveExibirMensagemListaVazia() {
        // banco H2 inicia vazio a cada execução
        assertThat(paginaLista.mensagemVaziaEstaVisivel()).isTrue();
    }

    @Test
    @DisplayName("Deve exibir tabela após cadastrar conta")
    void deveExibirTabelaAposCadastro() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formulario.salvar("Ana Listagem", "200.00");

        assertThat(lista.tabelaEstaVisivel()).isTrue();
    }

    // -----------------------------------------------
    //  UPDATE
    // -----------------------------------------------

    @Test
    @DisplayName("Deve editar nome e saldo de uma conta existente")
    void deveEditarConta() {
        // Cria a conta primeiro
        PaginaFormulario formularioCriacao = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formularioCriacao.salvar("Nome Antigo", "100.00");

        // Clica em editar e altera os dados
        PaginaFormulario formularioEdicao = lista.clicarEditarNaLinha(0);
        PaginaListaContas listaAtualizada = formularioEdicao.salvar("Nome Novo", "999.00");

        assertThat(listaAtualizada.nomeDaLinha(0)).isEqualTo("Nome Novo");
    }

    @Test
    @DisplayName("Deve pré-preencher formulário de edição com dados atuais")
    void devePreencherFormularioEdicaoComDadosAtuais() {
        PaginaFormulario formularioCriacao = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formularioCriacao.salvar("Carlos PreFill", "500.00");

        PaginaFormulario formularioEdicao = lista.clicarEditarNaLinha(0);

        assertThat(formularioEdicao.valorCampoNome()).isEqualTo("Carlos PreFill");
    }

    @Test
    @DisplayName("Deve exibir erro ao tentar editar com nome vazio")
    void deveExibirErroEdicaoNomeVazio() {
        PaginaFormulario formularioCriacao = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formularioCriacao.salvar("Para Editar", "300.00");

        PaginaFormulario formularioEdicao = lista.clicarEditarNaLinha(0);
        formularioEdicao.preencherNome("");
        formularioEdicao.clicarSalvar();

        assertThat(formularioEdicao.erroNomeEstaVisivel()).isTrue();
    }

    // -----------------------------------------------
    //  DELETE
    // -----------------------------------------------

    @Test
    @DisplayName("Deve excluir conta e remover da listagem")
    void deveExcluirConta() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formulario.salvar("Para Excluir", "50.00");

        int antes = lista.quantidadeDeLinhas();
        lista.clicarExcluirNaLinha(0);

        assertThat(lista.quantidadeDeLinhas()).isEqualTo(antes - 1);
    }

    @Test
    @DisplayName("Deve exibir mensagem de sucesso após excluir conta")
    void deveExibirMensagemSucessoAposExclusao() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formulario.salvar("Excluir Sucesso", "10.00");

        lista.clicarExcluirNaLinha(0);

        assertThat(lista.textoDaMensagemSucesso()).contains("sucesso");
    }

    @Test
    @DisplayName("Deve exibir aviso de lista vazia após excluir última conta")
    void deveVoltarParaListaVaziaAposExcluirUnica() {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formulario.salvar("Ultima Conta", "1.00");

        lista.clicarExcluirNaLinha(0);

        assertThat(lista.mensagemVaziaEstaVisivel()).isTrue();
    }
}
