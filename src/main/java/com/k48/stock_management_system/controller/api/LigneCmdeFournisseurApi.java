package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface LigneCmdeFournisseurApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ligne commande fournisseur créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @Operation(summary = "Créer une ligne de commande fournisseur", description = "Permet de créer une nouvelle ligne de commande fournisseur")
    @PostMapping("/create")
    LigneCmdeFournisseurDto save(@RequestBody LigneCmdeFournisseurDto dto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ligne commande fournisseur trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Ligne commande fournisseur non trouvée")
    })
    @Operation(summary = "Trouver une ligne de commande fournisseur par ID", description = "Récupère une ligne de commande fournisseur par son ID")
    @GetMapping("/{id}")
    LigneCmdeFournisseurDto findById(@PathVariable("id") Integer id);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des lignes de commande fournisseur",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeFournisseurDto.class, type = "array")))
    })
    @Operation(summary = "Lister toutes les lignes de commande fournisseur", description = "Récupère toutes les lignes de commande fournisseur")
    @GetMapping("/all")
    List<LigneCmdeFournisseurDto> findAll();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lignes de commande fournisseur par commande",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeFournisseurDto.class, type = "array")))
    })
    @Operation(summary = "Trouver les lignes par commande fournisseur", description = "Récupère toutes les lignes d'une commande fournisseur")
    @GetMapping("/commande/{commandeFournisseurId}")
    List<LigneCmdeFournisseurDto> findAllByCommandeFournisseurId(@PathVariable("commandeFournisseurId") Integer commandeFournisseurId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lignes de commande fournisseur par article",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeFournisseurDto.class, type = "array")))
    })
    @Operation(summary = "Trouver les lignes par article", description = "Récupère toutes les lignes contenant un article spécifique")
    @GetMapping("/article/{articleId}")
    List<LigneCmdeFournisseurDto> findAllByArticleId(@PathVariable("articleId") Integer articleId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ligne commande fournisseur supprimée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Ligne commande fournisseur non trouvée")
    })
    @Operation(summary = "Supprimer une ligne de commande fournisseur", description = "Supprime une ligne de commande fournisseur par son ID")
    @DeleteMapping("/delete/{id}")
    LigneCmdeFournisseurDto delete(@PathVariable("id") Integer id);
}