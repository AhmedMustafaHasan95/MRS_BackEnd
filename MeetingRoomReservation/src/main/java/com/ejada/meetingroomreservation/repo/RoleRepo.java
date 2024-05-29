package com.ejada.meetingroomreservation.repo;

import com.ejada.meetingroomreservation.entity.Permission;
import com.ejada.meetingroomreservation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    @Query("SELECT DISTINCT p FROM Role r JOIN r.permissions p WHERE r IN :roles")
    List<Permission> findPermissionsByRoles(List<Role> roles);
}
