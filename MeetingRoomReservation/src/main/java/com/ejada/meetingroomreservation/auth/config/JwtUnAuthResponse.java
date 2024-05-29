package com.ejada.meetingroomreservation.auth.config;

import com.ejada.meetingroomreservation.DTO.ApiStatusDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;

@Component
public class JwtUnAuthResponse implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = 2848589597094595376L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String errorCode = String.valueOf(HttpServletResponse.SC_UNAUTHORIZED);
        if (request.getAttribute("errorCode") != null) {
            errorCode = (String) request.getAttribute("errorCode");
        }
        Object ex = request.getAttribute("exception");
        Exception exception = ex == null ? null : (ex instanceof Exception ? (Exception) ex : new Exception(ex.toString()));
        String message;
        if (exception != null) {
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("status", new ApiStatusDTO(errorCode, exception.getMessage())));
            response.getOutputStream().write(body);
        } else {
            if (authException.getCause() != null) {
                message = authException.getCause().toString() + " " + authException.getMessage();
            } else {
                message = authException.getMessage();
            }
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("status", new ApiStatusDTO(errorCode, message)));

            response.getOutputStream().write(body);
        }
    }
}
