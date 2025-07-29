package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.EntrepriseDto;

import java.util.List;

public interface EntrepriseService extends AbstractService<EntrepriseDto> {
    EntrepriseDto save(EntrepriseDto dto);

    EntrepriseDto findById(Integer id);

    List<EntrepriseDto> findAll();

    EntrepriseDto delete(Integer id);
}
