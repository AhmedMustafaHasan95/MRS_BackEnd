package com.ejada.meetingroomreservation.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class UserDTO {
	private Long id;
	@NotBlank(message = "full name is required")
	private String fullname;
	@NotBlank(message = "username is required")
	private String username;
	@NotBlank(message = "password is required")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#@$&*])[A-Za-z\\d#@$&*]{8,20}$", message = "Password must be between 8 and 20 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one of the following special characters: #, @, $, &, *.")
	@JsonIgnore
	private String password;
	@NotBlank(message = "email is required")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Please enter correct email")
	private String email;
	private String tokenId;
	private Long roleId;
	private Boolean blocked;
}
