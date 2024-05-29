package com.ejada.meetingroomreservation.auth.model;

import lombok.Data;

import java.util.List;

@Data
public class JWTResponseDTO<T> {
    private Long id;

    private String username;
    private String fullName;
    private String email;
    private String accessToken;
    private String refreshToken;
    private List<String> permissions;
}
