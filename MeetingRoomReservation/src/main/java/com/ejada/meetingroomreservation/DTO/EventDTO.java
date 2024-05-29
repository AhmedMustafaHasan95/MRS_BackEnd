package com.ejada.meetingroomreservation.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class EventDTO {
	private long id;
	@NotBlank(message = "name is required")
	private String name;
	private String contactPerson;
	private String contactEmail;
	private String location;
	private String description;
	@NotBlank(message = "type is required")
	private String type;
	private String status;
	@NotBlank(message = "event-date is required")
	private String eventDate;
	@NotBlank(message = "start-at is required")
	@DateTimeFormat(pattern = "HH:mm")
	private String startAt;
	@NotBlank(message = "end-at is required")
	@DateTimeFormat(pattern = "HH:mm")
	private String endAt;
	@NotNull
	private RoomDTO room;
	private UserDTO creator;
	private LocalDateTime creationTime;

}
