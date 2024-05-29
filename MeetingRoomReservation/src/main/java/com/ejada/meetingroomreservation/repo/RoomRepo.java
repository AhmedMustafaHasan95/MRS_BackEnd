package com.ejada.meetingroomreservation.repo;

import com.ejada.meetingroomreservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepo extends JpaRepository<Room, Long> {

    List<Room> findAllByOfficeId(Long officeId);

    Room findRoomById(Long id);
}
