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

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerCreditCardDao;
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

	@Override
	public EntityResult customerCreditCardQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado=new EntityResultWrong();
		try {
		ControlFields controllerFilterandColumns=new ControlFields();	
		controllerFilterandColumns.addBasics(CustomerCreditCardDao.fields);
		controllerFilterandColumns.validate(keyMap);
		controllerFilterandColumns.validate(attrList);
		return this.daoHelper.query(this.customerCreditCardDao, keyMap, attrList);
		}catch( ValidateException e) {
			e.getMessage();
			resultado=new EntityResultWrong(e.getMessage());
		}catch(Exception e) {
			e.getStackTrace();
			resultado=new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}
	

	@Override
	public EntityResult customerCreditCardInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException { 
		
		EntityResult resultado = new EntityResultWrong();
		try {
			ControlFields controllerData=new ControlFields();
			controllerData.addBasics(CustomerCreditCardDao.fields);
			List<String> required=new ArrayList<>() {
				{
					add(CustomerCreditCardDao.ATTR_CRD_ID);
					add(CustomerCreditCardDao.ATTR_CST_ID);
				}
				};
			controllerData.setRequired(required);
			controllerData.validate(attrMap);
			resultado = this.daoHelper.insert(this.customerCreditCardDao, attrMap);	
			resultado.setMessage("Tarjeta añadida a cliente");

		}catch (DuplicateKeyException e) {
			resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		}catch (ValidateException e) {
			e.getStackTrace();
			resultado =new EntityResultWrong(e.getMessage());
		}
		catch(Exception e) {
			resultado=new EntityResultWrong(ErrorMessage.ERROR);
			e.printStackTrace();
		}
		return resultado;		
	}

	@Override
	public EntityResult customerCreditCardDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			ControlFields ControllerFilter=new ControlFields();
			ControllerFilter.addBasics(CustomerCreditCardDao.fields);
			List<String> required=new ArrayList<>() {
				{
					add(CustomerCreditCardDao.ATTR_CRD_ID);
					add(CustomerCreditCardDao.ATTR_CST_ID);
				}
				};
			ControllerFilter.setRequired(required);
			ControllerFilter.setOptional(false);
			ControllerFilter.validate(keyMap);
			
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
			e.getStackTrace();
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			e.getStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

}