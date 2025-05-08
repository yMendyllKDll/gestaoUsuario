package org.com.tokio.service;

import org.com.tokio.client.ViaCepClient;
import org.com.tokio.client.ViaCepResponse;
import org.com.tokio.exception.EnderecoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CepService {
    private static final Logger logger = LoggerFactory.getLogger(CepService.class);
    
    @Autowired
    private ViaCepClient viaCepClient;
    
    public boolean validarCep(String cep) {
        try {
            logger.info("Validando CEP: {}", cep);
            ViaCepResponse response = viaCepClient.buscarCep(cep);
            logger.info("Resposta para {}: erro={}", cep, response.getErro());
            return response != null && response.getErro() == null;
        } catch (FeignException.NotFound e) {
            logger.error("CEP {} não encontrado", cep);
            return false;
        } catch (Exception e) {
            logger.error("Erro ao validar CEP {}: {}", cep, e.getMessage());
            return false;
        }
    }

    public ViaCepResponse buscarCep(String cep) {
        try {
            ViaCepResponse response = viaCepClient.buscarCep(cep);
            if (response.getErro() != null && response.getErro()) {
                throw new EnderecoException("CEP não encontrado");
            }
            return response;
        } catch (Exception e) {
            throw new EnderecoException("Erro ao buscar CEP: " + e.getMessage());
        }
    }
}