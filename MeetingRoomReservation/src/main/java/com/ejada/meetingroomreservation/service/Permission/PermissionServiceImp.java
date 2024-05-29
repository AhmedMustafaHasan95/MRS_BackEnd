package com.ejada.meetingroomreservation.service.Permission;

import com.ejada.meetingroomreservation.DTO.ErrorDTO;
import com.ejada.meetingroomreservation.DTO.PermissionDTO;
import com.ejada.meetingroomreservation.Exceptions.EntityNotFoundException;
import com.ejada.meetingroomreservation.Exceptions.ValidationsException;
import com.ejada.meetingroomreservation.config.AppValidator;
import com.ejada.meetingroomreservation.entity.Permission;
import com.ejada.meetingroomreservation.repo.PermissionRepo;
import com.ejada.meetingroomreservation.service.country.CountryServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

@Service
public class PermissionServiceImp implements PermissionService {
	private final static Logger logger = LogManager.getLogger(CountryServiceImp.class);
	@Autowired
	PermissionRepo repo;
	@Autowired
	ModelMapper mapper;
	@Autowired
	AppValidator appValidator;

	@Override
	public List<PermissionDTO> getAllPermissions() throws Exception {
		return repo.findAll().stream().map(permission -> mapper.map(permission, PermissionDTO.class)).toList();
	}

	@Override
	public PermissionDTO getPermissionById(PermissionDTO PermissionDTO) throws Exception {
		try {
			appValidator.validateID(PermissionDTO.getId());
			Optional<Permission> permission = repo.findById(PermissionDTO.getId());
			if (permission.isPresent()) {
				return mapper.map(permission.get(), PermissionDTO.class);
			}
			throw new EntityNotFoundException("Permission", PermissionDTO.getId());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public PermissionDTO addPermission(PermissionDTO PermissionDTO) throws Exception {
		try {
			Permission permission = mapper.map(PermissionDTO, Permission.class);
			permission = repo.save(permission);
			return mapper.map(permission, PermissionDTO.class);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public PermissionDTO updatePermission(PermissionDTO PermissionDTO) throws Exception {
		try {
			List<ErrorDTO> errors = appValidator.validate(PermissionDTO);
			if (!errors.isEmpty())
				throw new ValidationsException(errors);
			if (repo.existsById(PermissionDTO.getId())) {
				Permission permission = mapper.map(PermissionDTO, Permission.class);
				permission = repo.save(permission);
				return mapper.map(permission, PermissionDTO.class);
			}
			throw new EntityNotFoundException("Permission", PermissionDTO.getId());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public boolean deletePermission(PermissionDTO PermissionDTO) throws Exception {
		try {
			appValidator.validateID(PermissionDTO.getId());
			if (repo.existsById(PermissionDTO.getId())) {
				repo.deleteById(PermissionDTO.getId());
				return true;
			}
			throw new EntityNotFoundException("Permission", PermissionDTO.getId());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<PermissionDTO> addPermissions(List<PermissionDTO> permissionsDTO) throws Exception {
		try {
			if (permissionsDTO == null || permissionsDTO.isEmpty())
				throw new ValidationException("permissions list can not be empty");

			List<ErrorDTO> errors = appValidator.validate(permissionsDTO);
			if (!errors.isEmpty())
				throw new ValidationsException(errors);
			List<Permission> permissios = permissionsDTO.stream().map(p -> mapper.map(p, Permission.class)).toList();
			return repo.saveAll(permissios).stream().map(p -> mapper.map(p, PermissionDTO.class)).toList();
		} catch (Exception e) {
			logger.error(e);
			throw e;

		}
	}
}
