package org.example.banco;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.banco.entity.Conta;
import org.example.banco.entity.ContaForm;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.exception.SaldoInvalidoException;
import org.example.banco.service.ContaServicePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = org.example.banco.controller.ContaController.class)
class ContaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ContaServicePort contaService;

    @Autowired
    private ObjectMapper mapper;

    // ─── GET /api/contas ─────────────────────────────────

    @Test
    @DisplayName("GET /api/contas deve retornar lista de contas")
    void deveListarContas() throws Exception {
        when(contaService.consultarPorNome(null))
            .thenReturn(List.of(new Conta(1L, "Ana", 100.0)));

        mvc.perform(get("/api/contas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nome").value("Ana"));
    }

    @Test
    @DisplayName("GET /api/contas?nome=Ana deve filtrar por nome")
    void deveFiltrarPorNome() throws Exception {
        when(contaService.consultarPorNome("Ana"))
            .thenReturn(List.of(new Conta(1L, "Ana", 100.0)));

        mvc.perform(get("/api/contas").param("nome", "Ana"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/contas deve retornar 200 com lista vazia")
    void deveRetornarListaVazia() throws Exception {
        when(contaService.consultarPorNome(null)).thenReturn(List.of());

        mvc.perform(get("/api/contas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    // ─── GET /api/contas/{id} ────────────────────────────

    @Test
    @DisplayName("GET /api/contas/1 deve retornar conta existente")
    void deveBuscarPorId() throws Exception {
        when(contaService.consultarPorId(1L))
            .thenReturn(new Conta(1L, "Pedro", 500.0));

        mvc.perform(get("/api/contas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Pedro"));
    }

    @Test
    @DisplayName("GET /api/contas/99 deve retornar 404")
    void deveRetornar404() throws Exception {
        when(contaService.consultarPorId(99L))
            .thenThrow(new ContaNaoEncontradaException(99L));

        mvc.perform(get("/api/contas/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.erro").exists());
    }

    // ─── POST /api/contas ────────────────────────────────

    @Test
    @DisplayName("POST com nome vazio deve retornar 400")
    void deveRetornar400NomeVazio() throws Exception {
        String body = mapper.writeValueAsString(new ContaForm("", 100.0));

        mvc.perform(post("/api/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST com saldo negativo deve retornar 400")
    void deveRetornar400SaldoNegativo() throws Exception {
        doThrow(new SaldoInvalidoException("O saldo não pode ser negativo: -10.0"))
            .when(contaService).incluirConta(any(), any());

        String body = mapper.writeValueAsString(new ContaForm("Teste", -10.0));

        mvc.perform(post("/api/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro").value("O saldo não pode ser negativo: -10.0"));
    }

    @Test
    @DisplayName("POST com JSON malformado deve retornar 400")
    void deveRetornar400JsonMalformado() throws Exception {
        mvc.perform(post("/api/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ isso nao e json }"))
            .andExpect(status().isBadRequest());
    }

    // ─── PUT /api/contas/{id} ────────────────────────────

    @Test
    @DisplayName("PUT para conta inexistente deve retornar 404")
    void deveRetornar404AoAtualizar() throws Exception {
        doThrow(new ContaNaoEncontradaException(99L))
            .when(contaService).alterarConta(eq(99L), any(), any());

        String body = mapper.writeValueAsString(new ContaForm("Alguém", 0.0));

        mvc.perform(put("/api/contas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isNotFound());
    }

    // ─── DELETE /api/contas/{id} ─────────────────────────

    @Test
    @DisplayName("DELETE /api/contas/1 deve retornar 204")
    void deveExcluirComSucesso() throws Exception {
        doNothing().when(contaService).excluirConta(1L);

        mvc.perform(delete("/api/contas/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE para conta inexistente deve retornar 404")
    void deveRetornar404AoExcluir() throws Exception {
        doThrow(new ContaNaoEncontradaException(99L))
            .when(contaService).excluirConta(99L);

        mvc.perform(delete("/api/contas/99"))
            .andExpect(status().isNotFound());
    }

    // ─── falha inesperada e fuzz ─────────────────────────

    @Test
    @DisplayName("Falha inesperada deve retornar 500 sem expor detalhes")
    void deveRetornar500SemExporDetalhes() throws Exception {
        when(contaService.consultarPorNome(null))
            .thenThrow(new RuntimeException("erro interno de banco"));

        mvc.perform(get("/api/contas"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.erro").value("Erro interno no servidor."));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "<script>alert(1)</script>",
        "'; DROP TABLE conta;--",
        "../../etc/passwd"
    })
    @DisplayName("Fuzz: entradas maliciosas devem ser tratadas com segurança")
    void fuzzEntradasMaliciosas(String entrada) throws Exception {
        doThrow(new SaldoInvalidoException("erro"))
            .when(contaService).incluirConta(any(), any());

        String body = mapper.writeValueAsString(new ContaForm(entrada, 100.0));

        mvc.perform(post("/api/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().is4xxClientError());
    }
}
