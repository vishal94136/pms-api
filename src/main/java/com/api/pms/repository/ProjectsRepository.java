package com.api.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.pms.entity.Projects;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Long> {

}
