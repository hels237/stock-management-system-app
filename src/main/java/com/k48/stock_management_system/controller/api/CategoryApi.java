package com.k48.stock_management_system.controller.api;


import com.k48.stock_management_system.dto.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

public interface CategoryApi {

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enregistrer une categorie", description= "Cette methode permet de creer  une cate")
    @ApiResponse(responseCode ="200" , description = "L'objet category cree",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class)))
    @ApiResponse(responseCode ="404", description = "L'objet category n'est pas valide")
    CategoryDto save(@RequestBody CategoryDto dto);


    @GetMapping(value = "/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher une categorie par ID", description = "Cette methode permet de chercher une categorie par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "La categorie a ete trouve dans la BD",
                    content =@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode ="404", description = "Aucune categorie n'existe dans la BDD avec l'ID fourni")
    })
    CategoryDto findById(@PathVariable("idCategory") Integer idCategory);


    @GetMapping(value = "/filter/{codeCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher une categorie par CODE", description="Cette methode permet de chercher une categorie par son CODE")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200",description = "L'article a ete trouve dans la BD",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode="404", description = "Aucun article n'existe dans la BD avec le CODE fourni")
    })
    CategoryDto findByCode(@PathVariable("codeCategory") String codeCategory);

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renvoi la liste des categories", description = "Cette methode permet de chercher et renvoyer la liste des categories qui existent dans la BD")
    @ApiResponse(responseCode = "200", description = "La liste des categories / Une liste vide",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class,type = "array")))
    @ApiResponse(responseCode = "404", description = "Aucune categorie n'existe dans la BD")
    List<CategoryDto> findAll();

    @DeleteMapping(value = "/delete/{idCategory}")
    @Operation(summary= "Supprimer un article", description = "Cette methode permet de supprimer une categorie par ID")
    @ApiResponse(responseCode = "404", description = "Aucune categorie n'existe dans la BD avec l'ID fourni")
    @ApiResponse(responseCode = "400", description = "La categorie n'est pas valide",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class)))
    CategoryDto delete(@PathVariable("idCategory") Integer id);
}
