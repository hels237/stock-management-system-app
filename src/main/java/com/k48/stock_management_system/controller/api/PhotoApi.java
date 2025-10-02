package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.PhotoUploadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo téléchargée avec succès",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Fichier invalide ou erreur de téléchargement")
    })
    @Operation(summary = "Télécharger une photo", description = "Permet de télécharger une photo pour une entité (article, utilisateur, client, fournisseur, entreprise)")
    @PostMapping(value = "/upload/{entityType}/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PhotoUploadResponseDto uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @PathVariable("entityType") String entityType,
            @PathVariable("entityId") Integer entityId
    );

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Photo non trouvée")
    })
    @Operation(summary = "Supprimer une photo", description = "Permet de supprimer une photo en utilisant son ID public")
    @DeleteMapping("/delete/{publicId}")
    void deletePhoto(@PathVariable("publicId") String publicId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo de l'article récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Article ou photo non trouvé")
    })
    @Operation(summary = "Afficher la photo d'un article", description = "Récupère la photo d'un article par son ID avec mise en cache")
    @GetMapping("/article/{id}/photo")
    ResponseEntity<byte[]> getArticlePhoto(@PathVariable("id") Integer id);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo de l'utilisateur récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou photo non trouvé")
    })
    @Operation(summary = "Afficher la photo d'un utilisateur", description = "Récupère la photo d'un utilisateur par son ID avec mise en cache")
    @GetMapping("/utilisateur/{id}/photo")
    ResponseEntity<byte[]> getUtilisateurPhoto(@PathVariable("id") Integer id);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo du client récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Client ou photo non trouvé")
    })
    @Operation(summary = "Afficher la photo d'un client", description = "Récupère la photo d'un client par son ID avec mise en cache")
    @GetMapping("/client/{id}/photo")
    ResponseEntity<byte[]> getClientPhoto(@PathVariable("id") Integer id);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo du fournisseur récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Fournisseur ou photo non trouvé")
    })
    @Operation(summary = "Afficher la photo d'un fournisseur", description = "Récupère la photo d'un fournisseur par son ID avec mise en cache")
    @GetMapping("/fournisseur/{id}/photo")
    ResponseEntity<byte[]> getFournisseurPhoto(@PathVariable("id") Integer id);
}
