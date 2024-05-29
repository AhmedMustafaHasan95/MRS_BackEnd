package com.ejada.meetingroomreservation.service.Permission;

import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.entity.Permission;

import java.util.List;

public interface PermissionService {
	public List<PermissionDTO> getAllPermissions() throws Exception;

	public PermissionDTO getPermissionById(PermissionDTO PermissionDTO) throws Exception;

	public PermissionDTO addPermission(PermissionDTO PermissionDTO) throws Exception;

	public PermissionDTO updatePermission(PermissionDTO PermissionDTO) throws Exception;

	public boolean deletePermission(PermissionDTO PermissionDTO) throws Exception;

	public List<PermissionDTO> addPermissions(List<PermissionDTO> permissions) throws Exception;

}
