package com.ejada.meetingroomreservation.utils;

import com.ejada.meetingroomreservation.DTO.ApiStatusDTO;
import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.DTO.RequestData;
import com.ejada.meetingroomreservation.DTO.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResponseRequestBuilder<T> {

    @Autowired
    ObjectMapper objectMapper;

    public <T> T getRequestData(RequestData requestDTO, Class<T> clazz) throws Exception {
        try {
            if (requestDTO.getData() == null) {
                throw new Exception("Missing Request Body");
            }
            return objectMapper.convertValue(requestDTO.getData(), clazz);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Error in parsing request data");
        }
    }

   /**
     * @param status
     * @return
     */
    public ResponseData wrapEmptySuccessResponse(ApiStatusDTO status) {
        ResponseData response = new ResponseData();
        response.setStatus(status);
        return response;
    }

    /**
     * @param message
     * @return
     */
    public ResponseData wrapFailureResponse(String message) {
        return wrapEmptySuccessResponse(generateFailureResponse(message));
    }

    public ResponseData wrapFailureResponse(List<ErrorDTO> errors) {
        return wrapEmptySuccessResponse(new ApiStatusDTO(errors));
    }


    /**
     * @param value
     * @param name
     * @return
     */
    public ResponseData wrapSuccessResponse(String name, T value) {
        Map<String, Object> data = new HashMap<>();
        ResponseData response = new ResponseData();
        data.put(name, value);
        response.setData(data);
        response.setStatus(generateSuccessResponse());
        return response;
    }

    public ResponseData wrapSuccessResponse(T data) {
        ResponseData response = new ResponseData();
        response.setData(data);
        response.setStatus(generateSuccessResponse());
        return response;
    }

    /**
     * @return
     */
    private ApiStatusDTO generateSuccessResponse() {
        return new ApiStatusDTO("200", "Operation done successfully");
    }

    /**
     * @param message
     * @return
     */
    private ApiStatusDTO generateFailureResponse(String message) {
        return new ApiStatusDTO("500", message);
    }

    public ResponseData wrapFailureResponseWithCode(String message, String code) {
        return wrapEmptySuccessResponse(generateFailureResponseWithCode(message, code));
    }

    private ApiStatusDTO generateFailureResponseWithCode(String message, String code) {
        return new ApiStatusDTO(code, message);
    }
}