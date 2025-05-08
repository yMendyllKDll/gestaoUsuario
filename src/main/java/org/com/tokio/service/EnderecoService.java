package org.com.tokio.service;

import org.com.tokio.dto.EnderecoDTO;
import org.com.tokio.exception.EnderecoException;
import org.com.tokio.exception.RecursoNaoEncontradoException;
import org.com.tokio.model.Endereco;
import org.com.tokio.model.Usuario;
import org.com.tokio.repository.EnderecoRepository;
import org.com.tokio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CepService cepService;

    @Transactional
    public Endereco criarEndereco(EnderecoDTO enderecoDTO) {
        Usuario usuario = usuarioRepository.findById(enderecoDTO.getUsuarioId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        var viaCepResponse = cepService.buscarCep(enderecoDTO.getCep());
        
        Endereco endereco = new Endereco();
        endereco.setLogradouro(viaCepResponse.getLogradouro());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setBairro(viaCepResponse.getBairro());
        endereco.setCidade(viaCepResponse.getLocalidade());
        endereco.setEstado(viaCepResponse.getUf());
        endereco.setCep(enderecoDTO.getCep());
        endereco.setUsuario(usuario);

        return enderecoRepository.save(endereco);
    }

    public Page<Endereco> listarEnderecosPorUsuario(Long usuarioId, Pageable pageable) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
        return enderecoRepository.findByUsuarioId(usuarioId, pageable);
    }

    public Endereco buscarPorId(Long id) {
        return enderecoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não encontrado"));
    }

    @Transactional
    public Endereco atualizarEndereco(Long id, EnderecoDTO enderecoDTO) {
        Endereco endereco = buscarPorId(id);
        
        var viaCepResponse = cepService.buscarCep(enderecoDTO.getCep());
        
        endereco.setLogradouro(viaCepResponse.getLogradouro());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setBairro(viaCepResponse.getBairro());
        endereco.setCidade(viaCepResponse.getLocalidade());
        endereco.setEstado(viaCepResponse.getUf());
        endereco.setCep(enderecoDTO.getCep());

        return enderecoRepository.save(endereco);
    }

    @Transactional
    public void deletarEndereco(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Endereço não encontrado");
        }
        enderecoRepository.deleteById(id);
    }
} 