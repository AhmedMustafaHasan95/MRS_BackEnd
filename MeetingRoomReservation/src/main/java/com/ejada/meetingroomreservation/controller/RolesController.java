package com.ejada.meetingroomreservation.controller;

import com.ejada.meetingroomreservation.DTO.CountryDTO;
import com.ejada.meetingroomreservation.DTO.RoleDTO;
import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.service.Role.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Roles")
public class RolesController extends BaseController {
    private static final Logger logger = LogManager.getLogger(RolesController.class);
    @Autowired
    private RoleService roleService;

    @GetMapping("getAllRoles")
    @PreAuthorize("hasAuthority('getAllRoles')")
    public ResponseEntity<ResponseData> getAllRoles() {
        try {
            logger.info("getAllRoles");
            List<RoleDTO> Roles = roleService.getAllRoles();
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(Roles);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PostMapping("getRoleById")
    @PreAuthorize("hasAuthority('getRoleById')")
    public ResponseEntity<ResponseData> getRoleById(@RequestBody RequestData requestData) {
        try {
            logger.info("getRoleById");
            RoleDTO RoleDTO = (RoleDTO) responseReqBuilder.getRequestData(requestData, CountryDTO.class);
            RoleDTO = roleService.getRoleById(RoleDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(RoleDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PostMapping("addRole")
    @PreAuthorize("hasAuthority('addRole')")
    public ResponseEntity<ResponseData> addRole(@RequestBody RequestData requestData) {
        RoleDTO RoleDTO = null;
        try {
            logger.info("addRole");
            RoleDTO = (RoleDTO) responseReqBuilder.getRequestData(requestData, RoleDTO.class);
            RoleDTO newRoleDTO = roleService.addRole(RoleDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newRoleDTO);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PutMapping("updateRole")
    @PreAuthorize("hasAuthority('updateRole')")
    public ResponseEntity<ResponseData> updateRole(@RequestBody RequestData requestData) {
        RoleDTO RoleDTO = null;
        try {
            logger.info("updateRole");
            RoleDTO = (RoleDTO) responseReqBuilder.getRequestData(requestData, RoleDTO.class);
            RoleDTO newRoleDTO = roleService.updateRole(RoleDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newRoleDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @DeleteMapping("deleteRole")
    @PreAuthorize("hasAuthority('deleteRole')")
    public ResponseEntity<ResponseData> deleteRole(@RequestBody RequestData requestData) {
        RoleDTO RoleDTO = null;
        try {
            logger.info("deleteRole");
            RoleDTO = (RoleDTO) responseReqBuilder.getRequestData(requestData, RoleDTO.class);
            boolean isDeleted = roleService.deleteRole(RoleDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(isDeleted);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }
}
