package com.unincor.presenca.controle_web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unincor.presenca.controle_web.model.domain.Evento;
import com.unincor.presenca.controle_web.model.service.EventoService;

@Controller
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoViewController {

    private final EventoService eventoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "eventos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("evento", new Evento());
        return "eventos/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        try {
            Evento evento = eventoService.buscarPorId(id);
            model.addAttribute("evento", evento);
            return "eventos/form";
        } catch (Exception e) {
            return "redirect:/eventos";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Evento evento,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "eventos/form";
        }

        try {
            eventoService.salvar(evento);
            redirectAttributes.addFlashAttribute("mensagem", "Evento salvo com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/eventos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao salvar evento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/eventos/novo";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Evento excluído com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir evento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        return "redirect:/eventos";
    }

}
