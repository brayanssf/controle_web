package com.unincor.presenca.controle_web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.unincor.presenca.controle_web.model.domain.Evento;
import com.unincor.presenca.controle_web.model.domain.Presenca;
import com.unincor.presenca.controle_web.model.domain.Usuario;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    
    List<Presenca> findByUsuario(Usuario usuario);
    
    List<Presenca> findByEvento(Evento evento);
    
    List<Presenca> findByUsuarioAndEvento(Usuario usuario, Evento evento);
    
    List<Presenca> findByTipoPresenca(String tipoPresenca);
    
}
