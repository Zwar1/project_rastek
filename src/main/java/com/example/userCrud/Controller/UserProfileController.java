package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.UserProfileService;
import com.example.userCrud.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping(path = "/api/addProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public web_response<ProfileImageRes> addProfileImage(ProfileImageReq req){
        ProfileImageRes profileImageRes = userProfileService.addProfileImage(req);
        return web_response.<ProfileImageRes>builder().data(profileImageRes).message("Success").build();
    }

}
