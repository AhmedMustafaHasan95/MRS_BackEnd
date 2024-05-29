package com.ejada.meetingroomreservation.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OfficeDTO {
    private long id;
    @NotBlank(message = "Office name is required")
    private String name;
    @NotNull(message = "Country is required")
    private CountryDTO country;
}
