package com.mercado_api.Security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mercado_api.model.Usuario;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities;

    // 🟢 NOVO: Construtor necessário para receber o Usuário do banco de dados
    public UserDetailsImpl(Usuario usuario) {
        this.userName = usuario.getEmail(); // ou usuario.getUsuario() dependendo da sua Model
        this.password = usuario.getSenha();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    } // 🟢 CORRIGIDO: Fechamento de chave que faltava aqui

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; } // 🟢 CORRIGIDO: Nome correto do método do Spring Security
}
