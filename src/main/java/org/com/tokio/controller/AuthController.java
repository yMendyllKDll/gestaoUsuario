package org.com.tokio.controller;

import lombok.Data;
import org.com.tokio.dto.UsuarioDTO;
import org.com.tokio.model.Usuario;
import org.com.tokio.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getSenha()
            )
        );
        
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok("Token JWT gerado com sucesso");
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registro(@Valid @RequestBody RegistroRequest registroRequest) {
        Usuario usuario = usuarioService.criarUsuario(registroRequest.toUsuarioDTO());
        return ResponseEntity.ok(usuario);
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        private String email;

        @NotBlank(message = "Senha é obrigatória")
        private String senha;
    }

    @Data
    public static class RegistroRequest {
        @NotBlank(message = "Nome é obrigatório")
        private String nome;

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        private String email;

        @NotBlank(message = "Senha é obrigatória")
        private String senha;

        public UsuarioDTO toUsuarioDTO() {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setNome(this.nome);
            dto.setEmail(this.email);
            dto.setSenha(this.senha);
            return dto;
        }
    }
} 