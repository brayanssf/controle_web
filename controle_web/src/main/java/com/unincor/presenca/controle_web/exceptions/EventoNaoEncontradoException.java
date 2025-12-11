package com.unincor.presenca.controle_web.exceptions;

public class EventoNaoEncontradoException extends RuntimeException {

    public EventoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
