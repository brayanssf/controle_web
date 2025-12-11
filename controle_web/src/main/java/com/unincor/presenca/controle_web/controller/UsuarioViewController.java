package com.unincor.presenca.controle_web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unincor.presenca.controle_web.model.domain.Usuario;
import com.unincor.presenca.controle_web.model.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioViewController {

    private final UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            model.addAttribute("usuario", usuario);
            return "usuarios/form";
        } catch (Exception e) {
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Usuario usuario, 
                        BindingResult result, 
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "usuarios/form";
        }

        try {
            usuarioService.salvar(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao salvar usuário: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/usuarios/novo";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao excluir usuário: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        return "redirect:/usuarios";
    }

}
