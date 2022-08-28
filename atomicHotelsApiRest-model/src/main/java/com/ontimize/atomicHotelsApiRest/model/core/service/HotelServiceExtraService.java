package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelServiceExtraService")
@Lazy
public class HotelServiceExtraService implements IHotelServiceExtraService {

	@Autowired
	private HotelServiceExtraDao dao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelServiceExtraQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();

			cf.setCPHtlColum(dao.ATTR_ID_HTL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.addBasics(dao.fields,ServicesXtraDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.dao, keyMap, attrList);

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
	public EntityResult hotelServiceExtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_ID_HTL);
					add(dao.ATTR_ID_SXT);
					add(dao.ATTR_PRECIO);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};

			cf.addBasics(dao.fields);

			cf.setCPHtlColum(dao.ATTR_ID_HTL);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.dao, attrMap);
			resultado.setMessage("HotelServiceExtra registrado");

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
	public EntityResult hotelServiceExtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			// ControlFields del filtro
			List<String> requiredFilter = Arrays.asList(dao.ATTR_ID);
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			List<String> restrictedData = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);// El id no se puede actualizar
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRestricted(restrictedData);
//			cf.setOptional(true); //No es necesario ponerlo
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					putAll(keyMap);
				}
			};			
			EntityResult auxEntity = hotelServiceExtraQuery(subConsultaKeyMap,
					EntityResultTools.attributes(dao.ATTR_ID)); // aquí validamos la resctricción por permisos
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.update(this.dao, attrMap, keyMap);

				if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
					resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
				} else {
					resultado.setMessage("Service Extra actualizado");
				}
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {// Puede ser que se meta una FK que no exista o se le ponga null al
			// precio cuando no se debería permitir
			resultado = new EntityResultWrong(
					ErrorMessage.UPDATE_ERROR_MISSING_FK + " / " + ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_ID, keyMap.get(dao.ATTR_ID));
				}
			};

			EntityResult auxEntity = hotelServiceExtraQuery(consultaKeyMap, EntityResultTools.attributes(dao.ATTR_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.dao, keyMap);
				resultado.setMessage("HotelServiceExtra eliminado");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

}
