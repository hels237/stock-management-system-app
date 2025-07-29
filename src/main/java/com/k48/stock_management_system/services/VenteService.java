package com.k48.stock_management_system.services;


import com.k48.stock_management_system.dto.VenteDto;

import java.util.List;

public interface VenteService extends AbstractService<VenteDto> {
    VenteDto save(VenteDto dto);

    VenteDto findById(Integer id);

    VenteDto findByCode(String code);

    List<VenteDto> findAll();

    VenteDto delete(Integer id);
}
