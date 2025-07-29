package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.FournisseurDto;

import java.util.List;

public interface FournisseurService extends AbstractService<FournisseurDto> {
    FournisseurDto save(FournisseurDto dto);

    FournisseurDto findById(Integer id);

    List<FournisseurDto> findAll();

    FournisseurDto delete(Integer id);
}
