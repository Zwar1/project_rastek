package com.example.userCrud.Service;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.UserProfile;
import com.example.userCrud.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class
UserProfileService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ImageStoreService imageStoreService;

    @Transactional
    public ProfileImageRes addProfileImage(ProfileImageReq request) {
        validationService.validate(request);

        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        String ImageUrl = imageStoreService.uploadImage(request.getProfileImage());

        UserProfile userProfile = new UserProfile();
        userProfile.setProfilePicture(ImageUrl);

        employeeEntity.setUserProfile(userProfile);

        return ProfileImageRes.builder().ProfileImage(userProfile.getProfilePicture()).build();

    }

}
