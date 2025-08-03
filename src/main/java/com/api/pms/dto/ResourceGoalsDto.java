package com.api.pms.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResourceGoalsDto {
    private String resourceName;
    private List<String> goals;
    private String goalStatus;
    private Integer year;
}
