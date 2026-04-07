package org.example.bancocrud.service;

import lombok.RequiredArgsConstructor;
import org.example.bancocrud.entity.Conta;
import org.example.bancocrud.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    // CREATE
    public void incluirContaDb(String nome, Double saldo) {
        // id null porque o banco vai gerar automatico
        Conta conta = new Conta(null, nome, saldo);
        contaRepository.save(conta);
        System.out.println("Conta criada: " + nome);
    }

    // READ - lista todas
    public List<Conta> consultarContasDb() {
        return contaRepository.findAll();
    }

    // READ - busca uma por id
    public Conta consultarContaDb(Long id) {
        // usei clausula de guarda aqui em vez de if aninhado
        return contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta nao encontrada: " + id));
    }

    // UPDATE - so muda o saldo, nome permanece igual
    public void alterarConta(Long id, Double novoSaldo) {
        Conta antiga = consultarContaDb(id);
        // como nao tem setter, preciso criar um novo objeto com os dados atualizados
        Conta atualizada = new Conta(antiga.getId(), antiga.getNome(), novoSaldo);
        contaRepository.save(atualizada);
        System.out.println("Saldo atualizado para: " + novoSaldo);
    }

    // DELETE
    public void excluirContaDb(Long id) {
        // garanto que existe antes de tentar deletar
        consultarContaDb(id);
        contaRepository.deleteById(id);
        System.out.println("Conta " + id + " removida");
    }
}
