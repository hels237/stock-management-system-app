package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.VenteApi;
import com.k48.stock_management_system.dto.VenteDto;
import com.k48.stock_management_system.services.VenteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@Tag(name = "Ventes", description = "API pour la gestion des  ventes")
@RestController
@RequestMapping(APP_ROOT + "ventes")
@RequiredArgsConstructor
public class VenteController implements VenteApi {

    private final VenteService venteService;

    @Override
    public VenteDto save(VenteDto dto) {
        return venteService.save(dto);
    }

    @Override
    public VenteDto findById(Integer id) {
        return venteService.findById(id);
    }

    @Override
    public VenteDto findByCode(String code) {
        return venteService.findByCode(code);
    }

    @Override
    public List<VenteDto> findAll() {
        return venteService.findAll();
    }

    @Override
    public VenteDto delete(Integer id) {
        return  venteService.delete(id);
    }
}
