package com.ejada.meetingroomreservation.auth.service;

import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.auth.config.ThreadLocalContext;
import com.ejada.meetingroomreservation.auth.exceptions.UserAuthenticationException;
import com.ejada.meetingroomreservation.auth.jwt.JwtTokenUtil;
import com.ejada.meetingroomreservation.auth.model.AppUserDetail;
import com.ejada.meetingroomreservation.auth.model.JWTResponseDTO;
import com.ejada.meetingroomreservation.auth.model.LoggedInUser;
import com.ejada.meetingroomreservation.auth.model.LoginRequestDTO;
import com.ejada.meetingroomreservation.auth.utils.AuthUtils;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.User;
import com.ejada.meetingroomreservation.repo.UserRepo;
import com.ejada.meetingroomreservation.service.User.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Component
public class AuthServiceImpl implements AuthService {
    private Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo repo;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private AppValidator appValidator;
    @Autowired
    private PasswordEncoder encoder;


    @Override
    public JWTResponseDTO login(LoginRequestDTO loginRequest) throws Exception {
        try {
            appValidator.validate(loginRequest);
            Optional<User> loggingUserOption = repo.findByUsernameOrEmail(loginRequest.getUsername(), loginRequest.getUsername());
            if (loggingUserOption.isEmpty()) {
                String message = String.format("Could not find user for (name: %s)", loginRequest.getUsername());
                logger.error(message);
                throw new UserAuthenticationException(message, "auth.login.badCredentials", loginRequest.getUsername());
            }
            User loggingUser = loggingUserOption.get();
            validatePassword(loggingUser, loginRequest.getPassword());
            AuthUtils.checkLoggedInSession(loggingUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            AppUserDetail userDetails = (AppUserDetail) authenticate.getPrincipal();
            return handleSuccessLogin(userDetails);
        } catch (UserAuthenticationException ex) {
            String message = String.format("User (name: %s) failed to login due to bad credentials", loginRequest.getUsername());
            logger.error(message + " due to " + ex.getMessage());
            throw ex;
        } catch (AuthenticationException ex) {
            String message = String.format("User (name: %s) failed to login due to bad credentials", loginRequest.getUsername());
            logger.error(message + " due to " + ex.getMessage());
            throw new UserAuthenticationException(message, "auth.login.badCredentials", loginRequest.getUsername());
        } catch (Exception ex) {
            String message = String.format("User (name: %s) failed to login", loginRequest.getUsername());
            logger.error(message + " due to " + ex.getMessage());
            throw new UserAuthenticationException(message, "auth.login.badCredentials", loginRequest.getUsername());
        }
    }

    private void validatePassword(User loggingUser, String password) throws Exception {
        boolean matches = encoder.matches(password, loggingUser.getPassword());
        if (!matches)
            throw new UserAuthenticationException("auth.login.badCredentials");
    }

    @Override
    @Transactional
    public UserDTO registerUser(UserDTO usrDTO) throws Exception {
        try {
            if (!usrDTO.getPassword().isBlank())
                usrDTO.setPassword(encoder.encode(usrDTO.getPassword()));
            usrDTO = userService.addUser(usrDTO);
            return usrDTO;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }


    /*@Override
    public void verifyEmail(String code) throws Exception {
        try {
            if (code.isBlank()) throw new ValidationsException("auth.verify.invalidCode");
            String decrypted = Utils.decrypt(code);
            String[] values = decrypted.split(";");
//            Integer userId = Integer.parseInt(values[1]);
            User user = userRepo.findTopByOrderByUsrIdDesc();
            Integer stageId = requestStageRepo.getStageIdByRecordId(user.getUsrId());
//            Optional<User> user = userRepo.findById(userId);
//            if (!user.isPresent()) {
//                throw new ValidationsException("No user found you this verification code");
//            }
//            Integer stageId = Integer.parseInt(values[3]);
            if (!requestStageRepo.existsById(stageId)) {
                throw new ValidationsException("No verification request found for this verification code");
            }
            if (!user.getVerificationCode().equalsIgnoreCase(values[2])) {
                throw new ValidationsException("Incorrect Verification code");
            }
            RequestStateDTO requestStateDTO = new RequestStateDTO();
            requestStateDTO.setRqstStgId(stageId);
            requestStateDTO.setComment("Verified successfully");
            requestStateDTO.setAction("VERIFY");
            requestService.updateRequestStage(requestStateDTO);
        } catch (Exception ex) {
            logger.error("verify user email failed due to :" + ex.getMessage());
            throw ex;
        }

    }*/

    @Override
    @Transactional
    public boolean logout(String username) throws Exception {
        try {
            UserDTO user = userService.getUserByUsername(username);
            if (user == null)
                throw new EntityNotFoundException("User not found");
            userService.updateTokenById("", user.getId());
            return true;
        } catch (Exception ex) {
            logger.error(String.format("User (name: %s) failed to logout", username));
            throw ex;
        }
    }

    private JWTResponseDTO handleSuccessLogin(AppUserDetail userDetails) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        payload.put("uuid", uuid);
        logger.info("uuid : " + uuid);
        // get permissions
        List<PermissionDTO> permissions = userService.findPermissionsByUsername(userDetails.getUsername());
        List<String> actions = permissions.stream().map(p -> p.getName()).toList();
        payload.put("permissions", actions);
        userDetails.setPermissions(permissions);
        String token = jwtTokenUtil.createToken(userDetails.getUsername(), payload);
        JWTResponseDTO jwtResponseDTO = getJwtResponseDTO(userDetails, token);
        // set session id with IssuedAt date save in token
        userService.updateTokenById(uuid, userDetails.getId());
        // save current logged in user
        LoggedInUser loggedInUser = new LoggedInUser(userDetails.getUser());
        ThreadLocalContext.setUserInfo(loggedInUser);
        return jwtResponseDTO;
    }

    private JWTResponseDTO getJwtResponseDTO(AppUserDetail userDetails, String token) throws Exception {
        JWTResponseDTO jwtResponseDTO = new JWTResponseDTO();
        jwtResponseDTO.setAccessToken(token);
        jwtResponseDTO.setEmail(userDetails.getEmail());
        jwtResponseDTO.setFullName(userDetails.getFullName());
        jwtResponseDTO.setUsername(userDetails.getUsername());
        jwtResponseDTO.setId(userDetails.getId());
        jwtResponseDTO.setPermissions(fetchUserPermission(userDetails).stream().map(PermissionDTO::getName).toList());
        return jwtResponseDTO;
    }

    private List<PermissionDTO> fetchUserPermission(AppUserDetail userDetails) throws Exception {
        return (userDetails.getPermissions() == null || userDetails.getPermissions().isEmpty()) ? userService.findPermissionsByUsername(userDetails.getUsername()) : userDetails.getPermissions();
    }

}
