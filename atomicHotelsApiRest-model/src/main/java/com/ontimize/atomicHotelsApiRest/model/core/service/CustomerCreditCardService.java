package com.ontimize.atomicHotelsApiRest.model.core.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerCreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("CustomerCreditCardService")
@Lazy
public class CustomerCreditCardService implements ICustomerCreditCardService{
	
	@Autowired
	private CustomerCreditCardDao customerCreditCardDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	ControlFields cf;
	
	@Autowired
	CustomerService customerService;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult customerCreditCardQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado=new EntityResultWrong();
		try {
		cf.reset();
		
		cf.setCPUserColum(CustomerDao.ATTR_USER);			
		cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_CUSTOMER);
		
		cf.addBasics(CustomerCreditCardDao.fields);
		cf.addBasics(CreditCardDao.fields);
		cf.validate(keyMap);
		cf.validate(attrList);
		return this.daoHelper.query(this.customerCreditCardDao, keyMap, attrList);
		}catch( ValidateException e) {
			resultado = e.getEntityResult();
		}catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}
	

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult customerCreditCardInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException { 
		
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(CustomerCreditCardDao.fields);
			List<String> required=new ArrayList<>() {
				{
					add(CustomerCreditCardDao.ATTR_CRD_ID);
					add(CustomerCreditCardDao.ATTR_CST_ID);
				}
				};
			cf.setRequired(required);
			cf.validate(attrMap);
			
			
			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(CustomerDao.ATTR_ID, attrMap.get(CustomerCreditCardDao.ATTR_CST_ID));
				}
			};

			EntityResult auxEntity = customerService.customerQuery(subConsultaKeyMap,
					EntityResultTools.attributes(CustomerDao.ATTR_USER)); // aqui se restringen por permisos
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros, la habitación es erronea.
				throw new EntityResultRequiredException(ErrorMessage.NO_CUSTOMER_ID);
			}			
			
			resultado = this.daoHelper.insert(this.customerCreditCardDao, attrMap);	
			resultado.setMessage("Tarjeta añadida a cliente");

			
			
		}catch (DuplicateKeyException e) {
			resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		}catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(e.getMessage());		
		}catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;		
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult customerCreditCardDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(CustomerCreditCardDao.fields);
			List<String> required=new ArrayList<>() {
				{
					add(CustomerCreditCardDao.ATTR_CRD_ID);
					add(CustomerCreditCardDao.ATTR_CST_ID);
				}
				};
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);
			
			Map<String,Object> consultaKeyMap=new HashMap<>()
			{
				{
				put( CustomerCreditCardDao.ATTR_CRD_ID,keyMap.get(CustomerCreditCardDao.ATTR_CRD_ID));
				put( CustomerCreditCardDao.ATTR_CST_ID,keyMap.get(CustomerCreditCardDao.ATTR_CST_ID));
				}
			};
			
			EntityResult auxEntity= customerCreditCardQuery(consultaKeyMap,
					EntityResultTools.attributes(CustomerCreditCardDao.ATTR_CRD_ID,
					CustomerCreditCardDao.ATTR_CST_ID));
		
			if(auxEntity.calculateRecordNumber()==0) {
			resultado=new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
		}else {
				resultado=this.daoHelper.delete(this.customerCreditCardDao, keyMap);
				resultado.setMessage("Asociacion de tarjeta y cliente borrada");			
		}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DataIntegrityViolationException e) {	
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		}catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

}