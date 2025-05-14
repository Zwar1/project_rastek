package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ClientReq;
import com.example.userCrud.Dto.ClientRes;
import com.example.userCrud.Dto.ProfilePictureReq;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Entity.ClientEntity;
import com.example.userCrud.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ClientController {
    private static final String CLIENTS_CLIENT_DATA_VIEW = "CLIENTS:CLIENT DATA:VIEW";
    private static final String CLIENTS_CLIENT_DETAILS_VIEW = "CLIENTS:CLIENT DETAILS:VIEW";
    private static final String CLIENTS_CLIENT_DETAILS_EDIT = "CLIENTS:LEADS:APPROVE/REJECT";

    @Autowired
    private ClientService clientService;

    // Test API
    @PostMapping(
            path = "/api/create/fromLeads/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ClientRes> createClientFromLead(@PathVariable("id") Long id) {
        ClientRes res = clientService.createClient(id);
        return web_response.<ClientRes>builder().data(res).message("Successfully added client").build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_CLIENT_DETAILS_VIEW + "')")
    @GetMapping(
            path = "/api/get/client/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ClientRes> getClientById(@PathVariable("id") Long id) {
        ClientRes res = clientService.getClientDetails(id);
        return web_response.<ClientRes>builder()
                .data(res)
                .message("Client found with associated user data")
                .build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_CLIENT_DATA_VIEW + "')")
    @GetMapping(
            path = "/api/get/client",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ClientRes> getAll() {
        return clientService.getAllClient();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_CLIENT_DETAILS_EDIT + "')")
    @PutMapping(
            path = "/api/update/client/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ClientRes> updateClient(@PathVariable("id") Long id, @RequestBody ClientReq req) {
        ClientRes res = clientService.updateClientDetails(id, req);
        return web_response.<ClientRes>builder().data(res).message("Client updated").build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_CLIENT_DETAILS_EDIT + "')")
    @DeleteMapping(
            path = "/api/delete/client/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
        return web_response.<String>builder().data("Client deleted").message("Client deleted").build();
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_CLIENT_DETAILS_EDIT + "')")
    @GetMapping("/api/client/{id}/profile-picture")
    public ResponseEntity<byte[]> getClientProfilePicture(@PathVariable Long id) {
        ClientEntity client = clientService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        if (client.getProfilePicture() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(client.getProfilePictureType()))
                .body(client.getProfilePicture());
    }

    @PreAuthorize("hasAuthority('" + CLIENTS_CLIENT_DETAILS_VIEW + "')")
    @PutMapping("/api/update/client/{id}/profile-picture")
    public web_response<ClientRes> updateClientProfilePicture(
            @PathVariable("id") Long id,
            @RequestBody ProfilePictureReq request) {
        ClientRes res = clientService.updateProfilePicture(id, request);
        return web_response.<ClientRes>builder()
                .data(res)
                .message("Profile picture updated successfully")
                .build();
    }

    @DeleteMapping("/api/client/{id}/delete/profile-picture")
    public web_response<String> deleteClientProfilePicture(@PathVariable Long id) {
        clientService.deleteClientProfilePicture(id);
        return web_response.<String>builder()
                .data(null)
                .message("Client profile picture successfully deleted")
                .build();
    }
}
