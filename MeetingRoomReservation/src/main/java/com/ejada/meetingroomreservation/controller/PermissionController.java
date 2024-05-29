package com.ejada.meetingroomreservation.controller;

import com.ejada.meetingroomreservation.DTO.*;
import com.ejada.meetingroomreservation.service.Permission.PermissionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Permission")
public class PermissionController extends BaseController {
	private static final Logger logger = LogManager.getLogger(PermissionController.class);
	@Autowired
	private PermissionService permissionService;

	@GetMapping("getAllPermissions")
	@PreAuthorize("hasAuthority('getAllPermissions')")
	public ResponseEntity<ResponseData> getAllPermissions() {
		try {
			logger.info("getAllPermissions");
			List<PermissionDTO> permissions = permissionService.getAllPermissions();
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(permissions);
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@PostMapping("getPermissionById")
	@PreAuthorize("hasAuthority('getPermissionById')")
	public ResponseEntity<ResponseData> getPermissionById(@RequestBody RequestData requestData) {
		try {
			logger.info("getPermissionById");
			PermissionDTO PermissionDTO = (PermissionDTO) responseReqBuilder.getRequestData(requestData,
					CountryDTO.class);
			PermissionDTO = permissionService.getPermissionById(PermissionDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(PermissionDTO);
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@PostMapping("addPermissions")
	@PreAuthorize("hasAuthority('addPermissions')")
	public ResponseEntity<ResponseData> addPermission(@RequestBody List<PermissionDTO> permissionsDTO) {
		try {
			logger.info("addPermission");
			ResponseData apiResponse = responseReqBuilder
					.wrapSuccessResponse(permissionService.addPermissions(permissionsDTO));
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@PostMapping("addPermission")
	@PreAuthorize("hasAuthority('addPermission')")
	public ResponseEntity<ResponseData> addPermissions(@RequestBody RequestData requestData) {
		PermissionDTO PermissionDTO = null;
		try {
			logger.info("addPermission");
			PermissionDTO = (PermissionDTO) responseReqBuilder.getRequestData(requestData, PermissionDTO.class);
			PermissionDTO newPermissionDTO = permissionService.addPermission(PermissionDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newPermissionDTO);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@PutMapping("updatePermission")
	@PreAuthorize("hasAuthority('updatePermission')")
	public ResponseEntity<ResponseData> updatePermission(@RequestBody RequestData requestData) {
		PermissionDTO PermissionDTO = null;
		try {
			logger.info("updatePermission");
			PermissionDTO = (PermissionDTO) responseReqBuilder.getRequestData(requestData, PermissionDTO.class);
			PermissionDTO newPermissionDTO = permissionService.updatePermission(PermissionDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newPermissionDTO);
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@DeleteMapping("deletePermission")
	@PreAuthorize("hasAuthority('deletePermission')")
	public ResponseEntity<ResponseData> deletePermission(@RequestBody RequestData requestData) {
		PermissionDTO PermissionDTO = null;
		try {
			logger.info("deletePermission");
			PermissionDTO = (PermissionDTO) responseReqBuilder.getRequestData(requestData, PermissionDTO.class);
			boolean isDeleted = permissionService.deletePermission(PermissionDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(isDeleted);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}
}
