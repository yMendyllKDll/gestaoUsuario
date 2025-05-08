package org.com.tokio.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ViaCepClientTest {

    @Autowired
    private ViaCepClient viaCepClient;

    @Test
    void buscarCep_Sucesso() {
        // Arrange
        String cep = "01001000";

        // Act
        ViaCepResponse response = viaCepClient.buscarCep(cep);

        // Assert
        assertNotNull(response);
        assertEquals("01001-000", response.getCep());
        assertEquals("Praça da Sé", response.getLogradouro());
        assertEquals("Sé", response.getBairro());
        assertEquals("São Paulo", response.getLocalidade());
        assertEquals("SP", response.getUf());
    }

    @Test
    void buscarCep_Invalido() {
        // Arrange
        String cep = "00000000";

        // Act
        ViaCepResponse response = viaCepClient.buscarCep(cep);

        // Assert
        assertNotNull(response);
        assertTrue(response.isErro());
    }
} 