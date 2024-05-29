package com.ejada.meetingroomreservation.service.Event;

import com.ejada.meetingroomreservation.DTO.ApprovedEventReq;
import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.DTO.EventDTO;
import com.ejada.meetingroomreservation.DTO.UserDTO;
import com.ejada.meetingroomreservation.Enums.EventStatus;
import com.ejada.meetingroomreservation.Enums.EventType;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import com.ejada.meetingroomreservation.auth.exceptions.UserAuthenticationException;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.Events;
import com.ejada.meetingroomreservation.entity.Room;
import com.ejada.meetingroomreservation.repo.EventRepo;
import com.ejada.meetingroomreservation.repo.RoomRepo;
import com.ejada.meetingroomreservation.service.User.UserService;
import com.ejada.meetingroomreservation.service.country.CountryServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImp implements EventService {
	private final static Logger logger = LogManager.getLogger(CountryServiceImp.class);
	@Autowired
	AppValidator appValidator;
	@Autowired
	EventRepo repo;
	@Autowired
	UserService userService;
	@Autowired
	ModelMapper mapper;
	@Autowired
	private RoomRepo roomRepo;

	@Override
	public List<EventDTO> getAllEvents() throws Exception {
		return repo.findAll().stream().map(Event -> mapper.map(Event, EventDTO.class)).toList();
	}

	@Override
	public EventDTO getEventById(EventDTO eventDTO) throws Exception {
		try {
			appValidator.validateID(eventDTO.getId());
			Optional<Events> event = repo.findById(eventDTO.getId());
			if (event.isPresent()) {
				return mapper.map(event.get(), EventDTO.class);
			}
			throw new EntityNotFoundException("Event", eventDTO.getId());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public EventDTO addEvent(EventDTO dto) throws Exception {
		try {
			UserDTO user = userService.getUserByUsername(dto.getCreator().getUsername());
			if (user.getBlocked())
				throw new UserAuthenticationException(user.getUsername() + " is blocked and can not add events");
			dto.setCreator(user);
			Events event = validateEvent(dto);
			event = repo.save(event);
			return mapper.map(event, EventDTO.class);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	private Events validateEvent(EventDTO eventDTO) throws Exception {
		List<ErrorDTO> errors = new ArrayList<ErrorDTO>();
		Events event = mapper.map(eventDTO, Events.class);
		event.setEventDate(LocalDate.parse(eventDTO.getEventDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		event.setStartAt(LocalTime.parse(eventDTO.getStartAt(), DateTimeFormatter.ofPattern("HH:mm")));
		event.setEndAt(LocalTime.parse(eventDTO.getEndAt(), DateTimeFormatter.ofPattern("HH:mm")));
		if (event.getEventDate().isBefore(LocalDate.now()))
			errors.add(new ErrorDTO("eventDate", "Event date can not be before today"));
		if (!event.getStartAt().isBefore(event.getEndAt()))
			errors.add(new ErrorDTO("startAt", "Event start time :" + eventDTO.getStartAt()
					+ " can not be equal or after end time :" + eventDTO.getEndAt()));
		if (!errors.isEmpty())
			throw new ValidationsException(errors);
		Room room = roomRepo.findRoomById(eventDTO.getRoom().getId());
		event.setRoom(room);
		event.setStartAt(event.getStartAt().plusSeconds(1));
		List<Events> overlappingEvents = this.findOverlappingEvents(event);
		if (event.getId() != 0)
			overlappingEvents = overlappingEvents.stream().filter(e -> e.getId() != event.getId()).toList();
		if (overlappingEvents != null && !overlappingEvents.isEmpty() && !overlappingEvents.contains(event))
			throw new ValidationsException("\"Event overlaps with existing events");
		return event;
	}

	@Override
	public List<Events> findOverlappingEvents(Events events) {
		return repo.findOverlappingEvents(events.getRoom(), events.getEventDate(), events.getStartAt(),
				events.getEndAt());
	}

	@Override
	public List<EventDTO> findUpcomingEvents(UserDTO user) throws Exception {
		try {
			List<Events> events = repo.findUpcomingEventsForUser(LocalDate.now(), user.getId());
			return events.stream().map(e -> mapper.map(e, EventDTO.class)).toList();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<EventDTO> findPendingEvents() throws Exception {
		try {
			List<Events> events = repo.findPendingEvents(LocalDate.now(), EventStatus.Pending);
			return events.stream().map(e -> mapper.map(e, EventDTO.class)).toList();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<EventDTO> findAllUpcommingEvents(ApprovedEventReq req) throws Exception {
		try {
			LocalDate startDate = (req.getStartDate() == null || req.getStartDate().isBlank())
					? LocalDate.now().withDayOfMonth(1)
					: LocalDate.parse(req.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			LocalDate endDate = (req.getEndDate() == null || req.getEndDate().isBlank())
					? LocalDate.now().withDayOfMonth(1)
					: LocalDate.parse(req.getEndDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			List<Events> events = repo.findUpcomingEvents(startDate, endDate, EventStatus.Accepted);
			return events.stream().map(e -> mapper.map(e, EventDTO.class)).toList();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public EventDTO updateEvent(EventDTO eventDTO) throws Exception {
		try {
			UserDTO user = userService.getUserByUsername(eventDTO.getCreator().getUsername());
			if (user.getBlocked())
				throw new UserAuthenticationException(user.getUsername() + " is blocked and can not add events");
			Optional<Events> eventOption = repo.findById(eventDTO.getId());
			if (eventOption.isEmpty())
				throw new EntityNotFoundException("Could not find event with ID : " + eventDTO.getId());
			Events withUpdatesEvent = validateEvent(eventDTO);
			Events toUpdateEvent = eventOption.get();
			toUpdateEvent.setContactEmail(withUpdatesEvent.getContactEmail());
			toUpdateEvent.setContactPerson(withUpdatesEvent.getContactPerson());
			toUpdateEvent.setDescription(withUpdatesEvent.getDescription());
			toUpdateEvent.setEndAt(withUpdatesEvent.getEndAt());
			toUpdateEvent.setEventDate(withUpdatesEvent.getEventDate());
			toUpdateEvent.setLocation(withUpdatesEvent.getLocation());
			toUpdateEvent.setName(withUpdatesEvent.getName());
			toUpdateEvent.setRoom(withUpdatesEvent.getRoom());
			toUpdateEvent.setStartAt(withUpdatesEvent.getStartAt());
			toUpdateEvent.setType(withUpdatesEvent.getType());
			toUpdateEvent = repo.save(toUpdateEvent);
			return mapper.map(toUpdateEvent, EventDTO.class);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public boolean deleteEvent(EventDTO eventDTO) throws Exception {
		try {
			appValidator.validateID(eventDTO.getId());
			if (repo.existsById(eventDTO.getId())) {
				repo.deleteById(eventDTO.getId());
				return true;
			}
			throw new EntityNotFoundException("Event", eventDTO.getId());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public EventDTO approveEvent(EventDTO dto) throws Exception {
		try {
			UserDTO user = userService.getUserByUsername(dto.getCreator().getUsername());
			if (user.getBlocked())
				throw new UserAuthenticationException(user.getUsername() + " is blocked and can not add events");
			dto.setCreator(user);
			Optional<Events> eventOption = repo.findById(dto.getId());
			if (eventOption.isEmpty())
				throw new EntityNotFoundException("Could not find event with ID : " + dto.getId());
			Events withUpdatesEvent = validateEvent(dto);
			Events toUpdateEvent = eventOption.get();
			toUpdateEvent.setStatus(EventStatus.Accepted);
			toUpdateEvent = repo.save(toUpdateEvent);
			return mapper.map(toUpdateEvent, EventDTO.class);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}
}
