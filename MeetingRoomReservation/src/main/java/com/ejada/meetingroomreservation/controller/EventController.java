package com.ejada.meetingroomreservation.controller;

import com.ejada.meetingroomreservation.DTO.ApprovedEventReq;
import com.ejada.meetingroomreservation.DTO.EventDTO;
import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.service.Event.EventService;
import com.ejada.meetingroomreservation.service.User.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/Event")

public class EventController extends BaseController {
	private static final Logger logger = LogManager.getLogger(EventController.class);
	@Autowired
	private EventService eventService;
	@Autowired
	private UserService userService;

	@GetMapping("getAllEvents")
	@PreAuthorize("hasAuthority('getAllEvents')")
	public ResponseEntity<ResponseData> getAllEvents() {
		try {
			logger.info("getAllEvents");
			List<EventDTO> events = eventService.getAllEvents();
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(events);
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@PostMapping("getEventById")
	@PreAuthorize("hasAuthority('getEventById')")
	public ResponseEntity<ResponseData> getEventById(@RequestBody RequestData requestData) {
		try {
			logger.info("getEventById");
			EventDTO eventDTO = (EventDTO) responseReqBuilder.getRequestData(requestData, EventDTO.class);
			eventDTO = eventService.getEventById(eventDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(eventDTO);
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@PostMapping("getPendingEvents")
	@PreAuthorize("hasAuthority('getPendingEvents')")
	public ResponseEntity<ResponseData> getPendingEvents() {
		try {

			List<EventDTO> events = eventService.findPendingEvents();
			logger.info(events);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(events);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			logger.error(e);
			return wrapException(e, logger);
		}
	}

	@PostMapping("getAllUpcommingEvents")
	@PreAuthorize("hasAuthority('getAllUpcommingEvents')")
	public ResponseEntity<ResponseData> getAllUpcommingEvents(@RequestBody ApprovedEventReq req) {
		try {
			List<EventDTO> events = eventService.findAllUpcommingEvents(req);
			logger.info(events);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(events);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			logger.error(e);
			return wrapException(e, logger);
		}
	}

	@PostMapping("getUpcomingEvents/{username}")
	@PreAuthorize("hasAuthority('getUpcomingEvents')")
	public ResponseEntity<ResponseData> getUpcomingEvents(@PathVariable String username) {
		try {
			UserDTO user = userService.getUserByUsername(username);
			if (user == null)
				throw new EntityNotFoundException("User not found");

			List<EventDTO> events = eventService.findUpcomingEvents(user);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(events);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			logger.error(e);
			return wrapException(e, logger);
		}
	}

	@PostMapping("addEvent")
	@PreAuthorize("hasAuthority('addEvent')")
	public ResponseEntity<ResponseData> addEvent(@RequestBody EventDTO eventDTO) {
		try {
			logger.info("addEvent");
			EventDTO newEventDTO = eventService.addEvent(eventDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newEventDTO);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@PostMapping("approveEvent")
	@PreAuthorize("hasAuthority('approveEvent')")
	public ResponseEntity<ResponseData> approveEvent(@RequestBody EventDTO eventDTO) {
		try {
			logger.info("approveEvent");
			EventDTO newEventDTO = eventService.approveEvent(eventDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newEventDTO);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@PutMapping("updateEvent")
	@PreAuthorize("hasAuthority('updateEvent')")
	public ResponseEntity<ResponseData> updateEvent(@RequestBody EventDTO eventDTO) {
		try {
			logger.info("updateEvent");
			EventDTO newEventDTO = eventService.updateEvent(eventDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newEventDTO);
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}

	@DeleteMapping("deleteEvent")
	@PreAuthorize("hasAuthority('deleteEvent')")
	public ResponseEntity<ResponseData> deleteEvent(@RequestBody EventDTO eventDTO) {
		try {
			boolean isDeleted = eventService.deleteEvent(eventDTO);
			ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(isDeleted);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			return wrapException(e, logger);
		}
	}
}
