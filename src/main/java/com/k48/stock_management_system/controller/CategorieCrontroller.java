package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.dto.CategoryDto;
import com.k48.stock_management_system.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT+"/categories")
@RequiredArgsConstructor
public class CategorieCrontroller {

    private final CategoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<?> saveCategorie(@RequestBody CategoryDto categoryDto){
        return ResponseEntity.ok(categoryService.save(categoryDto));

    }
    @GetMapping("/{categorieId}")
    public ResponseEntity<?> findById(@PathVariable Integer categorieId){
        return ResponseEntity.ok(categoryService.findById(categorieId));
    }

    @DeleteMapping("/{categorieId}")
    public ResponseEntity<?> deleteCategorie(@PathVariable Integer categorieId){
        return ResponseEntity.ok(categoryService.delete(categorieId));
    }


}
