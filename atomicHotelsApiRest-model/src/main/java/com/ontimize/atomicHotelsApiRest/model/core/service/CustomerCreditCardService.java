package com.ontimize.atomicHotelsApiRest.model.core.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerCreditCardDao;
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

	@Override
	public EntityResult customerCreditCardQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.customerCreditCardDao, keyMap, attrList);
	}
	

	@Override
	public EntityResult customerCreditCardInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException { 
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			
			ValidateFields.required(attrMap, CustomerCreditCardDao.ATTR_CST_ID, CustomerCreditCardDao.ATTR_CST_ID);	
			resultado = this.daoHelper.insert(this.customerCreditCardDao, attrMap);	
			resultado.setMessage("Tarjeta añadida a cliente");

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);		
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		
		return resultado;

	}

	@Override
	public EntityResult customerCreditCardDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap,CustomerCreditCardDao.ATTR_CRD);

			EntityResult auxEntity = this.daoHelper.query(this.customerCreditCardDao,
					EntityResultTools.keysvalues(this.customerCreditCardDao.ATTR_CST_ID, keyMap.get(CustomerCreditCardDao.ATTR_CST_ID)),
					EntityResultTools.attributes(CustomerCreditCardDao.ATTR_CST_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.customerCreditCardDao, keyMap);
				resultado.setMessage("Tarjeta de cliente eliminada");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

}