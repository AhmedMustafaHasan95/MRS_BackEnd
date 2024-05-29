package com.ejada.meetingroomreservation.controller;

import com.ejada.meetingroomreservation.DTO.OfficeDTO;
import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.service.office.OfficeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Office")
public class OfficeController extends BaseController {
    private static final Logger logger = LogManager.getLogger(OfficeController.class);
    @Autowired
    private OfficeService officeService;

    @GetMapping("getAllOffices")
    @PreAuthorize("hasAuthority('getAllOffices')")
    public ResponseEntity<ResponseData> getAllOffices() {
        try {
            logger.info("getAllOffices");
            List<OfficeDTO> offices = officeService.getAllOffices();
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(offices);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PostMapping("getOfficeById")
    @PreAuthorize("hasAuthority('getOfficeById')")
    public ResponseEntity<ResponseData> getOfficeById(@RequestBody RequestData requestData) {
        try {
            logger.info("getOfficeById");
            OfficeDTO officeDTO = (OfficeDTO) responseReqBuilder.getRequestData(requestData, OfficeDTO.class);
            officeDTO = officeService.getOfficeById(officeDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(officeDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PostMapping("addOffice")
    @PreAuthorize("hasAuthority('addOffice')")
    public ResponseEntity<ResponseData> addOffice(@RequestBody RequestData requestData) {
        OfficeDTO officeDTO = null;
        try {
            logger.info("addOffice");
            officeDTO = (OfficeDTO) responseReqBuilder.getRequestData(requestData, OfficeDTO.class);
            OfficeDTO newOfficeDTO = officeService.addOffice(officeDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newOfficeDTO);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @PutMapping("updateOffice")
    @PreAuthorize("hasAuthority('updateOffice')")
    public ResponseEntity<ResponseData> updateOffice(@RequestBody RequestData requestData) {
        OfficeDTO officeDTO = null;
        try {
            logger.info("updateOffice");
            officeDTO = (OfficeDTO) responseReqBuilder.getRequestData(requestData, OfficeDTO.class);
            OfficeDTO newOfficeDTO = officeService.updateOffice(officeDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newOfficeDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @DeleteMapping("deleteOffice")
    @PreAuthorize("hasAuthority('deleteOffice')")
    public ResponseEntity<ResponseData> deleteOffice(@RequestBody RequestData requestData) {
        OfficeDTO officeDTO = null;
        try {
            logger.info("deleteOffice");
            officeDTO = (OfficeDTO) responseReqBuilder.getRequestData(requestData, OfficeDTO.class);
            boolean isDeleted = officeService.deleteOffice(officeDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(isDeleted);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            return wrapException(e, logger);
        }
    }

    @GetMapping("/getOfficesOfCountry/{countryId}")
    @PreAuthorize("hasAuthority('getOfficesOfCountry')")
    public ResponseEntity<ResponseData> getAllOfficesByCountry(@PathVariable Long countryId) throws Exception {
        try {
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(officeService.getAllOfficesByCountryId(countryId));
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return wrapException(e, logger);
        }
    }
}
