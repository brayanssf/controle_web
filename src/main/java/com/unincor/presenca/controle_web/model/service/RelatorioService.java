package com.unincor.presenca.controle_web.model.service;

import com.unincor.presenca.controle_web.model.domain.Evento;
import com.unincor.presenca.controle_web.model.domain.Participante;
import com.unincor.presenca.controle_web.model.repository.EventoRepository;
import com.unincor.presenca.controle_web.model.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class RelatorioService {
    
    private final ParticipanteRepository participanteRepository;
    private final EventoRepository eventoRepository;
    
    public List<Participante> obterRelatorioParticipantes(Long eventoId, LocalDateTime inicio, LocalDateTime fim) {
        if (eventoId != null && inicio != null && fim != null) {
            return participanteRepository.buscarPorEventoEPeriodo(eventoId, inicio, fim);
        } else if (eventoId != null) {
            return participanteRepository.findByEventoId(eventoId);
        } else {
            return participanteRepository.findAll();
        }
    }
    
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        List<Participante> todosParticipantes = participanteRepository.findAll();

        Map<String, Long> distribuicaoGenero = new HashMap<>();
        for (Participante.Genero genero : Participante.Genero.values()) {
            long count = todosParticipantes.stream()
                .filter(p -> p.getGenero() == genero)
                .count();
            distribuicaoGenero.put(genero.getDescricao(), count);
        }
        estatisticas.put("distribuicaoGenero", distribuicaoGenero);
        
        List<Evento> eventos = eventoRepository.findByAtivoTrue();
        Map<String, Integer> participantesPorEvento = new HashMap<>();
        for (Evento evento : eventos) {
            int count = participanteRepository.findByEventoId(evento.getId()).size();
            participantesPorEvento.put(evento.getTitulo(), count);
        }
        estatisticas.put("participantesPorEvento", participantesPorEvento);
        
        estatisticas.put("totalParticipantes", todosParticipantes.size());
        estatisticas.put("totalEventos", eventos.size());
        
        return estatisticas;
    }
    
    public byte[] gerarRelatorioExcel(Long eventoId, LocalDateTime inicio, LocalDateTime fim) throws IOException {
        List<Participante> participantes = obterRelatorioParticipantes(eventoId, inicio, fim);
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Participantes");
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            Row headerRow = sheet.createRow(0);
            String[] colunas = {"ID", "Nome", "Email", "Evento", "Gênero", "Data Inscrição", "Confirmado"};
            for (int i = 0; i < colunas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colunas[i]);
                cell.setCellStyle(headerStyle);
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            int rowNum = 1;
            for (Participante p : participantes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getUsuario().getNome());
                row.createCell(2).setCellValue(p.getUsuario().getEmail());
                row.createCell(3).setCellValue(p.getEvento().getTitulo());
                row.createCell(4).setCellValue(p.getGenero().getDescricao());
                row.createCell(5).setCellValue(p.getInscritoEm().format(formatter));
                row.createCell(6).setCellValue(p.getConfirmado() ? "Sim" : "Não");
            }

            for (int i = 0; i < colunas.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }

}