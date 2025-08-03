package com.api.pms.dto;

import java.util.Set;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String resourceName;
    private Long resource_id;
    private Set<String> roles;

    public AuthResponseDto(String resourceName, Long resource_id, Set<String> roles) {
        this.resourceName = resourceName;
        this.roles = roles;
        this.resource_id = resource_id;
    }
}
