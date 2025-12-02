package com.unincor.presenca.controle_web.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.unincor.presenca.controle_web.exceptions.PresencaNaoEncontradaException;
import com.unincor.presenca.controle_web.model.domain.Presenca;
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
