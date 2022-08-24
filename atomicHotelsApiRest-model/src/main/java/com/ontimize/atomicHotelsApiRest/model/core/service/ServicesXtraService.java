package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingColumnsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("ServicesXtraService")
@Lazy
public class ServicesXtraService implements IServicesXtraService {

	@Autowired
	private ServicesXtraDao servicesXtraDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult servicesXtraQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(ServicesXtraDao.fields);
			cf.validate(keyMap);
			cf.validate(attrList);
			resultado = this.daoHelper.query(this.servicesXtraDao, keyMap, attrList);
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult servicesXtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			List<String> requeridos = new ArrayList<String>() {
				{
					add(ServicesXtraDao.ATTR_NAME);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(ServicesXtraDao.ATTR_ID);// No quiero que meta el id porque quiero el id autogenerado de la base
													// de datos
				}
			};

			cf.addBasics(ServicesXtraDao.fields);
			cf.setRequired(requeridos);
			cf.setRestricted(restricted);
			cf.setOptional(true);// El resto de los campos de fields serán aceptados
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.servicesXtraDao, attrMap);
			resultado.setMessage("Extra service registered");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult servicesXtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException { // attrMap filtro, keymap nuevo valor a actualizar

		EntityResult resultado = new EntityResultWrong();

		try {

			// ControlFields del filtro
			cf.reset();
			cf.addBasics(ServicesXtraDao.fields);
			List<String> requiredFilter = new ArrayList<String>() {
				{
//				add(ServicesXtraDao.ATTR_NAME);
					add(ServicesXtraDao.ATTR_ID);
				}
			};
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			cf.reset();
			cf.addBasics(ServicesXtraDao.fields);
			List<String> restrictedData = new ArrayList<String>() {
				{
					add(ServicesXtraDao.ATTR_ID);// El id no se puede actualizar
				}
			};
			cf.setRestricted(restrictedData);
			// cd.setOptional(true); //No es necesario ponerlo
			cf.validate(attrMap);

			resultado = this.daoHelper.update(this.servicesXtraDao, attrMap, keyMap);

			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Extra service updated");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult servicesXtraDelete(Map<String, Object> attrMap) throws OntimizeJEERuntimeException { // El
																												// filtro
																												// del
																												// where
																												// del
																												// postman

		EntityResult resultado = new EntityResultWrong();

		try {

			cf.reset();
			cf.addBasics(ServicesXtraDao.fields);
			List<String> requeridos = new ArrayList<String>() {
				{
					add(ServicesXtraDao.ATTR_ID);
				}
			};

			cf.setRequired(requeridos);
			// ValidateFields.atLeastOneRequired(requeridos);
			cf.setOptional(true);// El resto de los campos de fields serán aceptados
			cf.validate(attrMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(ServicesXtraDao.ATTR_ID, attrMap.get(ServicesXtraDao.ATTR_ID));
				}
			};

			EntityResult auxEntity = servicesXtraQuery(consultaKeyMap,
					EntityResultTools.attributes(ServicesXtraDao.ATTR_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.servicesXtraDao, attrMap);
				resultado.setMessage("Extra service deleted");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}

		/*
		 * try { ValidateFields.required(keyMap, ServicesXtraDao.ATTR_ID);
		 * ValidateFields.onlyThis(keyMap, ServicesXtraDao.ATTR_ID);
		 * ValidateFields.isInt(keyMap, ServicesXtraDao.ATTR_ID); //
		 * ValidateFields.emptyFields(keyMap, ServicesXtraDao.ATTR_ID);
		 * 
		 * Map<String, Object> consultaKeyMap = new HashMap<>() { {
		 * put(ServicesXtraDao.ATTR_ID, keyMap.get(ServicesXtraDao.ATTR_ID)); } };
		 * 
		 * EntityResult auxEntity = servicesXtraQuery(consultaKeyMap,
		 * EntityResultTools.attributes(ServicesXtraDao.ATTR_ID));
		 * 
		 * if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
		 * resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD); }
		 * else { resultado = this.daoHelper.delete(this.servicesXtraDao, keyMap);
		 * resultado.setMessage("Servicio extra eliminado"); } } catch
		 * (MissingFieldsException e) { resultado = new
		 * EntityResultWrong(ErrorMessage.REQUIRED_FIELD); } catch
		 * (InvalidFieldsValuesException e) { resultado = new
		 * EntityResultWrong(ErrorMessage.REQUIRED_FIELD); } catch (Exception e) {
		 * System.err.println(e.getMessage()); resultado = new
		 * EntityResultWrong(ErrorMessage.DELETE_ERROR); }
		 */
		return resultado;
	}

}
