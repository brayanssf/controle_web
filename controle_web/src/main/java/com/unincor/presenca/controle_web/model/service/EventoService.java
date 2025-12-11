package com.unincor.presenca.controle_web.model.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.unincor.presenca.controle_web.exceptions.EventoNaoEncontradoException;
import com.unincor.presenca.controle_web.model.domain.Evento;
import com.unincor.presenca.controle_web.model.repository.EventoRepository;

@Service
@RequiredArgsConstructor

public class EventoService {

    private final EventoRepository eventoRepository;

    @Transactional(readOnly = true)
    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNaoEncontradoException("Evento com ID " + id + " não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Evento> buscarPorNome(String nome) {
        return eventoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Evento> buscarPorData(String data) {
        return eventoRepository.findByData(data);
    }

    @Transactional
    public Evento salvar(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Transactional
    public void deletar(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new EventoNaoEncontradoException("Evento com ID " + id + " não encontrado");
        }
        eventoRepository.deleteById(id);
    }

}
