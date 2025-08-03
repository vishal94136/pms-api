package com.api.pms.dto;

import lombok.Data;

@Data
public class AnnualAssessmentDto {

    private Long id;
    private Long resource_id;
    private String resourceName;
    private Long role_id;
    private String roleName;
    private Long manager_id;
    private String managerName;
    private Integer rating;
    private String assessmentStatus;
    private String goalStatus;
    private Integer year;
}
