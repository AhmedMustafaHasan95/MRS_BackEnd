package com.ejada.meetingroomreservation.auth.controller;

import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.auth.model.JWTResponseDTO;
import com.ejada.meetingroomreservation.auth.model.LoginRequestDTO;
import com.ejada.meetingroomreservation.auth.model.LogoutRequestDTO;
import com.ejada.meetingroomreservation.auth.service.AuthService;
import com.ejada.meetingroomreservation.controller.BaseController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Auth")
public class AuthController extends BaseController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@RequestBody LoginRequestDTO loginRequestDTO) throws Exception {
        try {
            JWTResponseDTO jwtResponse = authService.login(loginRequestDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(jwtResponse);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData> registerUser(@RequestBody RequestData requestData) throws Exception {
        UserDTO usrDTO = null;
        try {
            usrDTO = (UserDTO) responseReqBuilder.getRequestData(requestData, UserDTO.class);
            usrDTO = authService.registerUser(usrDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(usrDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<ResponseData> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) throws Exception {
        try {
            authService.logout(logoutRequestDTO.getUsername());
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse("isLogout", true);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

}
