package com.ejada.meetingroomreservation.service.country;

import com.ejada.meetingroomreservation.DTO.CountryDTO;
import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.Country;
import com.ejada.meetingroomreservation.repo.CountryRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CountryServiceImp implements CountryService {
    private final static Logger logger = LogManager.getLogger(CountryServiceImp.class);
    @Autowired
    CountryRepo countryRepo;
    @Autowired
    ModelMapper mapper;
    @Autowired
    AppValidator appValidator;

    @Override
    public List<CountryDTO> getAllCountries() {
        return countryRepo.findAll().stream().map(country -> mapper.map(country, CountryDTO.class)).collect(Collectors.toList());
    }

    @Override
    public CountryDTO getCountryById(CountryDTO countryDTO) {
        try {
            appValidator.validateID(countryDTO.getId());
            Optional<Country> Country = countryRepo.findById(countryDTO.getId());
            if (Country.isPresent()) {
                return mapper.map(Country.get(), CountryDTO.class);
            }
            throw new EntityNotFoundException("Country", countryDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public CountryDTO addCountry(CountryDTO countryDTO) throws Exception {
        try {
            appValidator.validateDTO(countryDTO);
            Country country = mapper.map(countryDTO, Country.class);
            country = countryRepo.save(country);
            return mapper.map(country, CountryDTO.class);
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getCause().getCause().getMessage());
            throw new Exception(getErrorMSG(e));

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    private String getErrorMSG(DataIntegrityViolationException e) {
        Pattern pattern = Pattern.compile("Detail:.*");
        Matcher matcher = pattern.matcher(e.getCause().getCause().getMessage());
        return matcher.find() ?
                matcher.group() : "";
    }

    @Override
    public CountryDTO updateCountry(CountryDTO countryDTO) {
        try {
            List<ErrorDTO> errors = appValidator.validate(countryDTO);
            if (!errors.isEmpty()) throw new ValidationsException(errors);
            if (countryRepo.existsById(countryDTO.getId())) {
                Country country = mapper.map(countryDTO, Country.class);
                country = countryRepo.save(country);
                return mapper.map(country, CountryDTO.class);
            }
            throw new EntityNotFoundException("Country", countryDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean deleteCountry(CountryDTO countryDTO) {
        try {
            appValidator.validateID(countryDTO.getId());
            if (countryRepo.existsById(countryDTO.getId())) {
                countryRepo.deleteById(countryDTO.getId());
                return true;
            }
            throw new EntityNotFoundException("Countries", countryDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }
}
