package com.example.userCrud.Service;

import com.example.userCrud.Dto.AttachmentReq;
import com.example.userCrud.Dto.AttachmentRes;
import com.example.userCrud.Entity.Attachment;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.User;
import com.example.userCrud.Entity.UserProfile;
import com.example.userCrud.Repository.EmployeeRepository;
import com.example.userCrud.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AttachmentService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ImageStoreService imageStoreService;

    @Transactional
    public AttachmentRes addAttachment(AttachmentReq request) {
        validationService.validate(request);

        EmployeeEntity employeeEntity = employeeRepository.findFirstByNIK(request.getNIK())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        String attachment1 = imageStoreService.uploadAttachment(request.getAttachment());

        Attachment attachment = new Attachment();
        attachment.setAttachment(attachment1);

        employeeEntity.getAttachment().add(attachment);


        return AttachmentRes.builder().attachment(attachment.getAttachment()).build();
    }

}
