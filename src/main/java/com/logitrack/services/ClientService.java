package com.logitrack.services;

import com.logitrack.dto.request.ClientRequestDTO;
import com.logitrack.dto.response.ClientResponseDTO;
import com.logitrack.entities.Client;
import com.logitrack.exception.ResourceNotFoundException;
import com.logitrack.mapper.ClientMapper;
import com.logitrack.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientResponseDTO addClient(ClientRequestDTO request) {
        Client client = clientMapper.toEntity(request);
        client = clientRepository.save(client);
        return clientMapper.toResponseDTO(client);
    }

    public Page<ClientResponseDTO> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toResponseDTO);
    }

    public ClientResponseDTO getClientById(int id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID : " + id));
        return clientMapper.toResponseDTO(client);
    }

    public ClientResponseDTO updateClient(int id, ClientRequestDTO request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID : " + id));
        clientMapper.updateEntity(request, client);
        client = clientRepository.save(client);
        return clientMapper.toResponseDTO(client);
    }

    public void deleteClient(int id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client non trouvé avec l'ID : " + id);
        }
        clientRepository.deleteById(id);
    }
}