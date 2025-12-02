package com.unincor.presenca.controle_web.model.repository;

import com.unincor.presenca.controle_web.model.domain.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    List<Presenca> findByEventoId(Long eventoId);

    boolean existsByParticipanteIdAndEventoId(Long participanteId, Long eventoId);

    Optional<Presenca> findByParticipanteIdAndEventoId(Long participanteId, Long eventoId);

    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.evento.id = :eventoId AND p.presente = true")
    long contarPresentesPorEvento(@Param("eventoId") Long eventoId);

}
