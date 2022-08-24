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


import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
@Service("CreditCardService")
@Lazy
public class CreditCardService implements ICreditCardService{
	
	@Autowired
	private CreditCardDao creditCardDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	ControlFields cf;

	@Override 
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult creditCardQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado=new EntityResultWrong();
		try {
		cf.reset();
		
		cf.setCPUserColum(CustomerDao.ATTR_USER);			
		cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_CUSTOMER);
		
		cf.addBasics(CreditCardDao.fields);
		cf.validate(keyMap);
		cf.validate(attrList);
		resultado =  this.daoHelper.query(this.creditCardDao, keyMap, attrList);
		}catch(ValidateException e) {		
			resultado = e.getEntityResult();
		}catch(Exception e) {
			e.printStackTrace();
			resultado=new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	} 
	
	 
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult creditCardInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException { 
		
		EntityResult resultado = new EntityResultWrong();
		try { 
			cf.reset();
			cf.addBasics(CreditCardDao.fields);
			List<String> required=new ArrayList<>() {
			{
				add(CreditCardDao.ATTR_NUMBER);
				add(CreditCardDao.ATTR_DATE_EXPIRY);
			}
			};
			cf.setRequired(required);
			
			List<String> restricted=new ArrayList(){
				{
				add(CreditCardDao.ATTR_ID);
			}
				};
			cf.setRestricted(restricted);
			//controller.setOptional(false);yo digo que salta sin esto estefania dicq que no tengo idea
			cf.validate(attrMap);
			
			resultado = this.daoHelper.insert(this.creditCardDao, attrMap);	
			resultado.setMessage("Tarjeta registrada");
			
		}catch (DuplicateKeyException e) {
			resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		}catch (ValidateException e) {
			resultado = e.getEntityResult();
		}
		catch(Exception e) {
			e.printStackTrace();
			resultado=new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}
	
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult creditCardDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado=new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(creditCardDao.fields);
			List<String> required=new ArrayList<>() {
				{
					add(CreditCardDao.ATTR_ID);
				}
				};
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);
			
			Map<String,Object> consultaKeyMap=new HashMap<>()
			{
				{
				put( CreditCardDao.ATTR_ID,keyMap.get(CreditCardDao.ATTR_ID));	
				}
			};
			
			EntityResult auxEntity=creditCardQuery(consultaKeyMap,EntityResultTools.attributes(CreditCardDao.ATTR_ID));
		
			if(auxEntity.calculateRecordNumber()==0) {
			resultado=new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
		}else {
				resultado=this.daoHelper.delete(this.creditCardDao, keyMap);
				resultado.setMessage("Tarjeta borrada");
				
		}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}
}


				
