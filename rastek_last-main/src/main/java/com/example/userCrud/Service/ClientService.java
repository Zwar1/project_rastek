package com.example.userCrud.Service;

import com.example.userCrud.Dto.ClientReq;
import com.example.userCrud.Dto.ClientRes;
import com.example.userCrud.Dto.ProfilePictureReq;
import com.example.userCrud.Dto.UserResponse;
import com.example.userCrud.Entity.ClientEntity;
import com.example.userCrud.Entity.LeadsEntity;
import com.example.userCrud.Entity.ProjectEntity;
import com.example.userCrud.Hash.BCrypt;
import com.example.userCrud.Repository.ClientRepository;
import com.example.userCrud.Repository.LeadsRepository;
import com.example.userCrud.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
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

    @Transactional(readOnly = true)
    public Optional<ClientEntity> findById(Long id) {
        return clientRepository.findById(id);
    }


    private ClientRes toClientResponse(ClientEntity client) {
        return ClientRes.builder()
                .id(client.getId())
                .clientName(client.getClientName())
                .clientContact(client.getClientContact())
                .clientEmail(client.getClientEmail())
                .clientCountry(client.getClientCountry())
                .clientAddress(client.getClientAddress())
                .isActive(client.getIsActive())
                .picName(client.getPicName())
                .picNumber(client.getPicNumber())
                .password(client.getPassword())
                .version(client.getVersion())
                .created_by(client.getCreated_by())
                .update_by(client.getUpdate_by())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .user(client.getUser() != null ? UserResponse.builder()
                        .id(client.getUser().getId())
                        .username(client.getUser().getUsername())
                        .email(client.getUser().getEmail())
                        .is_active(client.getUser().is_active())
                        .created_by(client.getUser().getCreated_by())
                        .updated_by(client.getUser().getUpdate_by())
                        .created_at(client.getUser().getCreatedAt())
                        .updated_at(client.getUser().getUpdatedAt())
                        .build() : null)
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
            clientEntity.setClientCountry(req.getClientCountry());
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getClientAddress()) && !req.getClientAddress().isEmpty()) {
            clientEntity.setClientAddress(req.getClientAddress());
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getPicName()) && !req.getPicName().isEmpty()) {
            clientEntity.setPicName(req.getPicName());
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getPicNumber()) && !req.getPicNumber().describeConstable().isEmpty()) {
            clientEntity.setPicNumber(req.getPicNumber());
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getPassword()) && !req.getPassword().isEmpty()) {
            clientEntity.setPassword(BCrypt.hashpw(req.getPassword(), BCrypt.gensalt()));
            clientRepository.save(clientEntity);
        }
        if (Objects.nonNull(req.getIsActive())) {
            clientEntity.setIsActive(req.getIsActive());
            clientRepository.save(clientEntity);
        }
        if (StringUtils.hasText(req.getProfilePicture()) &&
                StringUtils.hasText(req.getProfilePictureType())) {
            try {
                byte[] pictureData = Base64.getDecoder().decode(req.getProfilePicture());
                clientEntity.setProfilePicture(pictureData);
                clientEntity.setProfilePictureType(req.getProfilePictureType());
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid profile picture format");
            }
        }

        clientEntity.setUpdate_by(currentUsername);
        clientRepository.save(clientEntity);
        return toClientResponse(clientEntity);
    }

    @Transactional(readOnly = true)
    public ClientRes getClientDetails(Long clientId) {
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

    @Transactional
    public void deleteClientProfilePicture(Long id) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        client.setProfilePicture(null);
        client.setProfilePictureType(null);
        clientRepository.save(client);
    }

    @Transactional
    public ClientRes updateProfilePicture(Long clientId, ProfilePictureReq request) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        // Validate base64 string, and decode image data, image size, mime type
        try {

            if (!StringUtils.hasText(request.getProfilePicture())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile picture data is required");
            }

            byte[] pictureData = Base64.getDecoder().decode(request.getProfilePicture());

            if (pictureData.length > 5 * 1024 * 1024) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile picture size exceeds 5MB limit");
            }

            String mimeType = request.getProfilePictureType();
            if (!Arrays.asList("image/jpeg", "image/png", "image/gif").contains(mimeType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid image type. Supported types: JPEG, PNG, GIF");
            }

            client.setProfilePicture(pictureData);
            client.setProfilePictureType(mimeType);

            // Update audit fields
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            client.setUpdate_by(currentUsername);

            ClientEntity savedClient = clientRepository.save(client);
            return toClientResponse(savedClient);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base64 image data");
        }
    }
}
