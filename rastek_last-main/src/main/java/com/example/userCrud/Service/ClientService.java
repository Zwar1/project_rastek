package com.example.userCrud.Service;

import com.example.userCrud.Dto.ClientReq;
import com.example.userCrud.Dto.ClientRes;
import com.example.userCrud.Entity.ClientEntity;
import com.example.userCrud.Entity.LeadsEntity;
import com.example.userCrud.Repository.ClientRepository;
import com.example.userCrud.Repository.LeadsRepository;
import com.example.userCrud.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ClientRes createClient(Long leadsId){
        // Find the leads entity
        LeadsEntity leads = leadsRepository.findById(leadsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leads not found"));

        // Check if client already exists with the same name
        if (clientRepository.existsByClientName(leads.getClientName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client already exists");
        }

        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientName(leads.getClientName());
        clientEntity.setClientContact(leads.getClientNumber());
        clientEntity.setClientEmail(leads.getClientEmail());
        clientEntity.setIsActive(true); // default value
        clientEntity.setCreated_by(currentUsername);

        clientRepository.save(clientEntity);
        leads.setStatus("Approved");
        leadsRepository.save(leads);

        return toClientResponse(clientEntity);
    }

    private ClientRes toClientResponse(ClientEntity client) {
        return ClientRes.builder()
                .id(client.getId())
                .clientName(client.getClientName())
                .clientContact(client.getClientContact())
                .clientEmail(client.getClientEmail())
                .clientCountry(client.getClientCountry())
                .isActive(client.getIsActive())
                .version(client.getVersion())
                .created_by(client.getCreated_by())
                .update_by(client.getUpdate_by())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }

    @Transactional
    public ClientRes updateClientDetails(Long clientId, ClientReq req){
        validationService.validate(req);

        ClientEntity clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if (Objects.nonNull(req.getClientName()) && !req.getClientName().isEmpty()) {
            clientEntity.setClientName(req.getClientName());
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getClientContact()) && !req.getClientContact().isEmpty()) {
            clientEntity.setClientCountry(req.getClientContact());
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getClientEmail()) && !req.getClientEmail().isEmpty()) {
            clientEntity.setClientEmail(req.getClientEmail());
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getClientCountry()) && !req.getClientCountry().isEmpty()) {
            clientEntity.setClientCountry( req.getClientCountry());
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getIsActive())) {
            clientEntity.setIsActive(req.getIsActive());
            clientRepository.save(clientEntity);
        }
        clientEntity.setUpdate_by(currentUsername);

        clientRepository.save(clientEntity);
        return toClientResponse(clientEntity);
    }

    @Transactional(readOnly = true)
    public ClientRes getClientDetails(Long clientId){
        ClientEntity clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        return toClientResponse(clientEntity);
    }

    @Transactional(readOnly = true)
    public List<ClientRes> getAllClient() {
        List<ClientEntity> employees = clientRepository.findAll();
        return employees.stream().map(this::toClientResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteClient(Long clientId){
        ClientEntity clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        clientRepository.delete(clientEntity);
    }
}
