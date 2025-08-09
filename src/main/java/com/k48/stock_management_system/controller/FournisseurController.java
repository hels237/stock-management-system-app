package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.FournisseurApi;
import com.k48.stock_management_system.dto.FournisseurDto;
import com.k48.stock_management_system.services.FournisseurService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;
@RestController
@Tag(name = "Fournisseur", description = "API pour la gestion des fournisseurs")
@RequestMapping(APP_ROOT+"fournisseurs")
@RequiredArgsConstructor
public class FournisseurController implements FournisseurApi {

    private final FournisseurService fournisseurService;

    @Override
    public FournisseurDto save(FournisseurDto dto) {
        return fournisseurService.save(dto);
    }

    @Override
    public FournisseurDto findById(Integer id) {
        return fournisseurService.findById(id);
    }

    @Override
    public List<FournisseurDto> findAll() {
        return fournisseurService.findAll();
    }

    @Override
    public FournisseurDto delete(Integer id) {
        return fournisseurService.delete(id);
    }
}
