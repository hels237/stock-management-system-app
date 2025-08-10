package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.EntrepriseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.ENTREPRISE_ENDPOINT;

public interface EntrepriseApi {

    @PostMapping("/create")
    @Operation(summary = "Créer une nouvelle entreprise", description = "Permet de créer une nouvelle entreprise dans le système")
    @ApiResponse(responseCode = "200", description = "L'objet entreprise créé ",content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntrepriseDto.class)))
    @ApiResponse(responseCode = "404", description = "L'objet entreprise n'est pas valide")
    EntrepriseDto save(@RequestBody EntrepriseDto dto);

    @GetMapping("/{idEntreprise}")
    @Operation(summary = "Trouver une entreprise par ID", description = "Permet de récupérer une entreprise en fonction de son ID")
    @ApiResponse(responseCode = "200", description = "L'objet entreprise trouvé", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntrepriseDto.class)))
    @ApiResponse(responseCode = "404", description = "Aucune entreprise n'existe avec l'ID fourni")
    EntrepriseDto findById(@PathVariable("idEntreprise") Integer id);

    @GetMapping(ENTREPRISE_ENDPOINT + "/all")
    @Operation(summary = "Trouver toutes les entreprises", description = "Permet de récupérer la liste de toutes les entreprises")
    @ApiResponse(responseCode = "200", description = "La liste des entreprises", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntrepriseDto.class, type = "array")))
    List<EntrepriseDto> findAll();

    @DeleteMapping(ENTREPRISE_ENDPOINT + "/delete/{idEntreprise}")
    @Operation(summary = "Supprimer une entreprise", description = "Permet de supprimer une entreprise en fonction de son ID")
    @ApiResponse(responseCode = "200", description = "Entreprise supprimée avec succès", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntrepriseDto.class)))
    @ApiResponse(responseCode = "404", description = "Aucune entreprise n'existe avec l'ID fourni")
    EntrepriseDto delete(@PathVariable("idEntreprise") Integer id);
}
