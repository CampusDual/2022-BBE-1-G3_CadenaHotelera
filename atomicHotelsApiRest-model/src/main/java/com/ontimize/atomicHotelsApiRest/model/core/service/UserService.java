package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.mockito.ArgumentMatchers.contains;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerResponse.Context;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IUserService;

import com.ontimize.atomicHotelsApiRest.model.core.dao.UserDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Lazy
@Service("UserService")
public class UserService implements IUserService {

	@Autowired
	private UserDao dao;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	public void loginQuery(Map<?, ?> key, List<?> attr) {
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult userQuery(Map<String, Object> keyMap, List<String> attrList) {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(dao.fields);

			cf.setRestricted(new ArrayList<String>() {
				{
					add(dao.ATTR_PASSWORD);
				}
			});
			cf.setControlPermissionsActive(false);
			cf.validate(keyMap);
			cf.validate(attrList);
//			System.err.println(keyMap);
//			System.err.println(attrList);
			resultado = this.daoHelper.query(this.dao, keyMap, attrList);

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult userInsert(Map<String, Object> attrMap) {
		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_USER);
					add(dao.ATTR_PASSWORD);
				}
			};

			cf.reset();
			cf.addBasics(dao.fields);
			cf.setControlPermissionsActive(false);// para que con user_ no se sobre escriba consigo mismo
			cf.setRequired(required);
			cf.validate(attrMap);

//			System.out.println(attrMap);
//			System.out.println(cf.toString());
//			
			resultado = this.daoHelper.insert(this.dao, attrMap);
			resultado.setMessage("Usuario registrado");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult userUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) {
		EntityResult resultado = new EntityResultWrong();

		try {

			// ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {
				{
					add(dao.ATTR_USER);
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.setControlPermissionsActive(false);// para que con user_ no se sobre escriba consigo mismo

			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			List<String> restrictedData = new ArrayList<String>() {
				{
					add(dao.ATTR_USER);// El id no se puede actualizar
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRestricted(restrictedData);
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_USER, keyMap.get(dao.ATTR_USER));
				}
			};
			EntityResult auxEntity = userQuery(subConsultaKeyMap, EntityResultTools.attributes(dao.ATTR_USER));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {

				resultado = this.daoHelper.update(this.dao, attrMap, keyMap);

				if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
					resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
				} else {
					resultado = new EntityResultMapImpl();
					resultado.setMessage("Usuario actualizado");
				}
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult userDelete(Map<String, Object> keyMap) {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_USER);
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setControlPermissionsActive(false);// para que con user_ no se sobre escriba consigo mismo

			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_USER, keyMap.get(dao.ATTR_USER));
				}
			};

			EntityResult auxEntity = userQuery(subConsultaKeyMap, EntityResultTools.attributes(dao.ATTR_USER));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				Map<String, Object> subKeyMapDelete = new HashMap<>() {
					{
						put(UserRoleDao.ATTR_USER, keyMap.get(dao.ATTR_USER));
					}
				};
				userRoleService.userRoleDelete(subKeyMapDelete);
				resultado = this.daoHelper.delete(this.dao, keyMap);
				resultado.setMessage("Usuario y roles asignados eliminados");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult userCancelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setControlPermissionsActive(false);// para que con user_ no se sobre escriba consigo mismo
			cf.setRequired(new ArrayList<>() {
				{
					add(dao.ATTR_USER);
				}
			});
			cf.setOptional(false);
			cf.validate(keyMap);

			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(new ArrayList<>() {
				{
					add(dao.NON_ATTR_ACTION);
				}
			});
			cf.setOptional(false);
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_USER, keyMap.get(dao.ATTR_USER));
				}
			};

			EntityResult auxEntity = userQuery(subConsultaKeyMap, EntityResultTools.attributes(dao.ATTR_USER));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);

			} else {

				Map<String, Object> finalAttrMap = new HashMap<>() {
					{
						put(dao.ATTR_BLOCKED, new Date());
					}
				};
				resultado = this.daoHelper.update(this.dao, finalAttrMap, keyMap);

				if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
					resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
				} else {
					resultado = new EntityResultMapImpl();
					resultado.setMessage("User canceled");
				}

			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}
}
