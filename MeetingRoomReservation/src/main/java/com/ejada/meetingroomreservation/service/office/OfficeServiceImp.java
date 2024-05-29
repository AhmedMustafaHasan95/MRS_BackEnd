package com.ejada.meetingroomreservation.service.office;

import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.DTO.OfficeDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.Office;
import com.ejada.meetingroomreservation.repo.OfficeRepo;
import com.ejada.meetingroomreservation.service.country.CountryServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfficeServiceImp implements OfficeService {
    private final static Logger logger = LogManager.getLogger(CountryServiceImp.class);
    @Autowired
    OfficeRepo repo;
    @Autowired
    ModelMapper mapper;
    @Autowired
    AppValidator appValidator;

    @Override
    public List<OfficeDTO> getAllOffices() {
        List<Office> offices = repo.findAll();
        return offices.stream().map(office -> mapper.map(office, OfficeDTO.class)).collect(Collectors.toList());
    }

    @Override
    public OfficeDTO getOfficeById(OfficeDTO officeDTO) {
        try {
            appValidator.validateID(officeDTO.getId());
            Optional<Office> office = repo.findById(officeDTO.getId());
            if (office.isPresent()) {
                return mapper.map(office.get(), OfficeDTO.class);
            }
            throw new EntityNotFoundException("Office", officeDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public OfficeDTO addOffice(OfficeDTO officeDTO) {
        try {
            Office office = mapper.map(officeDTO, Office.class);
            office = repo.save(office);
            return mapper.map(office, OfficeDTO.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public OfficeDTO updateOffice(OfficeDTO officeDTO) {
        try {
            List<ErrorDTO> errors = appValidator.validate(officeDTO);
            if (!errors.isEmpty()) throw new ValidationsException(errors);
            if (repo.existsById(officeDTO.getId())) {
                Office office = mapper.map(officeDTO, Office.class);
                office = repo.save(office);
                return mapper.map(office, OfficeDTO.class);
            }
            throw new EntityNotFoundException("Office", officeDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean deleteOffice(OfficeDTO officeDTO) {
        try {
            appValidator.validateID(officeDTO.getId());
            if (repo.existsById(officeDTO.getId())) {
                repo.deleteById(officeDTO.getId());
                return true;
            }
            throw new EntityNotFoundException("Office", officeDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<OfficeDTO> getAllOfficesByCountryId(Long countryId) throws Exception {
        try {
            List<ErrorDTO> errors = appValidator.validate(countryId);
            if (!errors.isEmpty()) throw new ValidationsException(errors);
            return repo.findAllByCountryId(countryId).stream().map(office -> mapper.map(office, OfficeDTO.class)).toList();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }
}
