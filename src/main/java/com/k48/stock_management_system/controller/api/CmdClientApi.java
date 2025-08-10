package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.CmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.model.EtatCmde;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface CmdClientApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Commande client créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "La requête est invalide ou les données fournies sont incorrectes")
    })
    @Operation(
            summary = "Crée une nouvelle commande client",
            description = "Permet de créer une nouvelle commande client avec les détails fournis dans le DTO."
    )
    @PostMapping("/create")
    CmdeClientDto save(@RequestBody CmdeClientDto dto);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande client mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @Operation(
            summary = "Met à jour l'état de la commande client",
            description = "Permet de mettre à jour l'état d'une commande client en fonction de son ID et de l'état fourni."
    )
    @PatchMapping("/update/etat/{idCommande}/{etatCommande}")
    CmdeClientDto updateEtatCmde(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCmde etatCommande);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantité de la commande client mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @Operation(
            summary = "Met à jour la quantité d'une ligne de commande client",
            description = "Permet de mettre à jour la quantité d'un article dans une commande client en fonction de l'ID de la commande, de l'ID de la ligne de commande et de la nouvelle quantité."
    )
    @PatchMapping("/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    CmdeClientDto updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
                                                             @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client de la commande client mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @Operation(
            summary = "Met à jour le client d'une commande client",
            description = "Permet de mettre à jour le client associé à une commande client en fonction de l'ID de la commande et de l'ID du client."
    )
    @PatchMapping("/update/client/{idCommande}/{idClient}")
    CmdeClientDto updateClient(@PathVariable("idCommande") Integer idCommande, @PathVariable("idClient") Integer idClient);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article de la commande client mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @Operation(
            summary = "Met à jour un article dans une commande client",
            description = "Permet de mettre à jour un article dans une commande client en fonction de l'ID de la commande, de l'ID de la ligne de commande et de l'ID de l'article."
    )
    @PatchMapping("/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    CmdeClientDto updateArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article supprimé de la commande client avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @Operation(
            summary = "Supprime un article d'une commande client",
            description = "Permet de supprimer un article d'une commande client en fonction de l'ID de la commande et de l'ID de la ligne de commande."
    )
    @DeleteMapping("/delete/article/{idCommande}/{idLigneCommande}")
    CmdeClientDto deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande client trouvée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @Operation(
            summary = "Récupère une commande client par son ID",
            description = "Permet de récupérer les détails d'une commande client en fonction de son ID."
    )
    @GetMapping("/{idCommandeClient}")
    CmdeClientDto findById(@PathVariable Integer idCommandeClient);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande client trouvée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec le code fourni")
    })
    @Operation(
            summary = "Récupère une commande client par son code",
            description = "Permet de récupérer les détails d'une commande client en fonction de son code."
    )
    @GetMapping("/filter/{codeCommandeClient}")
    CmdeClientDto findByCode(@PathVariable("codeCommandeClient") String code);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste de toutes les commandes clients",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée")
    })
    @Operation(
            summary = "Récupère toutes les commandes clients",
            description = "Permet de récupérer une liste de toutes les commandes clients existantes."
    )
    @GetMapping("/all")
    List<CmdeClientDto> findAll();


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des lignes de commande client pour la commande spécifiée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeClientDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Aucune ligne de commande client trouvée pour la commande avec l'ID fourni")
    })
    @Operation(
            summary = "Récupère toutes les lignes de commande client pour une commande spécifique",
            description = "Permet de récupérer toutes les lignes de commande associées à une commande client en fonction de l'ID de la commande."
    )
    @GetMapping("/lignesCommande/{idCommande}")
    List<LigneCmdeClientDto> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande client supprimée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande client trouvée avec l'ID fourni")
    })
    @Operation(
            summary = "Supprime une commande client",
            description = "Permet de supprimer une commande client en fonction de son ID."
    )
    @DeleteMapping("/delete/{idCommandeClient}")
    CmdeClientDto delete(@PathVariable("idCommandeClient") Integer id);
}
