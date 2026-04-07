package org.example.banco;

import org.example.banco.entity.Conta;
import org.example.banco.service.ContaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * Classe principal da aplicação Banco CRUD.
 * Demonstra as operações disponíveis via linha de comando.
 */
@SpringBootApplication
public class BancoCrudApplication implements CommandLineRunner {

    private final ContaService contaService;

    public BancoCrudApplication(ContaService contaService) {
        this.contaService = contaService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BancoCrudApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Sistema Bancário CRUD ===\n");

        // CREATE - inserindo contas
        System.out.println(">> Criando contas...");
        contaService.incluirConta("Maria Silva", 1500.00);
        contaService.incluirConta("João Souza", 800.00);
        contaService.incluirConta("Ana Lima", 300.00);

        // READ - listando todas
        System.out.println("\n>> Contas cadastradas:");
        listarContas();

        // READ - buscando por ID
        System.out.println("\n>> Consultando conta ID 1:");
        Conta conta = contaService.consultarConta(1L);
        System.out.printf("   %s | Saldo: R$ %.2f%n", conta.getNome(), conta.getSaldo());

        // UPDATE - alterando saldo
        System.out.println("\n>> Alterando saldo da conta ID 2 para R$ 1200.00...");
        contaService.alterarSaldo(2L, 1200.00);
        listarContas();

        // DELETE - excluindo conta
        System.out.println("\n>> Excluindo conta ID 3...");
        contaService.excluirConta(3L);
        listarContas();

        System.out.println("\n=== Fim da demonstração ===");
    }

    private void listarContas() {
        List<Conta> contas = contaService.consultarTodasContas();
        if (contas.isEmpty()) {
            System.out.println("   Nenhuma conta cadastrada.");
            return;
        }
        contas.forEach(c ->
            System.out.printf("   [ID: %d] %s | Saldo: R$ %.2f%n",
                c.getId(), c.getNome(), c.getSaldo())
        );
    }
}
