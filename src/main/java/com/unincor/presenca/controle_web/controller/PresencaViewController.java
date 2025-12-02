package com.unincor.presenca.controle_web.controller;

import com.unincor.presenca.controle_web.model.service.EventoService;
import com.unincor.presenca.controle_web.model.service.ParticipanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/presencas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class PresencaViewController {

    private final EventoService eventoService;
    private final ParticipanteService participanteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "presencas/lista";
    }

    @GetMapping("/evento/{eventoId}")
    public String listarPorEvento(@PathVariable Long eventoId, Model model) {
        try {
            model.addAttribute("evento", eventoService.buscarPorId(eventoId));
            model.addAttribute("participantes", participanteService.listarPorEvento(eventoId));
            return "presencas/marcar";
        } catch (Exception e) {
            return "redirect:/presencas";
        }
    }

    @PostMapping("/marcar")
    public String marcarPresenca(
            @RequestParam Long participanteId,
            @RequestParam Long eventoId,
            @RequestParam(required = false) String observacoes,
            RedirectAttributes redirectAttributes) {
        try {
            participanteService.marcarPresenca(participanteId, eventoId, observacoes);
            redirectAttributes.addFlashAttribute("mensagem", "Presença marcada com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        return "redirect:/presencas/evento/" + eventoId;
    }

}
