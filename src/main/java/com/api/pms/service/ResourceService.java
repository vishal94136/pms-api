package com.api.pms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.pms.Exception.ResourceNotFoundException;
import com.api.pms.dto.GoalDto;
import com.api.pms.dto.GoalsWithStatusDto;
import com.api.pms.dto.ResourceDto;
import com.api.pms.dto.RoleDto;
import com.api.pms.entity.AnnualAssessment;
import com.api.pms.entity.Goal;
import com.api.pms.entity.Resource;
import com.api.pms.entity.Role;
import com.api.pms.repository.AnnualAssessmentRepository;
import com.api.pms.repository.GoalRepository;
import com.api.pms.repository.ResourceRepository;
import com.api.pms.repository.RoleRepository;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final RoleRepository roleRepository;
    private final GoalRepository goalRepository;
    private final AnnualAssessmentRepository annualAssessmentRepository;

    public ResourceService(ResourceRepository resourceRepository, RoleRepository roleRepository, GoalRepository goalRepository, AnnualAssessmentRepository annualAssessmentRepository) {
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
        this.goalRepository = goalRepository;
        this.annualAssessmentRepository = annualAssessmentRepository;
    }

    @Transactional
    public ResourceDto createResource(ResourceDto resourceDto){
        Resource resource = new Resource();
        resource.setName(resourceDto.getName());

        if (resourceDto.getPassword() != null) {
            resource.setPassword(resourceDto.getPassword());
        }

        if (resourceDto.getRoles() != null) {
            Set<Role> newRoles = resourceDto.getRoles().stream()
                    .map(roleDto -> roleRepository.findByName(roleDto.getName())
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleDto.getName())))
                    .collect(Collectors.toSet());
            resource.setRoles(newRoles);
        }

        if (resourceDto.getManagerId() != null) {
            Resource manager = resourceRepository.findById(resourceDto.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found with ID: " + resourceDto.getManagerId()));
            resource.setManager(manager);
        }
        Resource saved = resourceRepository.save(resource);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ResourceDto> getAllResource() {
        return resourceRepository.findAll().stream()
        .map(this :: toDto)
        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ResourceDto> getResourceById(Long id) {
        return resourceRepository.findById(id)
                .map(this::toDto);
    }

    @Transactional
    public ResourceDto updateResource(Long id, ResourceDto resourceDto) {
        return resourceRepository.findById(id)
            .map(resource -> {
                resource.setName(resourceDto.getName());
                if (resourceDto.getPassword() != null) {
                    resource.setPassword(resourceDto.getPassword());
                }
                if (resourceDto.getRoles() != null) {
                    Set<Role> newRoles = resourceDto.getRoles().stream()
                            .map(roleDto -> roleRepository.findByName(roleDto.getName())
                                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleDto.getName())))
                            .collect(Collectors.toSet());
                    resource.setRoles(newRoles);
                }
                if (resourceDto.getManagerId() != null) {
                        Resource manager = resourceRepository.findById(resourceDto.getManagerId())
                                .orElseThrow(() -> new RuntimeException("Manager not found with ID: " + resourceDto.getManagerId()));
                        resource.setManager(manager);
                    } else {
                        resource.setManager(null);
                    }
                return toDto(resourceRepository.save(resource));
            })
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }

    @Transactional
    public void assignGoalToResource(Long resourceId, Long goalId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with ID: " + goalId));

        resource.getGoals().add(goal);
        resourceRepository.save(resource);

        int currentYear = LocalDate.now().getYear();
        AnnualAssessment assessment = annualAssessmentRepository.findByResourceIdAndYear(resourceId, currentYear)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found for resource ID: " + resourceId + " and year: " + currentYear));
        assessment.setGoalStatus("InProgress");
        annualAssessmentRepository.save(assessment);
    }

    @Transactional(readOnly = true)
    public GoalsWithStatusDto getGoalsForResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        int currentYear = LocalDate.now().getYear();
        AnnualAssessment assessment = annualAssessmentRepository.findByResourceIdAndYear(resourceId, currentYear)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found for resource ID: " + resourceId + " and year: " + currentYear));

        String goalStatus = assessment.getGoalStatus();

        List<GoalDto> goals = resource.getGoals().stream()
                .map(this::toGoalDto)
                .collect(Collectors.toList());

        return toGoalsWithStatusDto(goals, goalStatus);
    }

    @Transactional
    public void updateGoalStatus(Long resourceId, int year, String newStatus) {
        AnnualAssessment assessment = annualAssessmentRepository.findByResourceIdAndYear(resourceId, year)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found for resource ID: " + resourceId + " and year: " + year));
        assessment.setGoalStatus(newStatus);
        annualAssessmentRepository.save(assessment);
    }

    @Transactional
    public void deleteResource(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<ResourceDto> getResourcesUnderManager(Long managerId) {
        return resourceRepository.findByManagerId(managerId).stream()
        .map(this :: toDto)
        .collect(Collectors.toList());
    }
    
    private ResourceDto toDto(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setManagerId(resource.getManager() != null ? resource.getManager().getId() : null);
        dto.setRoles(resource.getRoles().stream()
                .map(role -> {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setId(role.getId());
                    roleDto.setName(role.getName());
                    return roleDto;
                })
                .collect(Collectors.toSet()));
        return dto;
    }

    private GoalDto toGoalDto(Goal goal) {
        GoalDto dto = new GoalDto();
        dto.setId(goal.getId());
        dto.setGoal(goal.getGoal());
        return dto;
    }

    private GoalsWithStatusDto toGoalsWithStatusDto(List<GoalDto> goals, String overallGoalStatus) {
        GoalsWithStatusDto dto = new GoalsWithStatusDto();
        dto.setGoals(goals);
        dto.setOverallGoalStatus(overallGoalStatus);
        return dto;
    }

}
