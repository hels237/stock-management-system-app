package com.k48.stock_management_system.controller;


import com.k48.stock_management_system.controller.api.UtilisateurApi;
import com.k48.stock_management_system.dto.ChangerMotDePasseUtilisateurDto;
import com.k48.stock_management_system.dto.UtilisateurDto;
import com.k48.stock_management_system.services.UtilisateurService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@Tag(name = "Utilisateur", description = "API pour la gestion des  utilisateurs")
@RestController
@RequestMapping(APP_ROOT + "utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController implements UtilisateurApi {

    private final UtilisateurService utilisateurService;

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
        return utilisateurService.save(dto);
    }

    @Override
    public UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) {
        return utilisateurService.changerMotDePasse(dto);
    }

    @Override
    public UtilisateurDto findById(Integer id) {
        return utilisateurService.findById(id);
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        return utilisateurService.findByEmail(email);
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurService.findAll();
    }

    @Override
    public UtilisateurDto delete(Integer id) {
        return utilisateurService.delete(id);
    }
}
