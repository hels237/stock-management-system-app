package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.ClientDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.model.Client;
import com.k48.stock_management_system.repositories.ClientRepository;
import com.k48.stock_management_system.services.AbstractService;
import com.k48.stock_management_system.services.ClientService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ObjectValidator<ClientDto> objectValidator;
    private final ClientRepository clientRepository;


    @Override
    public ClientDto save(ClientDto clientDto) {
        objectValidator.validate(clientDto);
        return ClientDto.toDto(clientRepository.save(ClientDto.toEntity(clientDto)));
    }

    @Override
    public ClientDto findById(Integer id) {
        if(id == null) {
            return null;
        }
        return
                clientRepository
                        .findById(id)
                        .map(ClientDto::toDto)
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
                        .map(ClientDto::toDto)
                        .toList();
    }

    @Override
    public ClientDto delete(Integer id) {
        if(id == null) {
            return null;
        }
        Client client = clientRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("client with ID " + id + " not found"));
        clientRepository.delete(client);

        return ClientDto.toDto(client);
    }
}
