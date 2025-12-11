package com.unincor.presenca.controle_web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.unincor.presenca.controle_web.model.domain.Presenca;
import com.unincor.presenca.controle_web.model.service.EventoService;
import com.unincor.presenca.controle_web.model.service.PresencaService;
import com.unincor.presenca.controle_web.model.service.UsuarioService;

@Controller
@RequestMapping("/presencas")
@RequiredArgsConstructor
public class PresencaViewController {

    private final PresencaService presencaService;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("presencas", presencaService.listarTodas());
        return "presencas/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("presenca", new Presenca());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("eventos", eventoService.listarTodos());
        return "presencas/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        try {
            Presenca presenca = presencaService.buscarPorId(id);
            model.addAttribute("presenca", presenca);
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("eventos", eventoService.listarTodos());
            return "presencas/form";
        } catch (Exception e) {
            return "redirect:/presencas";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Presenca presenca, 
                        BindingResult result, 
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("eventos", eventoService.listarTodos());
            return "presencas/form";
        }

        try {
            if (presenca.getDataHora() == null || presenca.getDataHora().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                presenca.setDataHora(LocalDateTime.now().format(formatter));
            }
            
            presencaService.salvar(presenca);
            redirectAttributes.addFlashAttribute("mensagem", "Presença registrada com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/presencas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao registrar presença: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/presencas/novo";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            presencaService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Presença excluída com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir presença: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        return "redirect:/presencas";
    }

}
