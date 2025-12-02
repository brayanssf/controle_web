package com.unincor.presenca.controle_web.model.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.unincor.presenca.controle_web.exceptions.PresencaNaoEncontradaException;
import com.unincor.presenca.controle_web.model.domain.Evento;
import com.unincor.presenca.controle_web.model.domain.Presenca;
import com.unincor.presenca.controle_web.model.domain.Usuario;
import com.unincor.presenca.controle_web.model.repository.PresencaRepository;

@Service
@RequiredArgsConstructor
public class PresencaService {

    private final PresencaRepository presencaRepository;

    @Transactional(readOnly = true)
    public List<Presenca> listarTodas() {
        return presencaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Presenca buscarPorId(Long id) {
        return presencaRepository.findById(id)
                .orElseThrow(() -> new PresencaNaoEncontradaException("Presença com ID " + id + " não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Presenca> buscarPorUsuario(Usuario usuario) {
        return presencaRepository.findByUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public List<Presenca> buscarPorEvento(Evento evento) {
        return presencaRepository.findByEvento(evento);
    }

    @Transactional(readOnly = true)
    public List<Presenca> buscarPorUsuarioEEvento(Usuario usuario, Evento evento) {
        return presencaRepository.findByUsuarioAndEvento(usuario, evento);
    }

    @Transactional(readOnly = true)
    public List<Presenca> buscarPorTipo(String tipoPresenca) {
        return presencaRepository.findByTipoPresenca(tipoPresenca);
    }

    @Transactional
    public Presenca salvar(Presenca presenca) {
        return presencaRepository.save(presenca);
    }

    @Transactional
    public void deletar(Long id) {
        if (!presencaRepository.existsById(id)) {
            throw new PresencaNaoEncontradaException("Presença com ID " + id + " não encontrada");
        }
        presencaRepository.deleteById(id);
    }

}
