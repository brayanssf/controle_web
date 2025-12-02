package com.unincor.presenca.controle_web.model.repository;

import com.unincor.presenca.controle_web.model.domain.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByAtivoTrue();

    List<Evento> findByDataInicioBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT e FROM Evento e WHERE e.ativo = true ORDER BY e.dataInicio DESC")
    List<Evento> listarEventosAtivosOrdenados();

}
