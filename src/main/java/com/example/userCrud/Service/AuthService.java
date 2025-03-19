package com.example.userCrud.Service;


import com.example.userCrud.Config.JwtService;
import com.example.userCrud.Dto.AuthRequest;
import com.example.userCrud.Dto.TokenResponse;
import com.example.userCrud.Entity.User;
import com.example.userCrud.Hash.BCrypt;
import com.example.userCrud.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private ValidationService validationService;

    public TokenResponse auth(AuthRequest request){

        validationService.validate(request);
        String Token = "";

        User user_auth = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if(user_auth.is_deleted()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Is Deleted from our World");
        }

        if(BCrypt.checkpw(request.getPassword(), user_auth.getPassword())){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            if (authentication.isAuthenticated()) {
                Token = jwtService.generateToken(request.getUsername());
            }
            return TokenResponse.builder()
                    .token(Token)
                    .username(user_auth.getUsername())
                    .email(user_auth.getEmail())
                    .build();
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Username or password wrong");
        }

    }
}
