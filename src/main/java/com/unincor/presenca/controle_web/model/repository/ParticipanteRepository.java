package com.unincor.presenca.controle_web.model.repository;

import com.unincor.presenca.controle_web.model.domain.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    List<Participante> findByEventoId(Long eventoId);

    List<Participante> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);

    Optional<Participante> findByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);

    @Query("SELECT p FROM Participante p WHERE p.evento.id = :eventoId "
            + "AND p.inscritoEm BETWEEN :inicio AND :fim")
    List<Participante> buscarPorEventoEPeriodo(
            @Param("eventoId") Long eventoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("SELECT COUNT(p) FROM Participante p WHERE p.evento.id = :eventoId")
    long contarPorEvento(@Param("eventoId") Long eventoId);
    
}
