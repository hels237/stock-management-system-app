package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.CategoryApi;
import com.k48.stock_management_system.dto.CategoryDto;
import com.k48.stock_management_system.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT+"/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    public CategoryDto save(CategoryDto dto) {
        return categoryService.save(dto);
    }

    @Override
    public CategoryDto findById(Integer idCategory) {
        return categoryService.findById(idCategory);
    }

    @Override
    public CategoryDto findByCode(String codeCategory) {
        return categoryService.findByCode(codeCategory);
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @Override
    public CategoryDto delete(Integer id) {
        return categoryService.delete(id);
    }
}
