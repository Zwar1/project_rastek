package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Service.AttachmentService;
import com.example.userCrud.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping(path = "/api/addAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public web_response<AttachmentRes> addProfileImage(AttachmentReq req){
        AttachmentRes attachmentRes = attachmentService.addAttachment(req);
        return web_response.<AttachmentRes>builder().data(attachmentRes).message("Success").build();
    }
}
