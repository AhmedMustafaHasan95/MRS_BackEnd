package com.ejada.meetingroomreservation.repo;

import com.ejada.meetingroomreservation.Enums.EventStatus;
import com.ejada.meetingroomreservation.entity.Events;
import com.ejada.meetingroomreservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface EventRepo extends JpaRepository<Events, Long> {
	@Query("SELECT e FROM Events e " + "WHERE e.room.id = :roomId " + "AND e.eventDate = :eventDate "
			+ "AND ((e.startAt <= :endAt AND e.endAt >= :startAt) "
			+ "OR (e.startAt >= :startAt AND e.startAt < :endAt) " + "OR (e.endAt > :startAt AND e.endAt <= :endAt))")
	List<Events> findOverlappingEvents(Long roomId, LocalDate eventDate, LocalTime startAt, LocalTime endAt);

	@Query("SELECT e FROM Events e " + "WHERE e.room = :room " + "AND e.eventDate = :eventDate "
			+ "AND ((e.startAt >= :startAt AND e.startAt < :endAt) OR "
			+ "     (e.endAt > :startAt AND e.endAt <= :endAt))")
	List<Events> findOverlappingEvents(@Param("room") Room room, @Param("eventDate") LocalDate eventDate,
			@Param("startAt") LocalTime startAt, @Param("endAt") LocalTime endAt);

	@Query("SELECT e FROM Events e WHERE e.eventDate >= :currentDate AND e.creator.id = :userId ORDER BY e.eventDate, e.startAt, e.endAt")
	List<Events> findUpcomingEventsForUser(LocalDate currentDate, Long userId);

	//// AND e.status='Accepted'
	@Query("SELECT e FROM Events e WHERE e.eventDate >= :startDate AND  e.eventDate <= :endDate AND e.status =:status ORDER BY e.eventDate, e.startAt, e.endAt")
	List<Events> findUpcomingEvents(LocalDate startDate, LocalDate endDate, EventStatus status);

	@Query("SELECT e FROM Events e WHERE e.eventDate >= :currentDate AND (e.status IS NULL OR e.status = :status) ORDER BY e.eventDate, e.startAt, e.endAt")
	List<Events> findPendingEvents(LocalDate currentDate, EventStatus status);

}
