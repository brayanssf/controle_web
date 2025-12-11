package com.unincor.presenca.controle_web.model.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do evento é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @NotBlank(message = "Local é obrigatório")
    @Column(nullable = false)
    private String local;

    @NotBlank(message = "Data é obrigatória")
    @Column(nullable = false)
    private String data;

    @NotBlank(message = "Horário é obrigatório")
    @Column(nullable = false)
    private String horario;

}