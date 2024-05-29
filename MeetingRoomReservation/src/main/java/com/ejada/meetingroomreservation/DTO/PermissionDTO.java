package com.ejada.meetingroomreservation.DTO;

import javax.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class PermissionDTO {
    private long id;
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "url action is required")
    private String urlAction;

}
