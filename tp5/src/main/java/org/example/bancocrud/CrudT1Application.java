package org.example.bancocrud;

import lombok.RequiredArgsConstructor;
import org.example.bancocrud.entity.Conta;
import org.example.bancocrud.service.ContaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class CrudT1Application implements CommandLineRunner {

    private final ContaService contaService;

    public static void main(String[] args) {
        // uso mysql em producao, h2 nos testes
        System.setProperty("spring.profiles.active", "mysql");
        SpringApplication.run(CrudT1Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Sistema Bancario iniciado ===");

        // criando algumas contas de teste
        contaService.incluirContaDb("Maria", 500.00);
        contaService.incluirContaDb("Joao", 200.00);
        contaService.incluirContaDb("Felipe", 400.00);

        System.out.println("\n-- Contas cadastradas --");
        consultarContas();

        // atualiza o saldo da primeira conta
        List<Conta> contas = contaService.consultarContasDb();
        if (!contas.isEmpty()) {
            Long primeiroId = contas.get(0).getId();
            contaService.alterarConta(primeiroId, 999.00);
        }

        System.out.println("\n-- Apos alteracao --");
        consultarContas();
    }

    private void consultarContas() {
        List<Conta> lista = contaService.consultarContasDb();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma conta encontrada.");
            return;
        }
        lista.forEach(c ->
            System.out.printf("ID: %d | Nome: %s | Saldo: R$ %.2f%n",
                c.getId(), c.getNome(), c.getSaldo())
        );
    }
}
