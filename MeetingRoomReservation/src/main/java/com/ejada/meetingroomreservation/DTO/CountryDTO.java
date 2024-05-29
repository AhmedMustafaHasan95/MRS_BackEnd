package com.ejada.meetingroomreservation.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CountryDTO {
    private long id;
    @NotBlank(message = "Country code is required")
    private String code;
    @NotBlank(message = "Country name is required")
    private String name;

}
