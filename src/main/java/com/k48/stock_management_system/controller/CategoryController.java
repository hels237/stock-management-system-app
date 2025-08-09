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


    @Override
    public CategoryDto save(CategoryDto dto) {
        return null;
    }

    @Override
    public CategoryDto findById(Integer idCategory) {
        return null;
    }

    @Override
    public CategoryDto findByCode(String codeCategory) {
        return null;
    }

    @Override
    public List<CategoryDto> findAll() {
        return List.of();
    }

    @Override
    public void delete(Integer id) {

    }
}
