package com.ejada.meetingroomreservation.repo;

import com.ejada.meetingroomreservation.entity.Permission;
import com.ejada.meetingroomreservation.entity.Role;
import com.ejada.meetingroomreservation.entity.RolePermission;
import com.ejada.meetingroomreservation.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepo extends JpaRepository<RolePermission, RolePermissionId> {
    @Query("SELECT DISTINCT rp.permission FROM RolePermission rp WHERE rp.role IN :roles")
    List<Permission> findPermissionsByRoles(List<Role> roles);
}