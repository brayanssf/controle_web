package com.unincor.presenca.controle_web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.unincor.presenca.controle_web.model.domain.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    List<Evento> findByNomeContainingIgnoreCase(String nome);
    
    List<Evento> findByData(String data);
    
}