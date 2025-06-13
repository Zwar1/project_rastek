package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create")
    public web_response<UserResponse> Create(@RequestBody CreateUserRequest request){
        UserResponse registerUserResponse = userService.create_user(request);
        return web_response.<UserResponse>builder().data(registerUserResponse).message("Success").build();
    }

    @GetMapping("/get/user/{userId}")
    public web_response<UserResponse> getUserById(@PathVariable("userId") Long userId) {
        logger.info("Received request for user ID: {}", userId);
        try {
            UserResponse response = userService.getUser(userId);
            logger.info("Found user: {}", response);
            return web_response.<UserResponse>builder()
                    .data(response)
                    .message("User found")
                    .build();
        } catch (Exception e) {
            logger.error("Error fetching user: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/get")
    public List<UserResponse> GetAll(){
        return userService.getAllUser();
    }

//    @PatchMapping("/addRole")
//    public web_response<UserResponse> AddRole(@RequestBody AddRoleRequest request){
//        UserResponse addRoleUserResponse = userService.addRole(request);
//        return web_response.<UserResponse>builder().data(addRoleUserResponse).message("Success").build();
//    }

    @PutMapping("/update/user/{userId}")
    public web_response<UserResponse> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UpdateUserRequest request
    ) {
        try {
            UserResponse response = userService.updateUser(userId, request);
            return web_response.<UserResponse>builder()
                    .data(response)
                    .message("User updated successfully")
                    .build();
        } catch (Exception e) {
            logger.error("Error updating user: ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")
    public web_response<String> Delete(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return web_response.<String>builder().message("Success").build();
    }

    @PatchMapping("/update/user/{userId}/addRole")
    public web_response<UserResponse> assignRole(@PathVariable("userId") Long userId, @RequestBody AddRoleRequest roleReq) {
        UserResponse assignRoleToUser = userService.addRole(userId, roleReq);
        return web_response.<UserResponse>builder()
                .data(assignRoleToUser)
                .message("Role assigned successfully")
                .build();
    }

    @GetMapping("/api/users/{userId}/roles")
    public web_response<UserResponse> getUserRoles(@PathVariable Long userId) {
        UserResponse user = userService.getUser(userId);
        return web_response.<UserResponse>builder()
                .data(user)
                .message("Success")
                .build();
    }
}