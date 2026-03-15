package org.example.banco.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.banco.entity.Conta;
import org.example.banco.entity.ContaForm;
import org.example.banco.exception.ContaNaoEncontradaException;
import org.example.banco.service.ContaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável pelas rotas da interface web.
 *
 * Rotas:
 *   GET  /contas           → listagem
 *   GET  /contas/nova      → formulário de cadastro
 *   POST /contas/nova      → salvar nova conta
 *   GET  /contas/{id}/editar → formulário de edição
 *   POST /contas/{id}/editar → salvar edição
 *   POST /contas/{id}/excluir → excluir conta
 */
@Controller
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("contas", contaService.consultarTodas());
        return "contas/lista";
    }

    @GetMapping("/nova")
    public String formularioNova(Model model) {
        model.addAttribute("contaForm", new ContaForm());
        model.addAttribute("acao", "nova");
        return "contas/formulario";
    }

    @PostMapping("/nova")
    public String salvarNova(@Valid @ModelAttribute ContaForm contaForm,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("acao", "nova");
            return "contas/formulario";
        }
        contaService.incluirConta(contaForm);
        redirect.addFlashAttribute("sucesso", "Conta criada com sucesso!");
        return "redirect:/contas";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Conta conta = contaService.consultarPorId(id);
        ContaForm form = new ContaForm(conta.getNome(), conta.getSaldo());
        model.addAttribute("contaForm", form);
        model.addAttribute("contaId", id);
        model.addAttribute("acao", "editar");
        return "contas/formulario";
    }

    @PostMapping("/{id}/editar")
    public String salvarEdicao(@PathVariable Long id,
                               @Valid @ModelAttribute ContaForm contaForm,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("contaId", id);
            model.addAttribute("acao", "editar");
            return "contas/formulario";
        }
        contaService.alterarConta(id, contaForm);
        redirect.addFlashAttribute("sucesso", "Conta atualizada com sucesso!");
        return "redirect:/contas";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        contaService.excluirConta(id);
        redirect.addFlashAttribute("sucesso", "Conta excluída com sucesso!");
        return "redirect:/contas";
    }

    // Redireciona raiz para /contas
    @GetMapping("/")
    public String raiz() {
        return "redirect:/contas";
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public String erroNaoEncontrado(ContaNaoEncontradaException ex, Model model) {
        model.addAttribute("erro", ex.getMessage());
        return "erro";
    }
}
