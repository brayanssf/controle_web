package com.unincor.presenca.controle_web.model.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "presencas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"participante_id", "evento_id"})
})

public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Participante é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @NotNull(message = "Evento é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "marcada_em", nullable = false)
    private LocalDateTime marcadaEm = LocalDateTime.now();

    @Column(length = 500)
    private String observacoes;

    @Column(name = "presente")
    private Boolean presente = true;

}
