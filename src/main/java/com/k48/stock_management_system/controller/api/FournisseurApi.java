package com.k48.stock_management_system.controller.api;


import com.k48.stock_management_system.dto.FournisseurDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
public interface FournisseurApi {

    @PostMapping("/create")
    @Operation(summary = "Créer ou modifier un fournisseur", description = "Permet de créer ou de modifier un fournisseur dans le système")
    @ApiResponse(responseCode = "200", description = "L'objet fournisseur cree / modifie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FournisseurDto.class)))
    @ApiResponse(responseCode = "404", description = "L'objet fournisseur n'est pas valide")
    FournisseurDto save(@RequestBody FournisseurDto dto);

    @GetMapping("/{idFournisseur}")
    @Operation(summary = "Trouver un fournisseur par ID", description = "Permet de récupérer un fournisseur en fonction de son ID")
    @ApiResponse(responseCode = "200", description = "L'objet fournisseur trouve", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FournisseurDto.class)))
    @ApiResponse(responseCode = "404", description = "Aucun fournisseur n'existe avec l'ID fourni")
    FournisseurDto findById(@PathVariable("idFournisseur") Integer id);

    @GetMapping("/all")
    @Operation(summary = "Trouver tous les fournisseurs", description = "Permet de récupérer la liste de tous les fournisseurs")
    @ApiResponse(responseCode = "200", description = "La liste des fournisseurs", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FournisseurDto.class, type = "array")))
    List<FournisseurDto> findAll();

    @DeleteMapping("/delete/{idFournisseur}")
    @Operation(summary = "Supprimer un fournisseur", description = "Permet de supprimer un fournisseur en fonction de son ID")
    @ApiResponse(responseCode = "200", description = "Fournisseur supprimé avec succès",content = @Content(mediaType = "application/json", schema = @Schema(implementation = FournisseurDto.class)))
    @ApiResponse(responseCode = "404", description = "Aucun fournisseur n'existe avec l'ID fourni")
    FournisseurDto delete(@PathVariable("idFournisseur") Integer id);
}
