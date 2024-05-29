package com.ejada.meetingroomreservation.service.Role;

import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.DTO.RoleDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.Role;
import com.ejada.meetingroomreservation.repo.RoleRepo;
import com.ejada.meetingroomreservation.service.country.CountryServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImp implements RoleService {
    private final static Logger logger = LogManager.getLogger(CountryServiceImp.class);

    @Autowired
    AppValidator appValidator;
    @Autowired
    RoleRepo repo;
    @Autowired
    ModelMapper mapper;

    @Override
    public List<RoleDTO> getAllRoles() throws Exception {
        return repo.findAll().stream().map(role -> mapper.map(role, RoleDTO.class)).toList();
    }

    @Override
    public RoleDTO getRoleById(RoleDTO roleDTO) throws Exception {
        try {
            appValidator.validateID(roleDTO.getId());
            Optional<Role> Role = repo.findById(roleDTO.getId());
            if (Role.isPresent()) {
                return mapper.map(Role.get(), RoleDTO.class);
            }
            throw new EntityNotFoundException("Role", roleDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public RoleDTO addRole(RoleDTO roleDTO) throws Exception {
        try {
            Role role = mapper.map(roleDTO, Role.class);
            role = repo.save(role);
            return mapper.map(role, RoleDTO.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public RoleDTO updateRole(RoleDTO roleDTO) throws Exception {
        try {
            List<ErrorDTO> errors = appValidator.validate(roleDTO);
            if (!errors.isEmpty()) throw new ValidationsException(errors);
            if (repo.existsById(roleDTO.getId())) {
                Role role = mapper.map(roleDTO, Role.class);
                role = repo.save(role);
                return mapper.map(role, RoleDTO.class);
            }
            throw new EntityNotFoundException("Role", roleDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean deleteRole(RoleDTO roleDTO) throws Exception {
        try {
            appValidator.validateID(roleDTO.getId());
            if (repo.existsById(roleDTO.getId())) {
                repo.deleteById(roleDTO.getId());
                return true;
            }
            throw new EntityNotFoundException("Role", roleDTO.getId());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }
}
