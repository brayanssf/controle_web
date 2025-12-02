package com.unincor.presenca.controle_web.model.service;

import com.unincor.presenca.controle_web.model.domain.Evento;
import com.unincor.presenca.controle_web.model.domain.Participante;
import com.unincor.presenca.controle_web.model.domain.Presenca;
import com.unincor.presenca.controle_web.model.domain.Usuario;
import com.unincor.presenca.controle_web.model.repository.EventoRepository;
import com.unincor.presenca.controle_web.model.repository.ParticipanteRepository;
import com.unincor.presenca.controle_web.model.repository.PresencaRepository;
import com.unincor.presenca.controle_web.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PresencaRepository presencaRepository;

    public List<Participante> listarPorEvento(Long eventoId) {
        return participanteRepository.findByEventoId(eventoId);
    }

    public List<Participante> listarPorUsuario(Long usuarioId) {
        return participanteRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public Participante inscrever(Long eventoId, String emailUsuario, Participante.Genero genero) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (participanteRepository.existsByUsuarioIdAndEventoId(usuario.getId(), eventoId)) {
            throw new RuntimeException("Usuário já inscrito neste evento");
        }

        long participantesAtuais = participanteRepository.contarPorEvento(eventoId);
        if (evento.getMaxParticipantes() > 0 && participantesAtuais >= evento.getMaxParticipantes()) {
            throw new RuntimeException("Evento com vagas esgotadas");
        }

        Participante participante = new Participante();
        participante.setUsuario(usuario);
        participante.setEvento(evento);
        participante.setGenero(genero);
        participante.setConfirmado(false);

        return participanteRepository.save(participante);
    }

    @Transactional
    public Participante confirmarInscricao(Long participanteId) {
        Participante participante = participanteRepository.findById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante não encontrado"));

        participante.setConfirmado(true);
        return participanteRepository.save(participante);
    }

    @Transactional
    public Presenca marcarPresenca(Long participanteId, Long eventoId, String observacoes) {
        Participante participante = participanteRepository.findById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante não encontrado"));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (presencaRepository.existsByParticipanteIdAndEventoId(participanteId, eventoId)) {
            throw new RuntimeException("Presença já registrada para este participante");
        }

        Presenca presenca = new Presenca();
        presenca.setParticipante(participante);
        presenca.setEvento(evento);
        presenca.setObservacoes(observacoes);
        presenca.setPresente(true);

        return presencaRepository.save(presenca);
    }

    @Transactional
    public void cancelarInscricao(Long participanteId) {
        participanteRepository.deleteById(participanteId);
    }

}
