package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.CmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.model.Client;
import com.k48.stock_management_system.model.CmdeClient;
import com.k48.stock_management_system.model.LigneCmdeClient;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.repositories.ClientRepository;
import com.k48.stock_management_system.repositories.CmdeClientRepository;
import com.k48.stock_management_system.repositories.LigneCmdeClientRepository;
import com.k48.stock_management_system.services.CmdeClientService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CmdeClientServiceImpl implements CmdeClientService {

    private final CmdeClientRepository cmdeClientRepository;
    private final ClientRepository clientRepository;
    private final LigneCmdeClientRepository ligneCmdeClientRepository;
    private final ArticleRepository articleRepository;
    private final ObjectValidator<CmdeClientDto> objectValidator;



    @Override
    public CmdeClientDto save(CmdeClientDto cmdeClientDto) {

        //valider la commande
        objectValidator.validate(cmdeClientDto);

        // verifier que le client  existe dans la BD
        Client client =
                clientRepository
                        .findById(cmdeClientDto.getClientDto().getId())
                        .orElseThrow(
                                ()-> new EntityNotFoundException("client with ID {}"+cmdeClientDto.getClientDto().getId()+" not found !")
                        );

        // verifier que l'article existe en BD pour chaque ligne de commande que l'on veut enregistrer
        if(cmdeClientDto.getLigneCmdeClientDtos() != null) {
            cmdeClientDto.getLigneCmdeClientDtos().forEach(ligCmdClt ->{
                if(ligCmdClt.getArticleDto() != null) {
                    Article article =
                            articleRepository
                                    .findById(ligCmdClt.getArticleDto().getId())
                                    .orElseThrow(
                                            ()-> new EntityNotFoundException("article with ID {} "+ligCmdClt.getArticleDto().getId()+"not found in DB!")
                                    );
                }else{
                    log.warn("No Article found in DB we can't finalize the save operation");

                }
            });
        }


        // if everything is good save the cmdClient
        CmdeClient cmdeClient = cmdeClientRepository.save(CmdeClientDto.toEntity(cmdeClientDto));

        // save the LineCmdClient we can't persist the ligneCmdeClient without knowing  whose cmdClient it's belong to
        if(cmdeClientDto.getLigneCmdeClientDtos() != null) {

            cmdeClientDto.getLigneCmdeClientDtos().forEach(ligCmdClt ->{
                LigneCmdeClient ligneCmdeClient = LigneCmdeClientDto.toEntity(ligCmdClt);
                ligneCmdeClient.setCmdeClient( cmdeClient );
                ligneCmdeClientRepository.save(ligneCmdeClient);

            });
        }

        

        return CmdeClientDto.toDto(cmdeClient);
    }

    @Override
    public CmdeClientDto findById(Integer cmdCltId) {
        return
                cmdeClientRepository
                        .findById(cmdCltId).map(CmdeClientDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("cmdClt with ID {} "+cmdCltId+"not found ")
                        );
    }

    @Override
    public List<CmdeClientDto> findAll() {
        List<CmdeClient>  cmdClients = cmdeClientRepository.findAll();
        return
                Optional.of(cmdClients)
                        .filter(listCmdClt -> !listCmdClt.isEmpty())
                        .orElseThrow(
                                ()->new EntityNotFoundException("EMPTY LIST {} no CMD_CLIENT"))
                        .stream()
                        .map(CmdeClientDto::toDto)
                        .toList();
    }

    @Override
    public CmdeClientDto delete(Integer id) {
        if(id == null) {
            log.error("id is null");
            return null;
        }

        //le orElseThrow()  par defaut leve une eexception NoSuchElementFound
        CmdeClient cmdeClient = cmdeClientRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("cmdClient not found with ID " + id));
        cmdeClientRepository.delete(cmdeClient);

        return CmdeClientDto.toDto(cmdeClient);
    }








}
