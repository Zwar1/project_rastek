package com.example.userCrud.Dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesDTO implements Serializable{
    private Long userId;
    private Long roleId;
}
