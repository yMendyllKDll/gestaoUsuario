package org.com.tokio.exception;

public class EnderecoException extends RuntimeException {
    
    public EnderecoException(String message) {
        super(message);
    }
    
    public EnderecoException(String message, Throwable cause) {
        super(message, cause);
    }
} 