package com.api.pms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.pms.entity.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long>{
    Optional<Resource> findByName(String name);
    List<Resource> findByManagerName(String managerName);
    List<Resource> findByManagerId(Long managerId);
}
