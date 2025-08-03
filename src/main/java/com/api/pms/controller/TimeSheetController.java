package com.api.pms.controller;

import org.springframework.web.bind.annotation.RestController;

import com.api.pms.Exception.ResourceNotFoundException;
import com.api.pms.dto.AnnualAssessmentDto;
import com.api.pms.dto.AuthRequestDto;
import com.api.pms.dto.AuthResponseDto;
import com.api.pms.dto.GoalDto;
import com.api.pms.dto.GoalsWithStatusDto;
import com.api.pms.dto.ResourceDto;
import com.api.pms.dto.ResourceGoalsDto;
import com.api.pms.dto.UpdateGoalStatusDto;
import com.api.pms.entity.Goal;
import com.api.pms.service.AnnualAssessmentService;
import com.api.pms.service.AuthService;
import com.api.pms.service.GoalService;
import com.api.pms.service.ResourceService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/pms")
public class TimeSheetController {

    private final ResourceService resourceService;
    private final AuthService authService;
    private final AnnualAssessmentService annualAssessmentService;
    private final GoalService goalService;

    public TimeSheetController(ResourceService resourceService, AuthService authService, AnnualAssessmentService annualAssessmentService, GoalService goalService){
        this.resourceService = resourceService;
        this.authService = authService;
        this.annualAssessmentService = annualAssessmentService;
        this.goalService = goalService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Healthy");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto authRequest) {
        try {
            AuthResponseDto response = authService.authenticate(authRequest.getResourceName(), authRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException | SecurityException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/createResource")
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody ResourceDto resourceDto) {
        ResourceDto createdResource = resourceService.createResource(resourceDto);
        return ResponseEntity.status(201).body(createdResource);
    }
    @GetMapping("/resources")
    public List<ResourceDto> getAllResource() {
        return resourceService.getAllResource();
    }
    
    @PostMapping("/annualAssessment")
    public ResponseEntity<AnnualAssessmentDto> createAssessment(@RequestBody AnnualAssessmentDto dto) {
        AnnualAssessmentDto created = annualAssessmentService.createAssessment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/annualAssessment/resource/{resourceId}")
    public ResponseEntity<List<AnnualAssessmentDto>> getAssessmentsForResource(@PathVariable Long resourceId) {
        List<AnnualAssessmentDto> assessments = annualAssessmentService.getAssessmentsForResource(resourceId);
        return ResponseEntity.ok(assessments);
    }

    @GetMapping("/annualAssessment/manager/{managerId}")
    public ResponseEntity<List<AnnualAssessmentDto>> getAllForManagerAndSubordinates(@PathVariable Long managerId) {
        List<AnnualAssessmentDto> assessments = annualAssessmentService.getAllForManagerAndSubordinates(managerId);
        return ResponseEntity.ok(assessments);
    }

    @PutMapping("annualAssessment/{id}")
    public ResponseEntity<AnnualAssessmentDto> updateAssessment(@PathVariable Long id, @RequestBody AnnualAssessmentDto dto) {
        AnnualAssessmentDto updated = annualAssessmentService.updateAssessment(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/goal")
    public ResponseEntity<Goal> createGoal(@RequestBody GoalDto dto) {
        Goal created = goalService.createGoal(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/goal")
    public ResponseEntity<List<GoalDto>> getAllGoals() {
        List<GoalDto> goals = goalService.getAllGoals();
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/manager/{id}/getResource")
    public ResponseEntity<List<ResourceDto>> getMethodName(@PathVariable Long id) {
        List<ResourceDto> resources = resourceService.getResourcesUnderManager(id);
        return ResponseEntity.ok(resources);
    }
    
    @PostMapping("/resource/{resourceId}/goals/{goalId}")
    public ResponseEntity<Void> assignGoalToResource(@PathVariable Long resourceId, @PathVariable Long goalId) {
        resourceService.assignGoalToResource(resourceId, goalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/annualAssessment/manager/{managerId}/goals")
    public ResponseEntity<List<ResourceGoalsDto>> getGoalsForManagerSubordinates(@PathVariable Long managerId) {
        List<ResourceGoalsDto> goalsData = annualAssessmentService.getGoalsForManagerSubordinates(managerId);
        return ResponseEntity.ok(goalsData);
    }

    @GetMapping("/resource/{resourceId}/goals")
    public ResponseEntity<GoalsWithStatusDto> getGoalsForResource(@PathVariable Long resourceId) {
        GoalsWithStatusDto goalsWithStatus = resourceService.getGoalsForResource(resourceId);
        return ResponseEntity.ok(goalsWithStatus);
    }

    @PutMapping("/resource/{resourceId}/years/{year}/updateGoalStatus")
    public ResponseEntity<Void> updateGoalStatus(@PathVariable Long resourceId, @PathVariable int year, @RequestBody UpdateGoalStatusDto updateDto) {
    resourceService.updateGoalStatus(resourceId, year, updateDto.getNewStatus());
    return ResponseEntity.ok().build();
}
}
