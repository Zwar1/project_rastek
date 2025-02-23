package com.example.userCrud.Service;


import com.example.userCrud.Dto.LeadsReq;
import com.example.userCrud.Dto.LeadsRes;
import com.example.userCrud.Entity.ClientEntity;
import com.example.userCrud.Entity.LeadsEntity;
import com.example.userCrud.Entity.User;
import com.example.userCrud.Hash.BCrypt;
import com.example.userCrud.Repository.ClientRepository;
import com.example.userCrud.Repository.LeadsRepository;
import com.example.userCrud.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadsService {

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public LeadsRes CreateLeads(LeadsReq req){
        validationService.validate(req);

        if(leadsRepository.existsByClientName(req.getClientName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client already exists");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        LeadsEntity leads = new LeadsEntity();

        leads.setClientName(req.getClientName());
        leads.setClientEmail(req.getClientEmail());
        leads.setClientNumber(req.getClientNumber());
        leads.setCreated_by(currentUsername);
        leads.setStatus("Pending");

        leadsRepository.save(leads);

        return toResponse(leads);
    }

    @Transactional
    public LeadsRes updateStatusToAccepted(Long id){

        LeadsEntity leads = leadsRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leads not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        leads.setApproved(true);
        leads.setRejected(false);
        leads.setStatus("Accepted");

        leadsRepository.save(leads);

        ClientEntity client = new ClientEntity();

        client.setClientName(leads.getClientName());
        client.setClientEmail(leads.getClientEmail());
        client.setClientContact(leads.getClientNumber());
        client.setCreated_by(currentUsername);

        User user = new User();

        user.setUsername(leads.getClientName());
        user.setPassword(BCrypt.hashpw(leads.getClientNumber() + leads.getClientName() + "@" + leads.getId(), BCrypt.gensalt()));
        user.setEmail(leads.getClientEmail());
        user.setCreated_by(currentUsername);
        userRepository.save(user);

        client.setUser(user);
        clientRepository.save(client);

        return toResponse(leads);
    }

    @Transactional
    public LeadsRes updateStatusToRejected(Long id){

        LeadsEntity leads = leadsRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leads not found"));

        leads.setRejected(true);
        leads.setApproved(false);
        leads.setStatus("Rejected");

        leadsRepository.save(leads);

        return toResponse(leads);
    }

    @Transactional(readOnly = true)
    public List<LeadsRes> getAllLeads(){
        List<LeadsEntity>  leadsEntityList = leadsRepository.findAll();

        return leadsEntityList.stream().map(
                leads -> LeadsRes.builder().id(leads.getId())
                        .clientNumber(leads.getClientNumber())
                        .clientEmail(leads.getClientEmail())
                        .clientName(leads.getClientName())
                        .status(leads.getStatus())
                        .isApproved(leads.isApproved())
                        .isRejected(leads.isRejected())
                        .version(leads.getVersion())
                        .created_by(leads.getCreated_by())
                        .update_by(leads.getUpdate_by())
                        .createdAt(leads.getCreatedAt())
                        .updatedAt(leads.getUpdatedAt())
                        .build()).collect(Collectors.toList());
    }

    public LeadsRes toResponse(LeadsEntity leads){
        return LeadsRes.builder()
                .id(leads.getId())
                .clientName(leads.getClientName())
                .clientEmail(leads.getClientEmail())
                .clientNumber(leads.getClientNumber())
                .status(leads.getStatus())
                .created_by(leads.getCreated_by())
                .update_by(leads.getUpdate_by())
                .createdAt(leads.getCreatedAt())
                .updatedAt(leads.getUpdatedAt())
                .version(leads.getVersion())
                .isApproved(leads.isApproved())
                .isRejected(leads.isRejected())
                .build();
    }
}
