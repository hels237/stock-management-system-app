package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.CategirieDto;

public interface CategorieService extends AbstractService<CategirieDto> {
    public CategirieDto findByCode(String code);
}
