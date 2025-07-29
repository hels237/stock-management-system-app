package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.ChangerMotDePasseUtilisateurDto;
import com.k48.stock_management_system.dto.UtilisateurDto;

import java.util.List;

public interface UtilisateurService extends AbstractService<UtilisateurDto> {
    UtilisateurDto save(UtilisateurDto dto);

    UtilisateurDto findById(Integer id);

    List<UtilisateurDto> findAll();

    UtilisateurDto delete(Integer id);

    UtilisateurDto findByEmail(String email);

    UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto);
}
