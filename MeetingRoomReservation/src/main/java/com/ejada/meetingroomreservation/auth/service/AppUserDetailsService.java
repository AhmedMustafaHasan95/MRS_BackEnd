package com.ejada.meetingroomreservation.auth.service;

import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.auth.model.AppUserDetail;
import com.ejada.meetingroomreservation.entity.Permission;
import com.ejada.meetingroomreservation.entity.User;
import com.ejada.meetingroomreservation.repo.UserRepo;
import com.ejada.meetingroomreservation.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsernameOrEmail(username, username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        try {
            List<PermissionDTO> permissions = userService.findPermissionsByUsername(user.get().getUsername());
            return new AppUserDetail(user.get(), permissions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
