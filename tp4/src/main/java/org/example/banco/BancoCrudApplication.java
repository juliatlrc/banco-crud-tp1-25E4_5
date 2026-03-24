package org.example.banco;

import org.example.banco.service.ContaServicePort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@SpringBootApplication
public class BancoCrudApplication implements CommandLineRunner {

    private final ContaServicePort contaService;
    private final Environment env;

    public BancoCrudApplication(ContaServicePort contaService, Environment env) {
        this.contaService = contaService;
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(BancoCrudApplication.class, args);
    }

    // Só executa a demo CLI se o profile "demo" estiver ativo
    // Para rodar: ./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
    @Override
    public void run(String... args) {
        if (!env.acceptsProfiles(Profiles.of("demo"))) return;

        System.out.println("=== Banco CRUD — Demo CLI ===\n");

        contaService.incluirConta("Maria Silva", 1500.00);
        contaService.incluirConta("João Souza", 800.00);
        contaService.incluirConta("Ana Lima", 300.00);

        System.out.println(">> Contas cadastradas:");
        contaService.consultarTodas().forEach(c ->
            System.out.printf("   [%d] %s | R$ %.2f%n", c.getId(), c.getNome(), c.getSaldo())
        );

        System.out.println("\n>> Alterando saldo da conta 1 para R$ 2000.00...");
        contaService.alterarConta(1L, "Maria Silva", 2000.00);

        System.out.println("\n>> Excluindo conta 3...");
        contaService.excluirConta(3L);

        System.out.println("\n>> Estado final:");
        contaService.consultarTodas().forEach(c ->
            System.out.printf("   [%d] %s | R$ %.2f%n", c.getId(), c.getNome(), c.getSaldo())
        );

        System.out.println("\n=== Fim da demo ===");
    }
}
