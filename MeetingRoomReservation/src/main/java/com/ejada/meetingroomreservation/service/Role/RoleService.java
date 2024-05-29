package com.ejada.meetingroomreservation.service.Role;

import com.ejada.meetingroomreservation.DTO.RoleDTO;

import java.util.List;

public interface RoleService {
    public List<RoleDTO> getAllRoles() throws Exception;

    public RoleDTO getRoleById(RoleDTO RoleDTO) throws Exception;

    public RoleDTO addRole(RoleDTO RoleDTO) throws Exception;

    public RoleDTO updateRole(RoleDTO RoleDTO) throws Exception;

    public boolean deleteRole(RoleDTO RoleDTO) throws Exception;


}
