package com.ejada.meetingroomreservation.auth.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequestDTO {
    @NotEmpty(message = "email")
    private String username;
    @NotEmpty
    private String password;
}
