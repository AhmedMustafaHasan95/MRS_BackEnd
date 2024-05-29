package com.ejada.meetingroomreservation.auth.utils;

import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.auth.exceptions.UserAuthenticationException;
import com.ejada.meetingroomreservation.entity.User;
import com.ejada.meetingroomreservation.service.User.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthUtils {
    private static Logger logger = LogManager.getLogger(AuthUtils.class);
    @Autowired
    private UserService userService;

    public static void checkLoggedInSession(User user) throws UserAuthenticationException {
        if (!(user.getTokenId() == null || user.getTokenId().isBlank())) {
            logger.error("This User has already an active session");
            throw new UserAuthenticationException("This User has already an active session");
        }
    }

    public void deleteUserExpiredToken(String username) throws Exception {
        try {
            UserDTO user = userService.getUserByUsername(username);
            if (user == null)
                throw new EntityNotFoundException("User not found");
            userService.updateTokenById("", user.getId());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    public List<SimpleGrantedAuthority> getUserAuthorities(String username) throws Exception {
        List<PermissionDTO> permissions = userService.findPermissionsByUsername(username);
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (PermissionDTO permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }
        return authorities;
    }
}
