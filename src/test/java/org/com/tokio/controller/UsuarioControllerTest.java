package org.com.tokio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.tokio.dto.UsuarioDTO;
import org.com.tokio.model.Usuario;
import org.com.tokio.service.UsuarioService;
import org.com.tokio.controller.UsuarioController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void criarUsuario_Sucesso() throws Exception {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Teste");
        usuarioDTO.setEmail("teste@teste.com");
        usuarioDTO.setSenha("senha123");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());

        when(usuarioService.criarUsuario(any(UsuarioDTO.class))).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value(usuarioDTO.getNome()))
                .andExpect(jsonPath("$.email").value(usuarioDTO.getEmail()));
    }

    @Test
    void listarUsuarios_Sucesso() throws Exception {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Usuario 1");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Usuario 2");

        Page<Usuario> usuarios = new PageImpl<>(Arrays.asList(usuario1, usuario2));
        when(usuarioService.listarUsuarios(any())).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2));
    }

    @Test
    void buscarPorId_Sucesso() throws Exception {
        // Arrange
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Teste");

        when(usuarioService.buscarPorId(id)).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value(usuario.getNome()));
    }
} 