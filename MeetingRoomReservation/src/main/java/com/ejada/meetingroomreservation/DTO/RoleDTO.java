package com.ejada.meetingroomreservation.DTO;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    private long id;
    @NotBlank(message = "code is required")
    private String code;
    @NotBlank(message = "name is required")
    private String name;
}
