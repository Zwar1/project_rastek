package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public web_response<UserResponse> Create(@RequestBody CreateUserRequest request){
        UserResponse registerUserResponse = userService.create_user(request);
        return web_response.<UserResponse>builder().data(registerUserResponse).message("Success").build();
    }

    @GetMapping("/get/{userId}")
    public web_response<UserResponse> Get(@PathVariable("userId") Long userId){
        UserResponse getUserResponse = userService.getUser(userId);
        return web_response.<UserResponse>builder().data(getUserResponse).message("Success").build();
    }

    @GetMapping("/get")
    public List<UserResponse> GetAll(){
        return userService.getAllUser();
    }

    @PatchMapping("/addRole")
    public web_response<UserResponse> AddRole(@RequestBody AddRoleRequest request){
        UserResponse addRoleUserResponse = userService.addRole(request);
        return web_response.<UserResponse>builder().data(addRoleUserResponse).message("Success").build();
    }

    @PatchMapping("/update")
    public web_response<UserResponse> Update(@RequestBody UpdateUserRequest request){
        UserResponse updateUserResponse = userService.updateUser(request);
        return web_response.<UserResponse>builder().data(updateUserResponse).message("Success").build();
    }

    @DeleteMapping("/delete/{userId}")
    public web_response<String> Delete(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return web_response.<String>builder().message("Success").build();
    }

}
