package org.example.banco.tests;

import org.example.banco.pages.PaginaFormulario;
import org.example.banco.pages.PaginaListaContas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes parametrizados para validar diferentes entradas na interface web.
 * Cada linha do CsvSource representa um cenário de teste independente.
 */
class ContaParametrizadoSeleniumTest extends TesteBase {

    private PaginaListaContas paginaLista;

    @BeforeEach
    void abrirPagina() {
        paginaLista = new PaginaListaContas(driver);
        paginaLista.abrir();
    }

    @ParameterizedTest(name = "Cadastrar conta: nome={0}, saldo={1}")
    @CsvSource({
        "Maria Silva, 500.00",
        "João Souza, 0.00",
        "Ana Lima,   1500.50",
        "Pedro Costa, 999.99"
    })
    @DisplayName("Deve cadastrar contas com diferentes nomes e saldos válidos")
    void deveCadastrarContasVariadas(String nome, String saldo) {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        PaginaListaContas lista = formulario.salvar(nome.trim(), saldo.trim());

        assertThat(lista.quantidadeDeLinhas()).isGreaterThan(0);
        assertThat(lista.nomeDaLinha(0)).isEqualTo(nome.trim());
    }

    @ParameterizedTest(name = "Saldo inválido: {0}")
    @ValueSource(strings = {"-1", "-0.01", "-100", "-999.99"})
    @DisplayName("Deve rejeitar saldos negativos no formulário")
    void deveRejeitarSaldosNegativos(String saldoInvalido) {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        formulario.preencherNome("Teste");
        formulario.preencherSaldo(saldoInvalido);
        formulario.clicarSalvar();

        assertThat(formulario.erroSaldoEstaVisivel()).isTrue();
    }

    @ParameterizedTest(name = "Nome inválido: \"{0}\"")
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Deve rejeitar nomes vazios ou só espaços")
    void deveRejeitarNomesVazios(String nomeInvalido) {
        PaginaFormulario formulario = paginaLista.clicarNovaConta();
        formulario.preencherNome(nomeInvalido);
        formulario.preencherSaldo("100.00");
        formulario.clicarSalvar();

        assertThat(formulario.erroNomeEstaVisivel()).isTrue();
    }
}
