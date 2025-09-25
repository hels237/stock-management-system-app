package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.LigneCmdeClientDto;

import java.util.List;

public interface LigneCmdeClientService {
    
    LigneCmdeClientDto save(LigneCmdeClientDto dto);
    
    LigneCmdeClientDto findById(Integer id);
    
    List<LigneCmdeClientDto> findAll();
    
    List<LigneCmdeClientDto> findAllByCommandeClientId(Integer commandeClientId);
    
    List<LigneCmdeClientDto> findAllByArticleId(Integer articleId);
    
    LigneCmdeClientDto delete(Integer id);
}