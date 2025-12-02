package com.unincor.presenca.controle_web.model.service;

import com.unincor.presenca.controle_web.dto.EventoRequest;
import com.unincor.presenca.controle_web.model.domain.Evento;
import com.unincor.presenca.controle_web.model.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class EventoService {

    private final EventoRepository eventoRepository;

    public List<Evento> listarTodos() {
        return eventoRepository.listarEventosAtivosOrdenados();
    }

    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));
    }

    @Transactional
    public Evento salvar(Evento evento) {
        // Validações
        if (evento.getDataFim().isBefore(evento.getDataInicio())) {
            throw new RuntimeException("Data de fim não pode ser anterior à data de início");
        }

        if (evento.getMaxParticipantes() < 0) {
            throw new RuntimeException("Número máximo de participantes não pode ser negativo");
        }

        if (evento.getId() == null) {
            evento.setAtivo(true);
        }

        return eventoRepository.save(evento);
    }

    @Transactional
    public Evento atualizar(Long id, EventoRequest request) {
        Evento evento = buscarPorId(id);

        evento.setTitulo(request.getTitulo());
        evento.setDescricao(request.getDescricao());
        evento.setDataInicio(request.getDataInicio());
        evento.setDataFim(request.getDataFim());
        evento.setLocal(request.getLocal());
        evento.setMaxParticipantes(request.getMaxParticipantes());

        return salvar(evento);
    }

    @Transactional
    public void deletar(Long id) {
        Evento evento = buscarPorId(id);
        evento.setAtivo(false);
        eventoRepository.save(evento);
    }

    @Transactional
    public void deletarPermanente(Long id) {
        eventoRepository.deleteById(id);
    }

}
