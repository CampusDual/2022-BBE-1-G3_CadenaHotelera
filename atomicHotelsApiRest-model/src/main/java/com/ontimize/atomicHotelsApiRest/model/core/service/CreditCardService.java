package com.ontimize.atomicHotelsApiRest.model.core.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
@Service("CreditCardService")
@Lazy
public class CreditCardService implements ICreditCardService{
	
	@Autowired
	private CreditCardDao creditCardDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult creditCardQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.creditCardDao, keyMap, attrList);
	}
	

	@Override
	public EntityResult creditCardInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException { 
		
		EntityResult resultado = new EntityResultMapImpl();
		try { 
			ControlFields controller=new ControlFields();
			controller.addBasics(CreditCardDao.fields);
			List<String> required=new ArrayList<>() {
			{
				add(CreditCardDao.ATTR_NUMBER);
				add(CreditCardDao.ATTR_DATE_EXPIRY);
			}
			};
			controller.setRequired(required);
			
			List<String> restricted=new ArrayList(){
				{
				add(CreditCardDao.ATTR_ID);
			}
				};
			controller.setRestricted(restricted);
			//controller.setOptional(false);yo digo que salta sin esto estefania dicq que no tengo idea
			controller.validate(attrMap);
			
			resultado = this.daoHelper.insert(this.creditCardDao, attrMap);	
			resultado.setMessage("Tarjeta registrada");
			
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);		
		}catch (InvalidFieldsValuesException e) {
				resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
		}catch (InvalidFieldsException e) {
			resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());		
		}catch (RestrictedFieldException e) {
			resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
		}catch(LiadaPardaException e) {
			resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR+ e.getMessage());
		}catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		e.printStackTrace();
		}
		 
		return resultado;

	}

	@Override
	public EntityResult creditCardDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ControlFields controller=new ControlFields();
			List<String> required=new ArrayList<>() {
			{
				add(CreditCardDao.ATTR_ID);
			}
			};
			controller.setRequired(required);
			ValidateFields.required(keyMap,CreditCardDao.ATTR_ID);
			Map<String,Object> consulta=new HashMap<>(){
				{
				put(CreditCardDao.ATTR_ID, keyMap.get(CreditCardDao.ATTR_ID));
				}
				};
				
				
			EntityResult auxEntity = this.creditCardQuery(consulta,EntityResultTools.attributes(CreditCardDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.creditCardDao, keyMap);
				resultado.setMessage("Tarjeta eliminada");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		}catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

}