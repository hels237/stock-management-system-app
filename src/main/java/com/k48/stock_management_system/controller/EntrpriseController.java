package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.dto.EntrepriseDto;
import com.k48.stock_management_system.services.EntrepriseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;


@RestController
@RequestMapping(APP_ROOT+"/entreprise")
@RequiredArgsConstructor
public class EntrpriseController {

    private final EntrepriseService entrepriseService;


    @PostMapping("/save")
    public ResponseEntity<?> saveEnterprise(@RequestBody EntrepriseDto entrepriseDto) {
        return ResponseEntity.ok(entrepriseService.save(entrepriseDto));
    }

    @GetMapping("/{entrepriseId}")
    public ResponseEntity<?> findEnterpriseById(@PathVariable Integer entrepriseId) {
        return ResponseEntity.ok(entrepriseService.findById(entrepriseId));
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllEnterprise() {
        return ResponseEntity.ok(entrepriseService.findAll());
    }

    @DeleteMapping("/{entrepriseId}")
    public ResponseEntity<?> deleteEnterpriseById(@PathVariable Integer entrepriseId) {
        return ResponseEntity.ok(entrepriseService.delete(entrepriseId));
    }

}
