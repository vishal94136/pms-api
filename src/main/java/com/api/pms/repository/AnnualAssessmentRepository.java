package com.api.pms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.pms.entity.AnnualAssessment;

@Repository
public interface AnnualAssessmentRepository extends JpaRepository<AnnualAssessment, Long>{

    List<AnnualAssessment> findByResourceId(Long resourceId);
}
