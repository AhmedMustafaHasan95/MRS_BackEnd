package com.ejada.meetingroomreservation.service.User;

import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.Permission;
import com.ejada.meetingroomreservation.entity.Role;
import com.ejada.meetingroomreservation.entity.User;
import com.ejada.meetingroomreservation.repo.RolePermissionRepo;
import com.ejada.meetingroomreservation.repo.UserRepo;
import com.ejada.meetingroomreservation.service.country.CountryServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserServiceImp implements UserService {
    private final static Logger logger = LogManager.getLogger(CountryServiceImp.class);
    private static Map<String, List<PermissionDTO>> userPermissions = new HashMap<>();
    @Autowired
    AppValidator appValidator;
    @Autowired
    UserRepo userRepo;
    @Autowired
    RolePermissionRepo roleRepo;
    @Autowired
    ModelMapper mapper;

    @Override
    public List<UserDTO> getAllUsers() throws Exception {
        return userRepo.findAll().stream().map(user -> mapper.map(user, UserDTO.class)).toList();
    }

    @Override
    public UserDTO getUserById(UserDTO userDTO) throws Exception {
        try {
            appValidator.validateID(userDTO.getId());
            Optional<User> user = userRepo.findById(userDTO.getId());
            if (user.isPresent())
                return mapper.map(user.get(), UserDTO.class);
            throw new EntityNotFoundException("User", userDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public UserDTO getUserByUsername(String username) throws Exception {
        try {
            if (username.isBlank())
                throw new ValidationsException("username can not be empty");
            Optional<User> user = userRepo.findByUsernameOrEmail(username, username);
            if (user.isEmpty())
                throw new EntityNotFoundException("User", username);
            return mapper.map(user.get(), UserDTO.class);

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) throws Exception {
        try {
            User user = mapper.map(userDTO, User.class);
            user = userRepo.save(user);
            return mapper.map(user, UserDTO.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) throws Exception {
        try {
            List<ErrorDTO> errors = appValidator.validate(userDTO);
            if (!errors.isEmpty()) throw new ValidationsException(errors);
            if (userRepo.existsById(userDTO.getId())) {
                User user = mapper.map(userDTO, User.class);
                user = userRepo.save(user);
                return mapper.map(user, UserDTO.class);
            }
            throw new EntityNotFoundException("User", userDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean deleteUser(UserDTO userDTO) throws Exception {
        try {
            appValidator.validateID(userDTO.getId());
            if (userRepo.existsById(userDTO.getId())) {
                userRepo.deleteById(userDTO.getId());
                return true;
            }
            throw new EntityNotFoundException("User", userDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updateTokenById(String tokenId, long id) throws Exception {
        try {
            userRepo.updateTokenById(tokenId, id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<PermissionDTO> findPermissionsByUsername(String username) throws Exception {
        try {
            if (userPermissions.containsKey(username))
                return userPermissions.get(username);
            List<Role> rolesByUsername = userRepo.findRolesByUsername(username);
            if (rolesByUsername == null || rolesByUsername.isEmpty())
                return new ArrayList<>();
            List<Permission> permissions = roleRepo.findPermissionsByRoles(rolesByUsername);
            List<PermissionDTO> permissionDTOS = permissions.stream().map(p -> mapper.map(p, PermissionDTO.class)).toList();
            userPermissions.put(username, permissionDTOS);
            return permissionDTOS;
        } catch (Exception ex) {
            logger.error(ex.getCause().getCause().getMessage());
            throw ex;
        }
    }
}
