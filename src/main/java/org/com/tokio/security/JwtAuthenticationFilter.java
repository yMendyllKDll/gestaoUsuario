package org.com.tokio.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.tokio.dto.UsuarioDTO;
import org.com.tokio.model.Usuario;
import org.com.tokio.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response) throws AuthenticationException {
        try {
            UsuarioDTO usuario = new ObjectMapper().readValue(request.getInputStream(), UsuarioDTO.class);
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    usuario.getEmail(),
                    usuario.getSenha(),
                    new ArrayList<>()
                )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = Jwts.builder()
            .setSubject(user.getUsername())
            .claim("tipoUsuario", usuario.getTipoUsuario().toString())
            .setExpiration(new Date(System.currentTimeMillis() + 864000000)) // 10 dias
            .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs".getBytes())
            .compact();

        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().write(token);
    }
} 