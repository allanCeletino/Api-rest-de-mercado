package com.mercado_api.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mercado_api.Repository.UsuarioRepository;
import com.mercado_api.model.Usuario;
import com.mercado_api.service.UsuarioService;
import jakarta.validation.Valid;
import com.mercado_api.model.UsuarioLogin;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/Usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository repository;

    // ------------------ get ------------------

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getByEmail(@PathVariable String email) {
        return service.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------ post ------------------

    @PostMapping("/logar")
    public ResponseEntity<Usuario> logar(@Valid @RequestBody UsuarioLogin usuarioLogin) {
        return service.logar(usuarioLogin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarUsuario(usuario));
    }

    // ------------------ put ------------------

    @PutMapping // 🟢 CORRIGIDO: faltava esta anotação — o endpoint não existia
    public ResponseEntity<Usuario> atualizarUsuario(@Valid @RequestBody Usuario usuario) {
        return repository.findById(usuario.getId())
                .map(usuarioExistente -> {
                    Usuario eUsuario = repository.save(usuario);
                    return ResponseEntity.ok(eUsuario);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------ delete ------------------

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}