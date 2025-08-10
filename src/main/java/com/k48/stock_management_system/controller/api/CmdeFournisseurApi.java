package com.k48.stock_management_system.controller.api;

import com.k48.stock_management_system.dto.CmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.model.EtatCmde;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface CmdeFournisseurApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Commande fournisseur créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "400", description = "La requête est invalide ou les données fournies sont incorrectes")
    })
    @Operation(summary = "Créer  une commande fournisseur", description = "Permet de créer  une commande fournisseur dans le système")
    @PostMapping("/save")
    CmdeFournisseurDto save(@RequestBody CmdeFournisseurDto dto);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "État de la commande fournisseur mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande fournisseur trouvée avec l'ID fourni")
    })
    @Operation(summary = "Mettre à jour l'état de la commande fournisseur", description = "Permet de mettre à jour l'état d'une commande fournisseur")
    @PatchMapping("/update/etat/{idCommande}/{etatCommande}")
    CmdeFournisseurDto updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCmde etatCommande);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantité de la commande fournisseur mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande fournisseur trouvée avec l'ID fourni")
    })
    @Operation(summary = "Mettre à jour la quantité de la commande fournisseur", description = "Permet de mettre à jour la quantité d'une ligne de commande dans une commande fournisseur")
    @PatchMapping("/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    CmdeFournisseurDto updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
                                                  @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fournisseur de la commande fournisseur mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande fournisseur trouvée avec l'ID fourni")
    })
    @Operation(summary = "Mettre à jour le fournisseur de la commande fournisseur", description = "Permet de mettre à jour le fournisseur d'une commande fournisseur")
    @PatchMapping("/update/fournisseur/{idCommande}/{idFournisseur}")
    CmdeFournisseurDto updateFournisseur(@PathVariable("idCommande") Integer idCommande, @PathVariable("idFournisseur") Integer idFournisseur);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article de la commande fournisseur mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande fournisseur trouvée avec l'ID fourni")
    })
    @Operation(summary = "Mettre à jour l'article de la commande fournisseur", description = "Permet de mettre à jour l'article d'une ligne de commande dans une commande fournisseur")
    @PatchMapping("/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    CmdeFournisseurDto updateArticle(@PathVariable("idCommande") Integer idCommande,
                                         @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article supprimé de la commande fournisseur avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande fournisseur trouvée avec l'ID fourni")
    })
    @Operation(summary = "Supprimer un article de la commande fournisseur", description = "Permet de supprimer un article d'une ligne de commande dans une commande fournisseur")
    @DeleteMapping("/delete/article/{idCommande}/{idLigneCommande}")
    CmdeFournisseurDto deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande fournisseur trouvée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande fournisseur trouvée avec l'ID fourni")
    })
    @Operation(summary = "Trouver une commande fournisseur par ID", description = "Permet de récupérer une commande fournisseur en fonction de son ID")
    @GetMapping("/{idCommandeFournisseur}")
    CmdeFournisseurDto findById(@PathVariable("idCommandeFournisseur") Integer id);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande fournisseur trouvée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande fournisseur trouvée avec le code fourni")
    })
    @Operation(summary = "Trouver une commande fournisseur par code", description = "Permet de récupérer une commande fournisseur en fonction de son code")
    @GetMapping("/{codeCommandeFournisseur}")
    CmdeFournisseurDto findByCode(@PathVariable("codeCommandeFournisseur") String code);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste de toutes les commandes fournisseurs récupérée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class)))
    })
    @Operation(summary = "Trouver toutes les commandes fournisseurs", description = "Permet de récupérer la liste de toutes les commandes fournisseurs")
    @GetMapping("/all")
    List<CmdeFournisseurDto> findAll();


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des lignes de commande fournisseur récupérée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LigneCmdeFournisseurDto.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Aucune ligne de commande fournisseur trouvée pour la commande avec l'ID fourni")
    })
    @Operation(summary = "Trouver toutes les lignes de commande fournisseur par ID de commande", description = "Permet de récupérer toutes les lignes de commande d'une commande fournisseur en fonction de son ID")
    @GetMapping("/lignesCommande/{idCommande}")
    List<LigneCmdeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande fournisseur supprimée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CmdeFournisseurDto.class))),
            @ApiResponse(responseCode = "404", description = "Aucune commande fournisseur trouvée avec l'ID fourni")
    })
    @Operation(summary = "Supprimer une commande fournisseur", description = "Permet de supprimer une commande fournisseur en fonction de son ID")
    @DeleteMapping("/delete/{idCommandeFournisseur}")
    CmdeFournisseurDto delete(@PathVariable("idCommandeFournisseur") Integer id);
}
