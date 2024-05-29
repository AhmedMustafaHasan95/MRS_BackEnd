package com.ejada.meetingroomreservation.config;

import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AppValidator {

    @Autowired
    public Validator validator;

    public <T> List<ErrorDTO> validate(List<T> DTOs) {
    	List<ErrorDTO> errors = new ArrayList<>();
    	for(int i=0;i<DTOs.size();i++)
    	{
    		List<ErrorDTO> e=validate(DTOs.get(i));
    		errors.addAll(e);
    	}
    	return errors;
    }

    public <T> List<ErrorDTO> validate(T DTO) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(DTO);
        List<ErrorDTO> errors = new ArrayList<>();
        if (constraintViolations != null) {
            for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                String attrName = constraintViolation.getPropertyPath().toString();
                errors.add(new ErrorDTO(attrName, constraintViolation.getMessage()));
            }
        }
        return errors;
    }

    public <T> void validateDTO(T DTO) {

        List<ErrorDTO> errors = validate(DTO);
        if (!errors.isEmpty()) {
            throw new ValidationsException(errors);
        }
    }

    public void validateID(Long id) throws ValidationsException {
        if (id == null || id <= 0) {
            throw new ValidationsException("Id is required");
        }
    }
}