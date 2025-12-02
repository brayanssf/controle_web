package com.unincor.presenca.controle_web.controller;

import com.unincor.presenca.controle_web.model.service.EventoService;
import com.unincor.presenca.controle_web.model.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class RelatorioViewController {

    private final RelatorioService relatorioService;
    private final EventoService eventoService;

    @GetMapping
    public String visualizar(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        model.addAttribute("estatisticas", relatorioService.obterEstatisticas());
        return "relatorios/visualizar";
    }

}
