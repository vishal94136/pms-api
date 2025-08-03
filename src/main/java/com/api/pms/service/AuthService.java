package com.api.pms.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.pms.Exception.ResourceNotFoundException;
import com.api.pms.dto.AuthResponseDto;
import com.api.pms.entity.Resource;
import com.api.pms.entity.Role;
import com.api.pms.repository.ResourceRepository;


@Service
public class AuthService {
    private final ResourceRepository resourceRepository;

    public AuthService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }
    
    @Transactional(readOnly = true)
    public AuthResponseDto authenticate(String resourceName, String password) {
        Resource resource = resourceRepository.findByName(resourceName)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with name: " + resourceName));
        if (!resource.checkPassword(password)) {
            throw new SecurityException("Invalid password");
        }
        Set<String> roleNames = resource.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new AuthResponseDto(resource.getName(),resource.getId(), roleNames);
    }
    
}
