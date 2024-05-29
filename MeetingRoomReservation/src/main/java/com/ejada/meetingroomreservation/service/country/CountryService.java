package com.ejada.meetingroomreservation.service.country;

import com.ejada.meetingroomreservation.DTO.CountryDTO;

import java.util.List;

public interface CountryService {
    public List<CountryDTO> getAllCountries() throws Exception;

    public CountryDTO getCountryById(CountryDTO countryDTO) throws Exception;

    public CountryDTO addCountry(CountryDTO countryDTO) throws Exception;

    public CountryDTO updateCountry(CountryDTO countryDTO) throws Exception;

    public boolean deleteCountry(CountryDTO countryDTO) throws Exception;
}
