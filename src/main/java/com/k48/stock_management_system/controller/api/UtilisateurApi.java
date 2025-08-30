package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.ChangerMotDePasseUtilisateurDto;
import com.k48.stock_management_system.dto.UtilisateurDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UtilisateurApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès",
                    content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UtilisateurDto.class))),
            @ApiResponse(responseCode = "400", description = "La requête est invalide ou les données fournies sont incorrectes")
    })
    @Operation(summary = "Créer un nouvel utilisateur", description = "Permet de créer un nouvel utilisateur dans le système")
    @PostMapping("/create")
    UtilisateurDto save(@RequestBody UtilisateurDto dto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mot de passe de l'utilisateur mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UtilisateurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun utilisateur trouvé avec l'ID fourni")
    })
    @Operation (summary = "Changer le mot de passe d'un utilisateur", description = "Permet de changer le mot de passe d'un utilisateur")
    @PostMapping("/update/password")
    UtilisateurDto changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto dto);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé avec succès",
                    content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UtilisateurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun utilisateur n'existe avec l'ID fourni")
    })
    @Operation(summary = "Trouver un utilisateur par ID", description = "Permet de récupérer un utilisateur en fonction de son ID")
    @GetMapping("/{idUtilisateur}")
    UtilisateurDto findById(@PathVariable("idUtilisateur") Integer id);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé avec succès",
                    content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UtilisateurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun utilisateur n'existe avec l'email fourni")
    })
    @Operation(summary = "Trouver un utilisateur par email", description = "Permet de récupérer un utilisateur en fonction de son email")
    @GetMapping("/find/{email}")
    UtilisateurDto findByEmail(@PathVariable("email") String email);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs trouvée avec succès",
                    content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UtilisateurDto.class, type = "array"))),
    })
    @Operation(summary = "Trouver tous les utilisateurs", description = "Permet de récupérer la liste de tous les utilisateurs")
    @GetMapping("/all")
    List<UtilisateurDto> findAll();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur supprimé avec succès",
                    content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UtilisateurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucun utilisateur n'existe avec l'ID fourni")
    })
    @Operation(summary = "Supprimer un utilisateur", description = "Permet de supprimer un utilisateur en fonction de son ID")
    @DeleteMapping("/delete/{idUtilisateur}")
    UtilisateurDto delete(@PathVariable("idUtilisateur") Integer id);
}
