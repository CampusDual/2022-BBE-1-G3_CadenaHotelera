package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.mockito.ArgumentMatchers.contains;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Lazy
@Service("UserService")
public class UserService implements IUserService {

	@Autowired
	private UserDao userDao;
	
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
			cf.addBasics(UserDao.fields);
			
			cf.setRestricted(new ArrayList<String>() {
				{
					add(UserDao.ATTR_PASSWORD);
				}
			});

			cf.validate(keyMap);
			cf.validate(attrList);				
			resultado = this.daoHelper.query(this.userDao, keyMap, attrList);

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());
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
					add(UserDao.ATTR_USER);
					add(UserDao.ATTR_PASSWORD);
				}
			};

			cf.reset();
			cf.addBasics(UserDao.fields);
			cf.setRequired(required);
			cf.validate(attrMap);
			
			System.out.println(attrMap);
			System.out.println(cf.toString());
			
			resultado = this.daoHelper.insert(this.userDao, attrMap);
			resultado.setMessage("Usuario registrado");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
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
					add(UserDao.ATTR_USER);
				}
			};
			cf.reset();
			cf.addBasics(UserDao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			List<String> restrictedData = new ArrayList<String>() {
				{
					add(UserDao.ATTR_USER);// El id no se puede actualizar
				}
			};
			cf.reset();
			cf.addBasics(UserDao.fields);
			cf.setRestricted(restrictedData);
			cf.validate(attrMap);

			resultado = this.daoHelper.update(this.userDao, attrMap, keyMap);

			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado = new EntityResultMapImpl();
				resultado.setMessage("Usuario actualizado");
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + " - " + e.getMessage());
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
					add(UserDao.ATTR_USER);
				}
			};
			cf.reset();
			cf.addBasics(UserDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(UserDao.ATTR_USER, keyMap.get(UserDao.ATTR_USER));
				}
			};

			EntityResult auxEntity = userQuery(subConsultaKeyMap, EntityResultTools.attributes(UserDao.ATTR_USER));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				Map<String, Object> subKeyMapDelete = new HashMap<>(){{put(UserRoleDao.ATTR_USER,keyMap.get(UserDao.ATTR_USER));}};
				userRoleService.userRoleDelete(subKeyMapDelete);
				resultado = this.daoHelper.delete(this.userDao, keyMap);
				resultado.setMessage("Usuario eliminado");
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

}
