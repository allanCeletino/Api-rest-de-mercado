package com.mercado_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mercado_api.Repository.UsuarioRepository;
import com.mercado_api.model.Usuario;
import com.mercado_api.model.UsuarioLogin;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // CADASTRAR USUÁRIO
    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new RuntimeException("Usuário não pode ser nulo.");
        }

        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Já existe um usuário com esse e-mail.");
        }

        usuario.setSenha(encoder.encode(usuario.getSenha()));

        return repository.save(usuario);
    }

    // LOGIN
    public Optional<Usuario> logar(UsuarioLogin loginDados) {

        if (loginDados == null) {
            return Optional.empty();
        }

        if (loginDados.getEmail() == null || loginDados.getSenha() == null) {
            return Optional.empty();
        }

        Optional<Usuario> usuarioBanco = repository.findByEmail(loginDados.getEmail());

        if (usuarioBanco.isPresent()) {

            if (encoder.matches(loginDados.getSenha(), usuarioBanco.get().getSenha())) {
                return usuarioBanco;
            }

        }

        return Optional.empty();
    }

    // BUSCAR POR EMAIL
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    // BUSCAR POR ID
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

}
