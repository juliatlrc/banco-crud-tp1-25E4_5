package org.example.banco.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.banco.entity.Conta;
import org.example.banco.entity.ContaForm;
import org.example.banco.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Expõe os endpoints REST da API.
 *
 * GET    /api/contas           → lista todas (filtro opcional: ?nome=)
 * GET    /api/contas/{id}      → busca por ID
 * POST   /api/contas           → cria nova conta
 * PUT    /api/contas/{id}      → atualiza conta existente
 * DELETE /api/contas/{id}      → remove conta
 */
@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @GetMapping
    public List<Conta> listar(@RequestParam(required = false) String nome) {
        return contaService.consultarPorNome(nome);
    }

    @GetMapping("/{id}")
    public Conta buscar(@PathVariable Long id) {
        return contaService.consultarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Conta> criar(@Valid @RequestBody ContaForm form) {
        contaService.incluirConta(form);
        // busca o registro recém-criado para retornar com o id gerado
        List<Conta> todas = contaService.consultarTodas();
        Conta criada = todas.get(todas.size() - 1);
        return ResponseEntity.status(201).body(criada);
    }

    @PutMapping("/{id}")
    public Conta atualizar(@PathVariable Long id, @Valid @RequestBody ContaForm form) {
        contaService.alterarConta(id, form);
        return contaService.consultarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        contaService.excluirConta(id);
        return ResponseEntity.noContent().build();
    }
}
