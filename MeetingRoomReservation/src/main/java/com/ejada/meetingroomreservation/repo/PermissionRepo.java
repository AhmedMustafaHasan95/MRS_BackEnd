package com.ejada.meetingroomreservation.repo;

import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {
}
