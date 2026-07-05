package com.mercado_api.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration 
public class BasicSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // permissão e gerenciamento de rota
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService) throws Exception {
            AuthenticationManagerBuilder builder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
            builder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
            return builder.build();
    }

@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // 🟢 Deixa livre APENAS o cadastro e o login
            .requestMatchers("/Usuario/cadastrar").permitAll()
            .requestMatchers("/Usuario/logar").permitAll()
            // 🔴 QUALQUER outra rota (produtos, pedidos, etc.) exige autenticação!
            .anyRequest().authenticated())
        .httpBasic(basic -> {}); // Ativa o modo Basic Auth
        return http.build();    
    }
}