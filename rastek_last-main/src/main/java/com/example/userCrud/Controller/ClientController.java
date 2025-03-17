package com.example.userCrud.Controller;

import com.example.userCrud.Dto.ClientReq;
import com.example.userCrud.Dto.ClientRes;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Entity.ClientEntity;
import com.example.userCrud.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping(
            path = "/api/create/fromLeads/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ClientRes> createClientFromLead(@PathVariable("id") Long id) {
        ClientRes res = clientService.createClient(id);
        return web_response.<ClientRes>builder().data(res).message("Successfully added client").build();
    }

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

    @GetMapping(
            path = "/api/get/client",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ClientRes> getAll() {
        return clientService.getAllClient();
    }

    @PutMapping(
            path = "/api/update/client/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<ClientRes> updateClient(@PathVariable("id") Long id, @RequestBody ClientReq req) {
        ClientRes res = clientService.updateClientDetails(id, req);
        return web_response.<ClientRes>builder().data(res).message("Client updated").build();
    }

    @DeleteMapping(
            path = "/api/delete/client/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public web_response<String> deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
        return web_response.<String>builder().data("Client deleted").message("Client deleted").build();
    }

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

    @DeleteMapping("/api/client/{id}/delete/profile-picture")
    public web_response<String> deleteClientProfilePicture(@PathVariable Long id) {
        clientService.deleteClientProfilePicture(id);
        return web_response.<String>builder()
                .data(null)
                .message("Client profile picture successfully deleted")
                .build();
    }
}
