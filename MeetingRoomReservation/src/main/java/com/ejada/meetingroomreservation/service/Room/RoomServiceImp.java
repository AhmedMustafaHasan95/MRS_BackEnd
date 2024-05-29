package com.ejada.meetingroomreservation.service.Room;

import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.DTO.RoomDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.Office;
import com.ejada.meetingroomreservation.entity.Room;
import com.ejada.meetingroomreservation.repo.OfficeRepo;
import com.ejada.meetingroomreservation.repo.RoomRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImp implements RoomService {
    private final static Logger logger = LogManager.getLogger(RoomServiceImp.class);
    @Autowired
    RoomRepo repo;
    @Autowired
    OfficeRepo officeRepo;
    @Autowired
    ModelMapper mapper;
    @Autowired
    AppValidator appValidator;

    @Override
    public List<RoomDTO> getAllRooms() {
        List<Room> offices = repo.findAll();
        return offices.stream().map(room -> mapper.map(room, RoomDTO.class)).collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(RoomDTO roomDTO) {
        try {
            appValidator.validateID(roomDTO.getId());
            Optional<Room> room = repo.findById(roomDTO.getId());
            if (room.isPresent()) {
                return mapper.map(room.get(), RoomDTO.class);
            }
            throw new EntityNotFoundException("Room", roomDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public RoomDTO addRoom(RoomDTO roomDTO) {
        try {
            Room room = mapper.map(roomDTO, Room.class);
            if (room.getOffice() != null && room.getOffice().getId() != 0) {
                Optional<Office> optionalOffice = officeRepo.findById(roomDTO.getOffice().getId());
                if (optionalOffice.isPresent())
                    room.setOffice(optionalOffice.get());
                else throw new EntityNotFoundException("Office", roomDTO.getOffice());
            } else throw new EntityNotFoundException("Office", roomDTO.getOffice());
            room = repo.save(room);
            return mapper.map(room, RoomDTO.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public RoomDTO updateRoom(RoomDTO roomDTO) {
        try {
            List<ErrorDTO> errors = appValidator.validate(roomDTO);
            if (!errors.isEmpty()) throw new ValidationsException(errors);
            if (repo.existsById(roomDTO.getId())) {
                Room room = mapper.map(roomDTO, Room.class);
                room = repo.save(room);
                return mapper.map(room, RoomDTO.class);
            }
            throw new EntityNotFoundException("Room", roomDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean deleteRoom(RoomDTO roomDTO) {
        try {
            appValidator.validateID(roomDTO.getId());
            if (repo.existsById(roomDTO.getId())) {
                repo.deleteById(roomDTO.getId());
                return true;
            }
            throw new EntityNotFoundException("Room", roomDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<RoomDTO> getAllRoomsByOfficeId(Long OfficeId) throws Exception {
        try {
            appValidator.validateID(OfficeId);
            return repo.findAllByOfficeId(OfficeId).stream().map(room -> mapper.map(room, RoomDTO.class)).toList();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }
}
