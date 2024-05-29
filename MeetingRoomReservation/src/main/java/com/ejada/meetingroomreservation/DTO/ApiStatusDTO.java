package com.ejada.meetingroomreservation.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiStatusDTO {
    private String message;
    private String code;
    private List<ErrorDTO> validationErrors;

    public ApiStatusDTO(String code,String message) {
        this.message = message;
        this.code = code;
    }

    public ApiStatusDTO(List<ErrorDTO> errors) {
        this.validationErrors = errors;
        this.code = "400";
        this.message = "response.error.validate.required";
    }
    public ApiStatusDTO(Object invalidErrors) {
        this.code = "400";
        this.message = "response.error.validate.invalid";
    }
}