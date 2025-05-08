package org.com.tokio.service;

import org.com.tokio.client.ViaCepClient;
import org.com.tokio.dto.EnderecoDTO;
import org.com.tokio.model.Endereco;
import org.com.tokio.model.Usuario;
import org.com.tokio.repository.EnderecoRepository;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarEndereco_Sucesso() {
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

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        // Act
        Endereco resultado = enderecoService.criarEndereco(enderecoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(enderecoDTO.getCep(), resultado.getCep());
        assertEquals(enderecoDTO.getLogradouro(), resultado.getLogradouro());
        assertEquals(enderecoDTO.getNumero(), resultado.getNumero());
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    void listarEnderecosPorUsuario_Sucesso() {
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
        Pageable pageable = PageRequest.of(0, 10);

        when(usuarioRepository.existsById(usuarioId)).thenReturn(true);
        when(enderecoRepository.findByUsuarioId(usuarioId, pageable)).thenReturn(enderecos);

        // Act
        Page<Endereco> resultado = enderecoService.listarEnderecosPorUsuario(usuarioId, pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        verify(enderecoRepository).findByUsuarioId(usuarioId, pageable);
    }

    @Test
    void buscarPorId_Sucesso() {
        // Arrange
        Long id = 1L;
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setLogradouro("Rua Teste");

        when(enderecoRepository.findById(id)).thenReturn(Optional.of(endereco));

        // Act
        Endereco resultado = enderecoService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(enderecoRepository).findById(id);
    }

    @Test
    void buscarPorId_EnderecoNaoEncontrado() {
        // Arrange
        Long id = 1L;
        when(enderecoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> enderecoService.buscarPorId(id));
        verify(enderecoRepository).findById(id);
    }
} 