package com.api.pms.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String resourceName;
    private String password;
}
