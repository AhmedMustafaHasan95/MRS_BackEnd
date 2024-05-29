package com.ejada.meetingroomreservation.service.office;

import com.ejada.meetingroomreservation.DTO.OfficeDTO;

import java.util.List;

public interface OfficeService {
    public List<OfficeDTO> getAllOffices() throws Exception;

    public OfficeDTO getOfficeById(OfficeDTO officeDTO) throws Exception;

    public OfficeDTO addOffice(OfficeDTO officeDTO) throws Exception;

    public OfficeDTO updateOffice(OfficeDTO officeDTO) throws Exception;

    public boolean deleteOffice(OfficeDTO officeDTO) throws Exception;

    public List<OfficeDTO> getAllOfficesByCountryId(Long countryId) throws Exception;
}
