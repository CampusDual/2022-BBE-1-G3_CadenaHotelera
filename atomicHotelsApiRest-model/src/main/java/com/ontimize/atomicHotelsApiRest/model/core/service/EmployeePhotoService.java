package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.processing.Filer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IEmployeePhotoService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.EmployeeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.EmployeePhotoDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.util.remote.BytesBlock;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeePhotoService")
@Lazy

public class EmployeePhotoService implements IEmployeePhotoService {

	@Autowired
	private EmployeePhotoDao dao;

	@Autowired
	private EmployeeService es;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeePhotoInsert(Map<String, Object> data) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {
			Path p = Paths.get((String) data.get(dao.ATTR_FILE));
			cf.reset();
			cf.addBasics(dao.fields);
			List<String> required = new ArrayList<>() {
				{
					add(dao.ATTR_NAME);
					add(dao.ATTR_FILE);
					add(dao.ATTR_EMPLOYEE_DNI);
				}
			};
			cf.setRequired(required);
			cf.validate(data);
			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(EmployeeDao.ATTR_IDEN_DOC, data.get(dao.ATTR_EMPLOYEE_DNI));
				}
			};

			if (es.employeeQuery(consultaKeyMap, List.of(EmployeeDao.ATTR_IDEN_DOC)).calculateRecordNumber() == 0) {
				resultado.setMessage("El documento de indentidad " + data.get(dao.ATTR_EMPLOYEE_DNI)
						+ " no existe en la base de datos empleados.");
			} else {
				if (Files.exists(p)) {
					data.put(dao.ATTR_FILE, Files.readAllBytes(p));
					resultado = daoHelper.insert(dao, data);
					resultado.setMessage("El archivo" + p.toAbsolutePath().toString()
							+ " cargado correctamente para el DNI " + data.get(dao.ATTR_EMPLOYEE_DNI) + ".");
				} else {
					resultado.setMessage("El archivo" + p.toAbsolutePath().toString() + " no existe. ");
				}
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado.setMessage("El Dni introducido " + data.get(dao.ATTR_EMPLOYEE_DNI) + " ya tiene foto asociada");
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity getPicture(Map<String, Object> filter, List<String> columns) {
		EntityResult resultado = new EntityResultWrong();

		Map<String, Object> consultaKeyMap = new HashMap<>() {
			{

				put(dao.ATTR_EMPLOYEE_DNI, filter.get(dao.ATTR_EMPLOYEE_DNI));
			}
		};
		Map<String, Object> consultaKeyMap2 = new HashMap<>() {
			{

				put(EmployeeDao.ATTR_IDEN_DOC, filter.get(dao.ATTR_EMPLOYEE_DNI));
			}
		};

		if (employeePhotoQuery(consultaKeyMap, List.of(dao.ATTR_EMPLOYEE_DNI)).calculateRecordNumber() > 0
				&& es.employeeQuery(consultaKeyMap2, List.of(EmployeeDao.ATTR_IDEN_DOC)).calculateRecordNumber() > 0) {

			resultado = this.daoHelper.query(dao, filter, columns);
			BytesBlock bytes = (BytesBlock) resultado.getRecordValues(0).get(dao.ATTR_FILE);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.IMAGE_JPEG);
			String pictureName = "picture.jpg";
			header.setContentDispositionFormData(pictureName, pictureName);
			header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity(bytes.getBytes(), header, HttpStatus.OK);

		} else {
			filter.put(dao.ATTR_EMPLOYEE_DNI, "00000000");
			resultado = this.daoHelper.query(dao, filter, columns);
			BytesBlock bytes = (BytesBlock) resultado.getRecordValues(0).get(dao.ATTR_FILE);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.IMAGE_JPEG);
			String pictureName = "picture.jpg";
			header.setContentDispositionFormData(pictureName, pictureName);
			header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity(bytes.getBytes(), header, HttpStatus.OK);
		
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeePhotoDelete(Map<String, Object> filter) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(List.of(dao.ATTR_EMPLOYEE_DNI));
			cf.validate(filter);
			Map<String, Object> consulta = new HashMap<>();
			consulta.put(dao.ATTR_EMPLOYEE_DNI, filter.get(dao.ATTR_EMPLOYEE_DNI));

			if (employeePhotoQuery(consulta, List.of(dao.ATTR_EMPLOYEE_DNI)).calculateRecordNumber() > 0) {
				resultado = daoHelper.delete(dao, filter);
				resultado.setMessage("Foto del DNI : " + filter.get(dao.ATTR_EMPLOYEE_DNI) + ". borrada");
			} else {
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeePhotoQuery(Map<String, Object> filter, List<String> columns)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {
			cf.reset();
			
			cf.setCPHtlColum(EmployeeDao.ATTR_HOTEL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			
			cf.addBasics(dao.fields);
//			cf.setRequired(List.of(dao.ATTR_EMPLOYEE_DNI));
			cf.validate(filter);
			
			
			cf.validate(columns);
			resultado = daoHelper.query(dao, filter, columns);
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;

	}

}
