package com.mercado_api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.mercado_api.Repository.ProdutoRepository;
import com.mercado_api.model.Produto;
import com.mercado_api.service.ProdutoService; // Adicione essa linha

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/Produto")
public class ProdutoController {

    @Autowired
    private ProdutoService service;
    @Autowired
    private ProdutoRepository repository;

    //----------------get

    @GetMapping("/id/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id){
        return service.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Page<Produto>> getByNome(Pageable pageable, @PathVariable String nome){
        Page<Produto> pagina = service.buscarPorNome(nome, pageable);
        return ResponseEntity.ok(pagina); 
    }

    //(Long idIniciar, Long idFinal, Pageable pageable);

 @GetMapping("/intervaloId")
    public ResponseEntity<Page<Produto>> getPorIntervaloPaginado(
        @RequestParam Long idIniciar, 
        @RequestParam Long idFinal,
        Pageable pageable) {
    
    Page<Produto> pagina = service.buscarPorIntervaloDeId(idIniciar, idFinal, pageable);
    return ResponseEntity.ok(pagina);
}

//------------post

@PostMapping
public ResponseEntity<Produto> criarProduto(@Valid @RequestBody Produto produto){
    Produto novoProduto = repository.save(produto);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
}

@PostMapping("/lote")
public ResponseEntity<List<Produto>> criarProdutosEmLote(@Valid @RequestBody List<Produto> produtos) {
    // O repository.saveAll salva uma lista inteira de uma vez só no banco!
    List<Produto> novosProdutos = repository.saveAll(produtos);
    return ResponseEntity.status(HttpStatus.CREATED).body(novosProdutos);
}

//-------------put


@PutMapping
    public ResponseEntity<Produto> atualizarProduto(@Valid @RequestBody Produto produto){
        return repository.findById(produto.getId())
                .map(produtoExiste -> {
                    // 🟢 CORRIGIDO: Nome do repository e da variável alinhados
                    Produto produtoAtualizado = repository.save(produto);
                    
                    // 🟢 CORRIGIDO: Retornando a mesma variável que foi salva acima
                    return ResponseEntity.ok(produtoAtualizado); 
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //--------------deleter

@DeleteMapping("/delete/{id}") // Adicionei a barra antes de 'delete' por segurança
    public ResponseEntity<Void> deleter(@PathVariable Long id){
        repository.deleteById(id);
        
        // 🎯 O Segredo: Diz ao Postman que foi deletado e não há mais conteúdo (Status 204)
        return ResponseEntity.noContent().build();
    }

    
}
