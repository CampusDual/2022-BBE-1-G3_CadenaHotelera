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
import com.ontimize.atomicHotelsApiRest.api.core.service.IUserRoleService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IUserService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao.UserRole;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao.Action;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Lazy
@Service("UserRoleService")
public class UserRoleService implements IUserRoleService {

	@Autowired
	private UserRoleDao dao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	public void loginQuery(Map<?, ?> key, List<?> attr) {
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult userRoleQuery(Map<String, Object> keyMap, List<String> attrList) {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setNoEmptyList(false);
			cf.setControlPermissionsActive(false);
			cf.validate(keyMap);
			cf.validate(attrList);
			
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
	public EntityResult userRoleInsert(Map<String, Object> attrMap) {
		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_USER);
//					add(dao.ATTR_ID_ROLENAME);
					add(dao.NON_ATTR_ROLE);
				}
			};

			cf.reset();
			cf.addBasics(dao.fields);
			cf.setControlPermissionsActive(false);//para que con user_ no se sobre escriba consigo mismo
			cf.setRequired(required);
			cf.validate(attrMap);

//			System.out.println(attrMap);
//			System.out.println(cf.toString());
//			UserRoleDao.UserRole role = (UserRole) attrMap.get(dao.NON_ATTR_ACTION);

			switch((UserRoleDao.UserRole )attrMap.get(dao.NON_ATTR_ROLE)){
			case CEO:
				attrMap.put(dao.ATTR_ID_ROLENAME, 1);
				break;
			case HOTEL_MANAGER:
				attrMap.put(dao.ATTR_ID_ROLENAME, 2);
				break;
			case STAFF:
				attrMap.put(dao.ATTR_ID_ROLENAME, 3);
				break;
			case CUSTOMER:
				attrMap.put(dao.ATTR_ID_ROLENAME, 4);
				break;
			case USER:
				attrMap.put(dao.ATTR_ID_ROLENAME, 5);
				break;
			}
			attrMap.remove(dao.NON_ATTR_ROLE);
			resultado = this.daoHelper.insert(this.dao, attrMap);
			resultado.setMessage("Rol de Usuario, registrado");

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
	public EntityResult userRoleDelete(Map<String, Object> keyMap) {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(dao.ATTR_USER);					
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setControlPermissionsActive(false);//para que con user_ no se sobre escriba consigo mismo

			cf.setOptional(false);
			cf.validate(keyMap);

//			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
//				{
//					put(dao.ATTR_USER, keyMap.get(dao.ATTR_USER));
//				}
//			};

//			EntityResult auxEntity = userQuery(subConsultaKeyMap, EntityResultTools.attributes(dao.ATTR_USER));

//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
//			} else {
				resultado = this.daoHelper.delete(this.dao, keyMap);
				resultado.setMessage("Roles de Usuario eliminados");
//			}

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

}
