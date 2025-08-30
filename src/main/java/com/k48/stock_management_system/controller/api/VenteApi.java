package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.VenteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface VenteApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "L'objet vente créé ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VenteDto.class))),
            @ApiResponse(responseCode = "400", description = "L'objet vente n'est pas valide")
    })
    @Operation(summary = "Enregistrer une vente", description = "Cette méthode permet de créer une vente")
    @PostMapping("/create")
    VenteDto save(@RequestBody VenteDto dto);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "L'objet vente trouvé", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VenteDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune vente n'existe avec l'ID fourni")
    })
    @Operation(summary = "Trouver une vente par ID", description = "Cette méthode permet de récupérer une vente en fonction de son ID")
    @GetMapping("/{idVente}")
    VenteDto findById(@PathVariable("idVente") Integer id);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "L'objet vente trouvé", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VenteDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune vente n'existe avec le code fourni")
    })
    @Operation(summary = "Trouver une vente par CODE", description = "Cette méthode permet de récupérer une vente en fonction de son code")
    @GetMapping("/{codeVente}")
    VenteDto findByCode(@PathVariable("codeVente") String code);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La liste des ventes", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VenteDto.class, type = "array"))),
    })
    @Operation(summary = "Trouver toutes les ventes", description = "Cette méthode permet de récupérer la liste de toutes les ventes")
    @GetMapping("/all")
    List<VenteDto> findAll();


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La vente a été supprimée de la BD", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VenteDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune vente n'existe avec l'ID fourni")
    })
    @Operation(summary = "Supprimer une vente", description = "Cette méthode permet de supprimer une vente en fonction de son ID")
    @DeleteMapping("/delete/{idVente}")
    VenteDto delete(@PathVariable("idVente") Integer id);
}
