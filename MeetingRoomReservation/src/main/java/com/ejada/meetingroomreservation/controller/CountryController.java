package com.ejada.meetingroomreservation.controller;


import com.ejada.meetingroomreservation.DTO.CountryDTO;
import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.ejada.meetingroomreservation.service.country.CountryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Country")
public class CountryController extends BaseController {
    private static final Logger logger = LogManager.getLogger(CountryController.class);
    @Autowired
    private CountryService countryService;


    @GetMapping("getCountryList")
    @PreAuthorize("hasAuthority('getCountryList')")
    public ResponseEntity<ResponseData> getAllcountry() {
        try {
            logger.info("getCountryList");
            List<CountryDTO> countries = countryService.getAllCountries();
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(countries);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e,logger);
        }
    }
    @PostMapping("getCountryById")
    @PreAuthorize("hasAuthority('getCountryById')")
    public ResponseEntity<ResponseData> getCountryById(@RequestBody RequestData requestData) {
        try {
            logger.info("getCountryById");
            CountryDTO CountryDTO = (CountryDTO) responseReqBuilder.getRequestData(requestData, CountryDTO.class);
            CountryDTO = countryService.getCountryById(CountryDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(CountryDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e,logger);
        }
    }


    @PostMapping("addCountry")
    @PreAuthorize("hasAuthority('addCountry')")
    public ResponseEntity<ResponseData> addCountry(@RequestBody RequestData requestData) {
        CountryDTO countryDTO = null;
        try {
            logger.info("addCountry");
            countryDTO = (CountryDTO) responseReqBuilder.getRequestData(requestData, CountryDTO.class);
            CountryDTO newCountryDTO = countryService.addCountry(countryDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newCountryDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e,logger);
        }
    }

    @PutMapping("updateCountry")
    @PreAuthorize("hasAuthority('updateCountry')")
    public ResponseEntity<ResponseData> updateCountry(@RequestBody RequestData requestData) throws Exception {
        CountryDTO CountryDTO = null;
        try {
            logger.info("updateCountry");
            CountryDTO = (CountryDTO) responseReqBuilder.getRequestData(requestData, CountryDTO.class);
            CountryDTO newCountryDTO = countryService.updateCountry(CountryDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse(newCountryDTO);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e,logger);
        }
    }
    @DeleteMapping("deleteCountry")
    @PreAuthorize("hasAuthority('deleteCountry')")
    public ResponseEntity<ResponseData> deleteCountry(@RequestBody RequestData requestData) throws Exception {
        CountryDTO CountryDTO = null;
        try {
            logger.info("deleteCountry");
            CountryDTO = (CountryDTO) responseReqBuilder.getRequestData(requestData, CountryDTO.class);
            boolean isDeleted = countryService.deleteCountry(CountryDTO);
            ResponseData apiResponse = responseReqBuilder.wrapSuccessResponse("deleted",isDeleted);
             return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return wrapException(e,logger);
        }
    }

}