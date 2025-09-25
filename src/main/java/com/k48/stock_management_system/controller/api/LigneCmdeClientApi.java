package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface LigneCmdeClientApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ligne commande client créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @Operation(summary = "Créer une ligne de commande client", description = "Permet de créer une nouvelle ligne de commande client")
    @PostMapping("/create")
    LigneCmdeClientDto save(@RequestBody LigneCmdeClientDto dto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ligne commande client trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Ligne commande client non trouvée")
    })
    @Operation(summary = "Trouver une ligne de commande client par ID", description = "Récupère une ligne de commande client par son ID")
    @GetMapping("/{id}")
    LigneCmdeClientDto findById(@PathVariable("id") Integer id);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des lignes de commande client",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeClientDto.class, type = "array")))
    })
    @Operation(summary = "Lister toutes les lignes de commande client", description = "Récupère toutes les lignes de commande client")
    @GetMapping("/all")
    List<LigneCmdeClientDto> findAll();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lignes de commande client par commande",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeClientDto.class, type = "array")))
    })
    @Operation(summary = "Trouver les lignes par commande client", description = "Récupère toutes les lignes d'une commande client")
    @GetMapping("/commande/{commandeClientId}")
    List<LigneCmdeClientDto> findAllByCommandeClientId(@PathVariable("commandeClientId") Integer commandeClientId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lignes de commande client par article",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeClientDto.class, type = "array")))
    })
    @Operation(summary = "Trouver les lignes par article", description = "Récupère toutes les lignes contenant un article spécifique")
    @GetMapping("/article/{articleId}")
    List<LigneCmdeClientDto> findAllByArticleId(@PathVariable("articleId") Integer articleId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ligne commande client supprimée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Ligne commande client non trouvée")
    })
    @Operation(summary = "Supprimer une ligne de commande client", description = "Supprime une ligne de commande client par son ID")
    @DeleteMapping("/delete/{id}")
    LigneCmdeClientDto delete(@PathVariable("id") Integer id);
}