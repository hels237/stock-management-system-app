package com.k48.stock_management_system.controller;


import com.k48.stock_management_system.controller.api.EntrepriseApi;
import com.k48.stock_management_system.dto.EntrepriseDto;
import com.k48.stock_management_system.services.EntrepriseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@RestController
@Tag(name = "Entreprise", description = "API pour la gestion des entreprises")
@RequestMapping(APP_ROOT+"entreprises")
@RequiredArgsConstructor
public class EntrepriseController implements EntrepriseApi {
    private final EntrepriseService entrepriseService;


    @Override
    public EntrepriseDto save(EntrepriseDto dto) {
        return entrepriseService.save(dto);
    }

    @Override
    public EntrepriseDto findById(Integer id) {
        return entrepriseService.findById(id);
    }

    @Override
    public List<EntrepriseDto> findAll() {
        return entrepriseService.findAll();
    }

    @Override
    public EntrepriseDto delete(Integer id) {
        return entrepriseService.delete(id);
    }

}
