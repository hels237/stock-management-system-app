package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.ClientDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.model.Client;
import com.k48.stock_management_system.model.CmdeClient;
import com.k48.stock_management_system.repositories.ClientRepository;
import com.k48.stock_management_system.repositories.CmdeClientRepository;
import com.k48.stock_management_system.services.ClientService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ObjectValidator<ClientDto> objectValidator;
    private final ClientRepository clientRepository;
    private final CmdeClientRepository commandeClientRepository;


    @Override
    public ClientDto save(ClientDto clientDto) {
        objectValidator.validate(clientDto);
        return ClientDto.fromEntity(clientRepository.save(ClientDto.toEntity(clientDto)));
    }

    @Override
    public ClientDto findById(Integer id) {
        if(id == null) {
            return null;
        }
        return
                clientRepository
                        .findById(id)
                        .map(ClientDto::fromEntity)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("client with ID " + id + " not found"+ ErrorCode.ARTICLE_NOT_FOUND)
                        );
    }

    @Override
    public List<ClientDto> findAll() {

        List<Client> clients = clientRepository.findAll();

        return
                Optional.of(clients)
                        .filter(list-> !list.isEmpty())
                        .orElseThrow(
                                ()-> new EntityNotFoundException(" EMPTY LIST OF CLIENTS")
                        )
                        .stream()
                        .map(ClientDto::fromEntity)
                        .toList();
    }

    @Override
    public ClientDto delete(Integer id) {
        if(id == null) {
            return null;
        }
        Client client = clientRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("client with ID " + id + " not found"));
        List<CmdeClient> commandeClients = commandeClientRepository.findAllByClientId(id);
        if (!commandeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un client qui a deja des commande clients",
                    ErrorCode.CLIENT_ALREADY_IN_USE);
        }
        clientRepository.delete(client);

        return ClientDto.fromEntity(client);
    }
}
