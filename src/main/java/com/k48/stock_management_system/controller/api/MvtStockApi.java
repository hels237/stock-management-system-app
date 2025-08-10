package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.MvtStockDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStockApi {

    @GetMapping("/stockReel/{idArticle}")
    @Operation(summary = "Obtenir le stock réel d'un article", description = "Retourne le stock réel de l'article spécifié par son ID")
    @ApiResponse(responseCode = "200", description = "Retourne le stock réel de l'article", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BigDecimal.class)))
    @ApiResponse(responseCode = "404", description = "Aucun stock trouvé pour l'article avec l'ID fourni")
    BigDecimal stockReelArticle(@PathVariable("idArticle") Integer idArticle);

    @GetMapping("/filter/article/{idArticle}")
    @Operation(summary = "Obtenir les mouvements de stock d'un article", description = "Retourne la liste des mouvements de stock pour l'article spécifié par son ID")
    @ApiResponse(responseCode = "200", description = "Retourne la liste des mouvements de stock pour l'article", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MvtStockDto.class, type = "array")))
    @ApiResponse(responseCode = "404", description = "Aucun mouvement de stock trouvé pour l'article avec l'ID fourni")
    List<MvtStockDto> mvtStkArticle(@PathVariable("idArticle") Integer idArticle);

    @PostMapping("/entree")
    @Operation(summary = "Enregistrer une entrée de stock", description = "Cette méthode permet d'enregistrer une entrée de stock pour un article")
    @ApiResponse(responseCode = "200", description = "Enregistre une entrée de stock", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MvtStockDto.class)))
    @ApiResponse(responseCode = "400", description = "La requête est invalide ou les données fournies sont incorrectes")
    MvtStockDto entreeStock(@RequestBody MvtStockDto dto);

    @PostMapping("/sortie")
    @Operation(summary = "Enregistrer une sortie de stock", description = "Cette méthode permet d'enregistrer une sortie de stock pour un article")
    @ApiResponse(responseCode = "200", description = "Enregistre une sortie de stock", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MvtStockDto.class)))
    @ApiResponse(responseCode = "400", description = "La requête est invalide ou les données fournies sont incorrectes")
    MvtStockDto sortieStock(@RequestBody MvtStockDto dto);

    @PostMapping("/correctionpos")
    @Operation(summary = "Corriger une entrée de stock", description = "Cette méthode permet de corriger une entrée de stock pour un article")
    @ApiResponse(responseCode = "200", description = "Corrige une entrée de stock", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MvtStockDto.class)))
    @ApiResponse(responseCode = "400", description = "La requête est invalide ou les données fournies sont incorrectes")
    MvtStockDto correctionStockPos(@RequestBody MvtStockDto dto);


    @PostMapping("/correctionneg")
    @Operation(summary = "Corriger une sortie de stock", description = "Cette méthode permet de corriger une sortie de stock pour un article")
    @ApiResponse(responseCode = "200", description = "Corrige une sortie de stock", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MvtStockDto.class)))
    @ApiResponse(responseCode = "400", description = "La requête est invalide ou les données fournies sont incorrectes")
    MvtStockDto correctionStockNeg(@RequestBody MvtStockDto dto);
}
