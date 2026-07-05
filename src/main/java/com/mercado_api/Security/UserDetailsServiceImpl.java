package com.mercado_api.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mercado_api.Repository.UsuarioRepository;
import com.mercado_api.model.Usuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository; // 🟢 Injetado como repository
    
    @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 🟢 Agora chamando o método correto que você acabou de criar
    Usuario usuario = repository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado -> " + username));
    
    return new UserDetailsImpl(usuario);
}
}
