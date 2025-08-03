package com.api.pms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.pms.dto.GoalDto;
import com.api.pms.entity.Goal;
import com.api.pms.repository.GoalRepository;

@Service
public class GoalService {
    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Transactional
    public Goal createGoal(GoalDto dto) {
        Goal goal = new Goal();
        goal.setGoal(dto.getGoal());
        return goalRepository.save(goal);
    }

    @Transactional(readOnly = true)
    public List<GoalDto> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private GoalDto toDto(Goal goal) {
        GoalDto dto = new GoalDto();
        dto.setId(goal.getId());
        dto.setGoal(goal.getGoal());
        return dto;
    }
}
