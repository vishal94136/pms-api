package com.api.pms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.pms.entity.AnnualAssessment;

@Repository
public interface AnnualAssessmentRepository extends JpaRepository<AnnualAssessment, Long>{

    List<AnnualAssessment> findByResourceId(Long resourceId);

    @Query("SELECT a FROM AnnualAssessment a WHERE a.resource.id = :resourceId AND a.year = :year")
    Optional<AnnualAssessment> findByResourceIdAndYear(Long resourceId, int year);
}
