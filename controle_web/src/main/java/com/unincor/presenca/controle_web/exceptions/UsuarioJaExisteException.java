package com.unincor.presenca.controle_web.exceptions;

public class UsuarioJaExisteException extends RuntimeException {

    public UsuarioJaExisteException(String mensagem) {
        super(mensagem);
    }
}
