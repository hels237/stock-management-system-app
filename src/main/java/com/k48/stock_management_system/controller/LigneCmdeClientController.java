package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.LigneCmdeClientApi;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.services.LigneCmdeClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@RestController
@Tag(name = "Lignes Commande Client", description = "API pour la gestion des lignes de commande client")
@RequestMapping(APP_ROOT + "lignesCommandeClient")
@RequiredArgsConstructor
public class LigneCmdeClientController implements LigneCmdeClientApi {

    private final LigneCmdeClientService service;

    @Override
    public LigneCmdeClientDto save(LigneCmdeClientDto dto) {
        return service.save(dto);
    }

    @Override
    public LigneCmdeClientDto findById(Integer id) {
        return service.findById(id);
    }

    @Override
    public List<LigneCmdeClientDto> findAll() {
        return service.findAll();
    }

    @Override
    public List<LigneCmdeClientDto> findAllByCommandeClientId(Integer commandeClientId) {
        return service.findAllByCommandeClientId(commandeClientId);
    }

    @Override
    public List<LigneCmdeClientDto> findAllByArticleId(Integer articleId) {
        return service.findAllByArticleId(articleId);
    }

    @Override
    public LigneCmdeClientDto delete(Integer id) {
        return service.delete(id);
    }
}