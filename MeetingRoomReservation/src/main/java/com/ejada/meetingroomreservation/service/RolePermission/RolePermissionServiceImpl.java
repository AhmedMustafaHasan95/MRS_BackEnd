package com.ejada.meetingroomreservation.service.RolePermission;

import com.ejada.meetingroomreservation.DTO.RolePermissionDTO;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.Permission;
import com.ejada.meetingroomreservation.entity.Role;
import com.ejada.meetingroomreservation.entity.RolePermission;
import com.ejada.meetingroomreservation.entity.RolePermissionId;
import com.ejada.meetingroomreservation.repo.PermissionRepo;
import com.ejada.meetingroomreservation.repo.RolePermissionRepo;
import com.ejada.meetingroomreservation.repo.RoleRepo;
import com.ejada.meetingroomreservation.service.country.CountryServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    private final static Logger logger = LogManager.getLogger(CountryServiceImp.class);

    @Autowired
    private RolePermissionRepo repo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PermissionRepo permissionRepo;
    @Autowired
    ModelMapper mapper;

    @Autowired
    AppValidator appValidator;

    @Override
    public RolePermissionDTO addRolePermission(RolePermissionDTO rolePermissionDTO) {
        try {
            Role role = roleRepo.findById(rolePermissionDTO.getRoleId()).orElseThrow();
            Permission permission = permissionRepo.findById(rolePermissionDTO.getPermissionId()).orElseThrow();
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);
            RolePermissionId id = new RolePermissionId();
            id.setRoleId(role.getId());
            id.setPermissionId(permission.getId());
            rolePermission.setId(id);
            RolePermission savedEntity = repo.save(rolePermission);
            return mapper.map(savedEntity, RolePermissionDTO.class);
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            throw e;
        }
    }
}