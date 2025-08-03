package com.api.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.pms.entity.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

}
