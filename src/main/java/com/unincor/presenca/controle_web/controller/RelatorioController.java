package com.unincor.presenca.controle_web.controller;

import com.unincor.presenca.controle_web.model.domain.Participante;
import com.unincor.presenca.controle_web.model.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Relatórios", description = "Geração de relatórios (Admin)")
@SecurityRequirement(name = "bearerAuth")

public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/participantes")
    @Operation(summary = "Obter dados de participantes")
    public ResponseEntity<List<Participante>> obterRelatorioParticipantes(
            @RequestParam(required = false) Long eventoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return ResponseEntity.ok(relatorioService.obterRelatorioParticipantes(eventoId, inicio, fim));
    }

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas gerais")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        return ResponseEntity.ok(relatorioService.obterEstatisticas());
    }

    @GetMapping("/participantes/exportar")
    @Operation(summary = "Exportar relatório em Excel")
    public ResponseEntity<byte[]> exportarRelatorio(
            @RequestParam(defaultValue = "xlsx") String formato,
            @RequestParam(required = false) Long eventoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) throws IOException {

        if ("xlsx".equalsIgnoreCase(formato)) {
            byte[] excelData = relatorioService.gerarRelatorioExcel(eventoId, inicio, fim);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "relatorio_participantes.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }

        return ResponseEntity.badRequest().build();
    }

}
