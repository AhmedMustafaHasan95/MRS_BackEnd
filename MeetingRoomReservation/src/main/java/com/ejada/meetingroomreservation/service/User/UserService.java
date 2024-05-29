package com.ejada.meetingroomreservation.service.User;

import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.DTO.UserDTO;

import java.util.List;

public interface UserService {
    public List<UserDTO> getAllUsers() throws Exception;

    public UserDTO getUserById(UserDTO userDTO) throws Exception;

    public UserDTO getUserByUsername(String username) throws Exception;

    public UserDTO addUser(UserDTO userDTO) throws Exception;

    public UserDTO updateUser(UserDTO userDTO) throws Exception;

    public boolean deleteUser(UserDTO userDTO) throws Exception;

    public void updateTokenById(String tokenId, long id) throws Exception;

    public List<PermissionDTO> findPermissionsByUsername(String username) throws Exception;
}
