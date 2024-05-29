package com.ejada.meetingroomreservation.controller;

import com.ejada.meetingroomreservation.DTO.RoomDTO;
import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.entity.Office;
import com.ejada.meetingroomreservation.repo.OfficeRepo;
import com.ejada.meetingroomreservation.service.Room.RoomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Room")
public class RoomController extends BaseController {
    private static final Logger logger = LogManager.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;
    @Autowired
    private OfficeRepo officeRepo;

    @GetMapping("getAllRooms")
    @PreAuthorize("hasAuthority('getAllRooms')")
    public ResponseEntity<ResponseData> getAllRooms() {
        try {
            logger.info("getAllRooms");
            List<RoomDTO> rooms = roomService.getAllRooms();
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(rooms);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PostMapping("getRoomById")
    @PreAuthorize("hasAuthority('getRoomById')")
    public ResponseEntity<ResponseData> getRoomById(@RequestBody RequestData requestData) {
        try {
            logger.info("getRoomById");
            RoomDTO roomDTO = (RoomDTO) responseReqBuilder.getRequestData(requestData, RoomDTO.class);
            roomDTO = roomService.getRoomById(roomDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(roomDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PostMapping("addRoom")
    @PreAuthorize("hasAuthority('addRoom')")
    public ResponseEntity<ResponseData> addRoom(@RequestBody RequestData requestData) {
        RoomDTO roomDTO = null;
        try {
            logger.info("addRoom");
            roomDTO = (RoomDTO) responseReqBuilder.getRequestData(requestData, RoomDTO.class);
            RoomDTO newRoomDTO = roomService.addRoom(roomDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newRoomDTO);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PutMapping("updateRoom")
    @PreAuthorize("hasAuthority('updateRoom')")
    public ResponseEntity<ResponseData> updateRoom(@RequestBody RequestData requestData) {
        RoomDTO roomDTO = null;
        try {
            logger.info("updateRoom");
            roomDTO = (RoomDTO) responseReqBuilder.getRequestData(requestData, RoomDTO.class);
            RoomDTO newRoomDTO = roomService.updateRoom(roomDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newRoomDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @DeleteMapping("deleteRoom")
    @PreAuthorize("hasAuthority('deleteRoom')")
    public ResponseEntity<ResponseData> deleteRoom(@RequestBody RequestData requestData) {
        RoomDTO RoomDTO = null;
        try {
            logger.info("deleteRoom");
            RoomDTO = (RoomDTO) responseReqBuilder.getRequestData(requestData, RoomDTO.class);
            boolean isDeleted = roomService.deleteRoom(RoomDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(isDeleted);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @GetMapping("/getByOffice/{officeId}")
    @PreAuthorize("hasAuthority('getRoomByOffice')")
    public ResponseEntity<ResponseData> getAllRoomsByOffice(@PathVariable Long officeId) throws Exception {
        try {
            List<RoomDTO> rooms = roomService.getAllRoomsByOfficeId(officeId);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(rooms);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }
}