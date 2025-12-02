package com.unincor.presenca.controle_web.controller;

import com.unincor.presenca.controle_web.dto.EventoRequest;
import com.unincor.presenca.controle_web.model.domain.Evento;
import com.unincor.presenca.controle_web.model.domain.Participante;
import com.unincor.presenca.controle_web.model.domain.Presenca;
import com.unincor.presenca.controle_web.model.service.EventoService;
import com.unincor.presenca.controle_web.model.service.ParticipanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Gerenciamento de eventos")
@SecurityRequirement(name = "bearerAuth")

public class EventoRestController {

    private final EventoService eventoService;
    private final ParticipanteService participanteService;

    @GetMapping
    @Operation(summary = "Listar todos os eventos")
    public ResponseEntity<List<Evento>> listarTodos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID")
    public ResponseEntity<Evento> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventoService.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo evento (Admin)")
    public ResponseEntity<Evento> criar(@Valid @RequestBody EventoRequest request) {
        try {
            Evento evento = new Evento();
            evento.setTitulo(request.getTitulo());
            evento.setDescricao(request.getDescricao());
            evento.setDataInicio(request.getDataInicio());
            evento.setDataFim(request.getDataFim());
            evento.setLocal(request.getLocal());
            evento.setMaxParticipantes(request.getMaxParticipantes());

            return ResponseEntity.ok(eventoService.salvar(evento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar evento (Admin)")
    public ResponseEntity<Evento> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest request) {
        try {
            return ResponseEntity.ok(eventoService.atualizar(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar evento (Admin)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            eventoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/inscrever")
    @Operation(summary = "Inscrever-se em um evento")
    public ResponseEntity<Participante> inscrever(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        try {
            String emailUsuario = authentication.getName();
            Participante.Genero genero = Participante.Genero.valueOf(body.get("genero"));
            return ResponseEntity.ok(participanteService.inscrever(id, emailUsuario, genero));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/participantes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar participantes de um evento (Admin)")
    public ResponseEntity<List<Participante>> listarParticipantes(@PathVariable Long id) {
        return ResponseEntity.ok(participanteService.listarPorEvento(id));
    }

    @PostMapping("/{id}/presencas")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Marcar presença (Admin)")
    public ResponseEntity<Presenca> marcarPresenca(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body
    ) {
        try {
            Long participanteId = Long.valueOf(body.get("participanteId").toString());
            String observacoes = body.getOrDefault("observacoes", "").toString();
            return ResponseEntity.ok(participanteService.marcarPresenca(participanteId, id, observacoes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
