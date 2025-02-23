package com.example.userCrud.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AttachmentReq {

    @NotNull
    private Long NIK;

    @NotNull
    private MultipartFile attachment;

}
