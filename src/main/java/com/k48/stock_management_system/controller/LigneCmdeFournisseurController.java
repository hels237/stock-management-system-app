package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.LigneCmdeFournisseurApi;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.services.LigneCmdeFournisseurService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@RestController
@Tag(name = "Lignes Commande Fournisseur", description = "API pour la gestion des lignes de commande fournisseur")
@RequestMapping(APP_ROOT + "lignesCommandeFournisseur")
@RequiredArgsConstructor
public class LigneCmdeFournisseurController implements LigneCmdeFournisseurApi {

    private final LigneCmdeFournisseurService service;

    @Override
    public LigneCmdeFournisseurDto save(LigneCmdeFournisseurDto dto) {
        return service.save(dto);
    }

    @Override
    public LigneCmdeFournisseurDto findById(Integer id) {
        return service.findById(id);
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAll() {
        return service.findAll();
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAllByCommandeFournisseurId(Integer commandeFournisseurId) {
        return service.findAllByCommandeFournisseurId(commandeFournisseurId);
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAllByArticleId(Integer articleId) {
        return service.findAllByArticleId(articleId);
    }

    @Override
    public LigneCmdeFournisseurDto delete(Integer id) {
        return service.delete(id);
    }
}