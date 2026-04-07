package org.example.banco.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.banco.entity.Conta;
import org.example.banco.entity.ContaForm;
import org.example.banco.service.ContaServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST da API bancária.
 *
 * GET    /api/contas           → lista todas (filtro ?nome= opcional)
 * GET    /api/contas/{id}      → busca por ID
 * POST   /api/contas           → cria nova conta
 * PUT    /api/contas/{id}      → atualiza nome e saldo
 * DELETE /api/contas/{id}      → remove conta
 */
@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaServicePort contaService;

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
        contaService.incluirConta(form.getNome(), form.getSaldo());
        // busca a conta recém-salva pelo nome e saldo pra retornar com o id gerado
        List<Conta> todas = contaService.consultarTodas();
        Conta criada = todas.get(todas.size() - 1);
        return ResponseEntity.status(201).body(criada);
    }

    @PutMapping("/{id}")
    public Conta atualizar(@PathVariable Long id, @Valid @RequestBody ContaForm form) {
        contaService.alterarConta(id, form.getNome(), form.getSaldo());
        return contaService.consultarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        contaService.excluirConta(id);
        return ResponseEntity.noContent().build();
    }
}
