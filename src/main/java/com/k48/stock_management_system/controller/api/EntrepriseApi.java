package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.EntrepriseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.ENTREPRISE_ENDPOINT;

public interface EntrepriseApi {

    @PostMapping("/create")
    @ApiResponse(responseCode = "200", description = "L'objet entreprise créé ",content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntrepriseDto.class)))
    @ApiResponse(responseCode = "404", description = "L'objet entreprise n'est pas valide")
    EntrepriseDto save(@RequestBody EntrepriseDto dto);

    @GetMapping("/{idEntreprise}")
    @ApiResponse(responseCode = "200", description = "L'objet entreprise trouvé", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntrepriseDto.class)))
    @ApiResponse(responseCode = "404", description = "Aucune entreprise n'existe avec l'ID fourni")
    EntrepriseDto findById(@PathVariable("idEntreprise") Integer id);

    @GetMapping(ENTREPRISE_ENDPOINT + "/all")
    @ApiResponse(responseCode = "200", description = "La liste des entreprises", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntrepriseDto.class, type = "array")))

    List<EntrepriseDto> findAll();

    @DeleteMapping(ENTREPRISE_ENDPOINT + "/delete/{idEntreprise}")
    @ApiResponse(responseCode = "200", description = "Entreprise supprimée avec succès", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntrepriseDto.class)))
    @ApiResponse(responseCode = "404", description = "Aucune entreprise n'existe avec l'ID fourni")
    EntrepriseDto delete(@PathVariable("idEntreprise") Integer id);
}
