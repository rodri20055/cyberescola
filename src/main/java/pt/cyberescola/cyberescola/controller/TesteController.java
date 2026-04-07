package pt.cyberescola.cyberescola.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.cyberescola.cyberescola.model.Utilizador;
import pt.cyberescola.cyberescola.repository.UtilizadorRepository;

@RestController
public class TesteController {

    private final UtilizadorRepository repo;

    public TesteController(UtilizadorRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/utilizadores")
    public List<Utilizador> listar() {
        return repo.findAll();
    }
}