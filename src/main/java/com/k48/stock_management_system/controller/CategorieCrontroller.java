package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.dto.CategirieDto;
import com.k48.stock_management_system.services.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT+"/categories")
@RequiredArgsConstructor
public class CategorieCrontroller {

    private final CategorieService categorieService;

    @PostMapping("/")
    public ResponseEntity<?> saveCategorie(@RequestBody CategirieDto categirieDto){
        return ResponseEntity.ok(categorieService.save(categirieDto));

    }
    @GetMapping("/{categorieId}")
    public ResponseEntity<?> findById(@PathVariable Integer categorieId){
        return ResponseEntity.ok(categorieService.findById(categorieId));
    }

    @DeleteMapping("/{categorieId}")
    public ResponseEntity<?> deleteCategorie(@PathVariable Integer categorieId){
        return ResponseEntity.ok(categorieService.delete(categorieId));
    }


}
