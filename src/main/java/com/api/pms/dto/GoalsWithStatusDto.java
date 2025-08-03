package com.api.pms.dto;

import java.util.List;

import lombok.Data;

@Data
public class GoalsWithStatusDto {
    private List<GoalDto> goals;
    private String overallGoalStatus;
}