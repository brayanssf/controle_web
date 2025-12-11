package com.unincor.presenca.controle_web.model.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "presencas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Evento é obrigatório")
    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @NotBlank(message = "Tipo de presença é obrigatório")
    @Column(nullable = false)
    private String tipoPresenca;

    @NotBlank(message = "Data e hora são obrigatórias")
    @Column(nullable = false)
    private String dataHora;

}