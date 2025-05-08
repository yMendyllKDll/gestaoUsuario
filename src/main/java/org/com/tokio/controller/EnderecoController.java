package org.com.tokio.controller;

import org.com.tokio.dto.EnderecoDTO;
import org.com.tokio.model.Endereco;
import org.com.tokio.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<Endereco> criarEndereco(@RequestBody EnderecoDTO enderecoDTO) {
        return ResponseEntity.ok(enderecoService.criarEndereco(enderecoDTO));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<Endereco>> listarEnderecosPorUsuario(@PathVariable Long usuarioId, Pageable pageable) {
        return ResponseEntity.ok(enderecoService.listarEnderecosPorUsuario(usuarioId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(enderecoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id, @RequestBody EnderecoDTO enderecoDTO) {
        return ResponseEntity.ok(enderecoService.atualizarEndereco(id, enderecoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Long id) {
        enderecoService.deletarEndereco(id);
        return ResponseEntity.noContent().build();
    }
} 