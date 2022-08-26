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

import com.amadeus.referenceData.locations.Hotel;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IAnswerService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.AnswerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.QuestionDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("AnswerService")
@Lazy
public class AnswerService implements IAnswerService {

	@Autowired
	private AnswerDao dao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult answerQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			cf.addBasics(dao.fields);

			cf.setCPHtlColum(QuestionDao.ATTR_HTL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

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
	public EntityResult answerPublicQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRestricted(new ArrayList() {
				{
					add(dao.ATTR_PUBLIC);
					add(dao.ATTR_USER);
				}
			});
			cf.setCPHtlColum(QuestionDao.ATTR_HTL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);
			cf.validate(keyMap);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.dao, keyMap, attrList, "queryPublic");
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
	public EntityResult answerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {

			cf.reset();
			cf.addBasics(dao.fields, QuestionDao.fields);
			cf.setRequired(new ArrayList<String>() {
				{
					add(dao.ATTR_ANSWER);
					add(dao.ATTR_QUESTION_ID);
				}
			});
			cf.setRestricted(new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
					add(dao.ATTR_USER);
				}
			});

			cf.setCPUserColum(dao.ATTR_USER);
			cf.addCPUser(true);
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>();
			subConsultaKeyMap.put(QuestionDao.ATTR_ID, attrMap.get(dao.ATTR_QUESTION_ID));
			EntityResult auxEntity = questionService.questionQuery(subConsultaKeyMap,
					EntityResultTools.attributes(QuestionDao.ATTR_ID, QuestionDao.ATTR_HTL_ID)); // aqui se restringen
																									// por permisos
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros, la habitación es erronea.
				throw new EntityResultRequiredException(ErrorMessage.INVALID_QUESTION_ID);
			}

//			if (attrMap.containsKey(dao.ATTR_CUSTOMER_ID)) {
//				Map<String, Object> subConsultaKeyMap = new HashMap<>();
//				cf.reset(); 
//				cf.setCPUserColum(CustomerDao.ATTR_USER);			
//				cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_CUSTOMER);
//				cf.validate(subConsultaKeyMap);
//				subConsultaKeyMap.put(CustomerDao.ATTR_ID, attrMap.get(dao.ATTR_CUSTOMER_ID));
//				EntityResult auxEntity = daoHelper.query(this.dao,subConsultaKeyMap,
//						EntityResultTools.attributes(HotelDao.ATTR_ID),"queryCustomerGuestsHotel"); // aqui se restringen por permisos
//				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros, la habitación es erronea.
//					throw new EntityResultRequiredException(ErrorMessage.INVALID_CUSTOMER_ID);
//				}
//			}

			resultado = this.daoHelper.insert(this.dao, attrMap);
			resultado.setMessage("Answer registrada");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(e.getMessage());
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
	public EntityResult answerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			// ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {
				{
					add(dao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(requiredFilter);

			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);

			// ControlFields de los nuevos datos

			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRestricted(new ArrayList<String>() {
				{
					add(dao.ATTR_USER);
					add(dao.ATTR_ID);
				}
			});
			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>();
			subConsultaKeyMap.putAll(keyMap);
			EntityResult auxEntity = answerQuery(subConsultaKeyMap, EntityResultTools.attributes(dao.ATTR_ID)); // aqui
																												// se
																												// restringen
																												// por
																												// permisos
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros, la habitación es erronea.
				throw new EntityResultRequiredException(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			}

//			if (attrMap.containsKey(dao.ATTR_HTL_ID)) {
//				subConsultaKeyMap = new HashMap<>();
//				subConsultaKeyMap.put(HotelDao.ATTR_ID, attrMap.get(dao.ATTR_HTL_ID));
//
//				auxEntity = hotelService.hotelQuery(subConsultaKeyMap,
//						EntityResultTools.attributes(HotelDao.ATTR_ID)); // aqui se restringen por permisos
//				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros, la habitación es erronea.
//					throw new EntityResultRequiredException(ErrorMessage.INVALID_HOTEL_ID);
//				}
//			}

			resultado = this.daoHelper.update(this.dao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Answer actualizada");
			}
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(e.getMessage());

		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult answerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
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

			EntityResult auxEntity = answerQuery(consultaKeyMap, EntityResultTools.attributes(dao.ATTR_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.dao, keyMap);
				resultado.setMessage("Answer eliminada");
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

}
