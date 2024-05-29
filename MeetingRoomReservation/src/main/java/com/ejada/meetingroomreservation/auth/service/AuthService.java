package com.ejada.meetingroomreservation.auth.service;

import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.auth.model.JWTResponseDTO;
import com.ejada.meetingroomreservation.auth.model.LoginRequestDTO;

public interface AuthService {
    public JWTResponseDTO login(LoginRequestDTO loginRequest) throws Exception;

    public boolean logout(String username) throws Exception;

    public UserDTO registerUser(UserDTO usrDTO) throws Exception;

}
