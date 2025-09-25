package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;

import java.util.List;

public interface LigneCmdeFournisseurService {
    
    LigneCmdeFournisseurDto save(LigneCmdeFournisseurDto dto);
    
    LigneCmdeFournisseurDto findById(Integer id);
    
    List<LigneCmdeFournisseurDto> findAll();
    
    List<LigneCmdeFournisseurDto> findAllByCommandeFournisseurId(Integer commandeFournisseurId);
    
    List<LigneCmdeFournisseurDto> findAllByArticleId(Integer articleId);
    
    LigneCmdeFournisseurDto delete(Integer id);
}