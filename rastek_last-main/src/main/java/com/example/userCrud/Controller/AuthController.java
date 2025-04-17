package com.example.userCrud.Controller;

import com.example.userCrud.Config.JwtService;
import com.example.userCrud.Dto.AuthRequest;
import com.example.userCrud.Dto.TokenResponse;
import com.example.userCrud.Dto.web_response;
import com.example.userCrud.Entity.User;
import com.example.userCrud.Repository.UserRepository;
import com.example.userCrud.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
    @PostMapping("/login")
    public web_response<TokenResponse> authAndLogin(@RequestBody AuthRequest request){
        TokenResponse tokenResponse = authService.auth(request);
        return web_response.<TokenResponse>builder().data(tokenResponse).message("Success").build();
    }

}
