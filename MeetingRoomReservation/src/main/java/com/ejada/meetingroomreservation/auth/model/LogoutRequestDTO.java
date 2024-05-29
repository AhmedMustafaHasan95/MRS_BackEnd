package com.ejada.meetingroomreservation.auth.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LogoutRequestDTO {
    @NotBlank(message = "username can not be empty")
    String username;
}
