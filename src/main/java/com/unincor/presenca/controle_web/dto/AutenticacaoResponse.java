package com.unincor.presenca.controle_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AutenticacaoResponse {

    private String token;
    private String email;
    private String nome;
    private String papel;

}
