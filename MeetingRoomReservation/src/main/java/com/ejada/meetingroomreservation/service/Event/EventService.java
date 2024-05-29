package com.ejada.meetingroomreservation.service.Event;

import com.ejada.meetingroomreservation.DTO.ApprovedEventReq;
import com.ejada.meetingroomreservation.DTO.EventDTO;
import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.entity.Events;

import java.util.List;

public interface EventService {
	List<EventDTO> getAllEvents() throws Exception;

	EventDTO getEventById(EventDTO EventDTO) throws Exception;

	EventDTO addEvent(EventDTO EventDTO) throws Exception;

	EventDTO approveEvent(EventDTO EventDTO) throws Exception;

	EventDTO updateEvent(EventDTO EventDTO) throws Exception;

	boolean deleteEvent(EventDTO EventDTO) throws Exception;

	List<Events> findOverlappingEvents(Events events) throws Exception;

	List<EventDTO> findUpcomingEvents(UserDTO user) throws Exception;

	List<EventDTO> findAllUpcommingEvents(ApprovedEventReq req) throws Exception;

	List<EventDTO> findPendingEvents() throws Exception;


}
