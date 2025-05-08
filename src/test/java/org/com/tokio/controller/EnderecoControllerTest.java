package org.com.tokio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.tokio.dto.EnderecoDTO;
import org.com.tokio.model.Endereco;
import org.com.tokio.model.Usuario;
import org.com.tokio.service.EnderecoService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnderecoController.class)
class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnderecoService enderecoService;

    @Test
    void criarEndereco_Sucesso() throws Exception {
        // Arrange
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep("12345678");
        enderecoDTO.setLogradouro("Rua Teste");
        enderecoDTO.setNumero("123");
        enderecoDTO.setUsuarioId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");

        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCep(enderecoDTO.getCep());
        endereco.setLogradouro(enderecoDTO.getLogradouro());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setUsuario(usuario);

        when(enderecoService.criarEndereco(any(EnderecoDTO.class))).thenReturn(endereco);

        // Act & Assert
        mockMvc.perform(post("/api/enderecos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enderecoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cep").value(enderecoDTO.getCep()))
                .andExpect(jsonPath("$.logradouro").value(enderecoDTO.getLogradouro()))
                .andExpect(jsonPath("$.numero").value(enderecoDTO.getNumero()));
    }

    @Test
    void listarEnderecosPorUsuario_Sucesso() throws Exception {
        // Arrange
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Endereco endereco1 = new Endereco();
        endereco1.setId(1L);
        endereco1.setUsuario(usuario);

        Endereco endereco2 = new Endereco();
        endereco2.setId(2L);
        endereco2.setUsuario(usuario);

        Page<Endereco> enderecos = new PageImpl<>(Arrays.asList(endereco1, endereco2));
        when(enderecoService.listarEnderecosPorUsuario(usuarioId, PageRequest.of(0, 10))).thenReturn(enderecos);

        // Act & Assert
        mockMvc.perform(get("/api/enderecos/usuario/{usuarioId}", usuarioId))
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
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setLogradouro("Rua Teste");

        when(enderecoService.buscarPorId(id)).thenReturn(endereco);

        // Act & Assert
        mockMvc.perform(get("/api/enderecos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.logradouro").value(endereco.getLogradouro()));
    }
} 