package org.com.tokio.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            String user = Jwts.parser()
                .setSigningKey("SecretKeyToGenJWTs".getBytes())
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();

            String tipoUsuario = Jwts.parser()
                .setSigningKey("SecretKeyToGenJWTs".getBytes())
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .get("tipoUsuario", String.class);

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, 
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + tipoUsuario)));
            }
            return null;
        }
        return null;
    }
} 