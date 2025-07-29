package com.k48.stock_management_system.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Builder
public class ChangerMotDePasseUtilisateurDto {
    private Integer id;

    private String motDePasse;

    private String confirmMotDePasse;
}
