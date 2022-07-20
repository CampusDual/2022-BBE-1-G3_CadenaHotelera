package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CreditCardDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

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
			 
			ValidateFields.required(attrMap, CreditCardDao.ATTR_NUMBER, CreditCardDao.ATTR_DATE_EXPIRY);
			ValidateFields.NegativeNotAllowed(((Number)( attrMap.get(CreditCardDao.ATTR_NUMBER))).longValue());
			ValidateFields.invalidCreditCard(((Number)(attrMap.get(CreditCardDao.ATTR_NUMBER))).longValue());
			
			ValidateFields.validDateExpiry((String) attrMap.get(CreditCardDao.ATTR_DATE_EXPIRY));
			resultado = this.daoHelper.insert(this.creditCardDao, attrMap);	
			resultado.setMessage("Tarjeta registrada");

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);		
		}catch (NumberFormatException e) {
			resultado =new EntityResultWrong(ErrorMessage.INVALID_NUMBER_CREDITCARD);
		}catch (InvalidFieldsValuesException e) {
				resultado =new EntityResultWrong(ErrorMessage.DATA_EXPIRY_BEFORE_TODAY);
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
			ValidateFields.required(keyMap,CreditCardDao.ATTR_ID);
			EntityResult auxEntity = this.daoHelper.query(this.creditCardDao,
					EntityResultTools.keysvalues(CreditCardDao.ATTR_ID, keyMap.get(CreditCardDao.ATTR_ID)),
					EntityResultTools.attributes(CreditCardDao.ATTR_ID));
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