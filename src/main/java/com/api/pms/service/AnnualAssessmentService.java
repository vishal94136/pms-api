package com.api.pms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.pms.Exception.ResourceNotFoundException;
import com.api.pms.dto.AnnualAssessmentDto;
import com.api.pms.entity.AnnualAssessment;
import com.api.pms.entity.Resource;
import com.api.pms.entity.Role;
import com.api.pms.repository.AnnualAssessmentRepository;
import com.api.pms.repository.ResourceRepository;
import com.api.pms.repository.RoleRepository;


@Service
public class AnnualAssessmentService {
    private final AnnualAssessmentRepository annualAssessmentRepository;
    private final ResourceRepository resourceRepository;
    private final RoleRepository roleRepository;

    public AnnualAssessmentService(AnnualAssessmentRepository annualAssessmentRepository,
                                   ResourceRepository resourceRepository,
                                   RoleRepository roleRepository) {
        this.annualAssessmentRepository = annualAssessmentRepository;
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public AnnualAssessmentDto createAssessment(AnnualAssessmentDto dto) {
        AnnualAssessment assessment = new AnnualAssessment();

        Resource resource = resourceRepository.findById(dto.getResource_id())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + dto.getResource_id()));
        assessment.setResource(resource);

        Role role = roleRepository.findById(dto.getRole_id())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + dto.getRole_id()));
        assessment.setRole(role);

        Resource manager = resourceRepository.findById(dto.getManager_id())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + dto.getManager_id()));
        assessment.setManager(manager);

        assessment.setRating(dto.getRating());
        assessment.setGoalStatus(dto.getGoalStatus());
        assessment.setAssessmentStatus(dto.getAssessmentStatus());
        assessment.setYear(dto.getYear());

        AnnualAssessment saved = annualAssessmentRepository.save(assessment);
        return toAnnualAssessmentDto(saved);
    }
    
    @Transactional(readOnly = true)
    public List<AnnualAssessmentDto> getAssessmentsForResource(Long resourceId) {
        return annualAssessmentRepository.findByResourceId(resourceId).stream()
                .map(this::toAnnualAssessmentDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnnualAssessmentDto> getAllForManagerAndSubordinates(Long managerId) {
        resourceRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));

        List<AnnualAssessment> managerAssessments = annualAssessmentRepository.findByResourceId(managerId);

        List<Resource> subordinates = resourceRepository.findByManagerId(managerId);
        List<AnnualAssessment> subordinateAssessments = subordinates.stream()
                .flatMap(sub -> annualAssessmentRepository.findByResourceId(sub.getId()).stream())
                .collect(Collectors.toList());

        managerAssessments.addAll(subordinateAssessments);
        return managerAssessments.stream()
                .map(this::toAnnualAssessmentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AnnualAssessmentDto updateAssessment(Long id, AnnualAssessmentDto dto) {
        return annualAssessmentRepository.findById(id)
                .map(assessment -> {
                    if (dto.getResource_id() != null) {
                        Resource resource = resourceRepository.findById(dto.getResource_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + dto.getResource_id()));
                        assessment.setResource(resource);
                    }

                    if (dto.getRole_id() != null) {
                        Role role = roleRepository.findById(dto.getRole_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + dto.getRole_id()));
                        assessment.setRole(role);
                    }

                    if (dto.getManager_id() != null) {
                        Resource manager = resourceRepository.findById(dto.getManager_id())
                                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + dto.getManager_id()));
                        assessment.setManager(manager);
                    }

                    if (dto.getRating() != null) {
                        assessment.setRating(dto.getRating());
                    }

                    if (dto.getAssessmentStatus() != null) {
                        assessment.setAssessmentStatus(dto.getAssessmentStatus());
                    }

                    if (dto.getGoalStatus() != null) {
                        assessment.setGoalStatus(dto.getGoalStatus());
                    }

                    if (dto.getYear() != null) {
                        assessment.setYear(dto.getYear());
                    }

                    AnnualAssessment updated = annualAssessmentRepository.save(assessment);
                    return toAnnualAssessmentDto(updated);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with ID: " + id));
    }
    
    private AnnualAssessmentDto toAnnualAssessmentDto(AnnualAssessment assessment) {
        AnnualAssessmentDto dto = new AnnualAssessmentDto();
        dto.setId(assessment.getId());
        dto.setResource_id(assessment.getResource().getId());
        dto.setResourceName(assessment.getResource().getName());
        dto.setRole_id(assessment.getRole().getId());
        dto.setRoleName(assessment.getRole().getName());
        dto.setManager_id(assessment.getManager().getId());
        dto.setManagerName(assessment.getManager().getName());
        dto.setRating(assessment.getRating());
        dto.setAssessmentStatus(assessment.getAssessmentStatus());
        dto.setGoalStatus(assessment.getGoalStatus());
        dto.setYear(assessment.getYear());
        return dto;
    }
}
