package org.com.tokio.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViaCepResponse {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String ibge;
    private String gia;
    private String ddd;
    private String siafi;
    
    @JsonProperty("erro")
    private Boolean erro;

    public boolean isErro() {
        return erro != null && erro;
    }
} 