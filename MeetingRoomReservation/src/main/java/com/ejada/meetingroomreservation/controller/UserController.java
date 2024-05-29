package com.ejada.meetingroomreservation.controller;


import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.service.User.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/User")
public class UserController extends BaseController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private UserService UserService;


    @GetMapping("getUserList")
    //@PreAuthorize("hasAuthority('User_LIST')")
    public ResponseEntity<ResponseData> getAllUser() {
        try {
            logger.info("getUserList");
            List<UserDTO> users = UserService.getAllUsers();
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(users);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PostMapping("getUserById")
    //@PreAuthorize("hasAuthority('User_LIST')")
    public ResponseEntity<ResponseData> getUserById(@RequestBody RequestData requestData) {
        try {
            logger.info("getUserById");
            UserDTO userDTO = (UserDTO) responseReqBuilder.getRequestData(requestData, UserDTO.class);
            userDTO = UserService.getUserById(userDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(userDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }


    @PostMapping("addUser")
    //@PreAuthorize("hasAuthority('User_ADD')")
    public ResponseEntity<ResponseData> addUser(@RequestBody RequestData requestData) {
        UserDTO userDTO = null;
        try {
            logger.info("addUser");
            userDTO = (UserDTO) responseReqBuilder.getRequestData(requestData, UserDTO.class);
            UserDTO newUserDTO = UserService.addUser(userDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newUserDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PutMapping("updateUser")
    //@PreAuthorize("hasAuthority('User_UPDATE')")
    public ResponseEntity<ResponseData> updateUser(@RequestBody RequestData requestData) throws Exception {
        UserDTO userDTO = null;
        try {
            logger.info("updateUser");
            userDTO = (UserDTO) responseReqBuilder.getRequestData(requestData, UserDTO.class);
            UserDTO newUserDTO = UserService.updateUser(userDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newUserDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @DeleteMapping("deleteUser")
    //@PreAuthorize("hasAuthority('User_DELETE')")
    public ResponseEntity<ResponseData> deleteUser(@RequestBody RequestData requestData) throws Exception {
        UserDTO userDTO = null;
        try {
            logger.info("deleteUser");
            userDTO = (UserDTO) responseReqBuilder.getRequestData(requestData, UserDTO.class);
            boolean isDeleted = UserService.deleteUser(userDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse("deleted", isDeleted);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

}