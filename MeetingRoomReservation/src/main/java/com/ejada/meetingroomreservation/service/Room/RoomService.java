package com.ejada.meetingroomreservation.service.Room;

import com.ejada.meetingroomreservation.DTO.RoomDTO;

import java.util.List;

public interface RoomService {
    public List<RoomDTO> getAllRooms() throws Exception;

    public RoomDTO getRoomById(RoomDTO RoomDTO) throws Exception;

    public RoomDTO addRoom(RoomDTO RoomDTO) throws Exception;

    public RoomDTO updateRoom(RoomDTO RoomDTO) throws Exception;

    public boolean deleteRoom(RoomDTO RoomDTO) throws Exception;

    List<RoomDTO> getAllRoomsByOfficeId(Long roomId) throws Exception;
}
