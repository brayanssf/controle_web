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

@Table(name = "participantes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "evento_id"})
})

public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Evento é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    @Column(name = "inscrito_em", nullable = false, updatable = false)
    private LocalDateTime inscritoEm = LocalDateTime.now();

    @Column(name = "confirmado")
    private Boolean confirmado = false;

    public enum Genero {
        MASCULINO("Masculino"),
        FEMININO("Feminino"),
        OUTRO("Outro"),
        NAO_INFORMAR("Prefiro não informar");

        private final String descricao;

        Genero(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

}
