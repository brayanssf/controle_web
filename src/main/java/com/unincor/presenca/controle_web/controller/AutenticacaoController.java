package com.unincor.presenca.controle_web.controller;

import com.unincor.presenca.controle_web.dto.AutenticacaoRequest;
import com.unincor.presenca.controle_web.dto.AutenticacaoResponse;
import com.unincor.presenca.controle_web.dto.RegistroRequest;
import com.unincor.presenca.controle_web.model.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")

public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService = null;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar novo usuário")
    public ResponseEntity<AutenticacaoResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        try {
            AutenticacaoResponse response = autenticacaoService.registrar(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login")
    public ResponseEntity<AutenticacaoResponse> login(@Valid @RequestBody AutenticacaoRequest request) {
        try {
            AutenticacaoResponse response = autenticacaoService.autenticar(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
