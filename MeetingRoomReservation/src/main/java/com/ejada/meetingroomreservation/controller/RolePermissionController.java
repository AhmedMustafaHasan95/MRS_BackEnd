package com.ejada.meetingroomreservation.controller;

import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.DTO.RolePermissionDTO;
import com.ejada.meetingroomreservation.service.RolePermission.RolePermissionService;
import com.ejada.meetingroomreservation.utils.ResponseRequestBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/RolePermissions")
public class RolePermissionController extends BaseController {
    private static final Logger logger = LogManager.getLogger(RolesController.class);
    @Autowired
    private RolePermissionService service;
    @Autowired
    private ResponseRequestBuilder responseRequestBuilder;

    @PostMapping("addRolePermission")
    public ResponseEntity<ResponseData> addRolePermissions(@RequestBody RequestData requestData) throws Exception {
        RolePermissionDTO dto = null;
        try {
            logger.info("addRolePermission");
            dto = (RolePermissionDTO) responseRequestBuilder.getRequestData(requestData, RolePermissionDTO.class);
            RolePermissionDTO newDTO = service.addRolePermission(dto);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }
}