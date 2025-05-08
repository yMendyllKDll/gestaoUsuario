package org.com.tokio.service;

import org.com.tokio.dto.UsuarioDTO;
import org.com.tokio.model.Usuario;
import org.com.tokio.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarUsuario_Sucesso() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Teste");
        usuarioDTO.setEmail("teste@teste.com");
        usuarioDTO.setSenha("senha123");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());

        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.criarUsuario(usuarioDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuarioDTO.getNome(), resultado.getNome());
        assertEquals(usuarioDTO.getEmail(), resultado.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void listarUsuarios_Sucesso() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Usuario 1");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Usuario 2");

        Page<Usuario> usuarios = new PageImpl<>(Arrays.asList(usuario1, usuario2));
        Pageable pageable = PageRequest.of(0, 10);

        when(usuarioRepository.findAllByOrderByNomeAsc(any(Pageable.class))).thenReturn(usuarios);

        // Act
        Page<Usuario> resultado = usuarioService.listarUsuarios(pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        verify(usuarioRepository).findAllByOrderByNomeAsc(pageable);
    }

    @Test
    void buscarPorId_Sucesso() {
        // Arrange
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Teste");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = usuarioService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(usuarioRepository).findById(id);
    }

    @Test
    void buscarPorId_UsuarioNaoEncontrado() {
        // Arrange
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.buscarPorId(id));
        verify(usuarioRepository).findById(id);
    }
} 