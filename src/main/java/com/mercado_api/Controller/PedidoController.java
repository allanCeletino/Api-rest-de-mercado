package com.mercado_api.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mercado_api.Enum.StatusPedido;
import com.mercado_api.Repository.PedidoRepository;
import com.mercado_api.Repository.UsuarioRepository;
import com.mercado_api.dto.PedidoDTO;
import com.mercado_api.model.Pedido;
import com.mercado_api.model.Usuario;
import com.mercado_api.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/Pedido")
public class PedidoController {

    @Autowired
    private PedidoService service;
    @Autowired
    private PedidoRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // ------------------- get -------------------

    @GetMapping("/id/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable Long id){
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/BuscarPorUsuario/{id}")
    public ResponseEntity<Page<Pedido>> getByUsuario(Pageable pageable, @PathVariable Long id){
        Usuario usuario = new Usuario();
        usuario.setId(id);

        Page<Pedido> pedidos = service.buscarPorUsuario(usuario, pageable);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/BuscarPorPedido/{statusPedido}")
    public ResponseEntity<Page<Pedido>> getByStatusPedido(Pageable pageable, @PathVariable StatusPedido statusPedido){
        Page<Pedido> buscarPorStatus = service.buscarPorStatusPedido(statusPedido, pageable);
        return ResponseEntity.ok(buscarPorStatus);
    }

    @GetMapping("/BuscarPorDatas")
    public ResponseEntity<Page<Pedido>> buscarPorDatas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            Pageable pageable) {
        return ResponseEntity.ok(service.BuscarPorDataPorPedido(inicio, fim, pageable));
    }

    // ------------------- post -------------------

    @PostMapping
    public ResponseEntity<Pedido> salvarPedido(@Valid @RequestBody PedidoDTO sacola, Authentication authentication) {
        String emailLogado = authentication.getName();

        Usuario usuarioLogado = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado!"));

        Pedido novoPedido = service.criarPedido(sacola, usuarioLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    // ------------------- put -------------------

    @PutMapping
    public ResponseEntity<Pedido> atualizarPedido(@Valid @RequestBody Pedido pedido){
        return repository.findById(pedido.getId())
                .map(pedidoExiste -> {
                    Pedido pedidoAtualizado = repository.save(pedido);
                    return ResponseEntity.ok(pedidoAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------- delete -------------------

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleter(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}