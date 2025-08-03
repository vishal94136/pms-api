package com.api.pms.dto;

import java.util.Set;

import lombok.Data;

@Data
public class ResourceDto {

    private Long id;
    private String name;
    private String password;
    private Set<RoleDto> roles;
    private Long managerId;
}
