package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("BedComboService")
@Lazy
public class BedComboService implements IBedComboService{
	
	@Autowired
	private BedComboDao bedComboDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override 
	public EntityResult bedComboQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bedComboDao, keyMap, attrList);
	}
	 
	@Override
	public EntityResult bedComboInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(attrMap,BedComboDao.ATTR_NAME,BedComboDao.ATTR_SLOTS);
			ValidateFields.NegativeNotAllowed((int) attrMap.get(BedComboDao.ATTR_SLOTS));
			resultado=this.daoHelper.insert(this.bedComboDao, attrMap);
			resultado.setMessage("Tipo de cama insertado");
		} catch (MissingFieldsException e) {
			resultado= new EntityResultWrong(ErrorMessage.CREATION_ERROR+e.getMessage());
			
		}catch (DuplicateKeyException e) {
			resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}
		catch (NumberFormatException e) {
			resultado =new EntityResultWrong(ErrorMessage.NEGATIVE_OR_CERO_NOT_ALLOWED);
		}
		catch(Exception e) {
			resultado=new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}
		return resultado;		
	}

	@Override
	public EntityResult bedComboUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado=new EntityResultMapImpl();
		try {
			ValidateFields.required(attrMap, BedComboDao.ATTR_ID);
			ValidateFields.NegativeNotAllowed((int) keyMap.get(BedComboDao.ATTR_SLOTS));
			resultado=this.daoHelper.update(bedComboDao, attrMap, keyMap);
			if(resultado.getCode()==EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado=new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD); 
			}else {
				resultado.setMessage("Tipo de cama actualizado");
			} 
			}catch(MissingFieldsException e){
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR+e.getMessage());
			}catch(DuplicateKeyException e){
				e.printStackTrace();
				resultado=new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
			}catch( DataIntegrityViolationException e){
				e.printStackTrace();
				resultado=new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
			}catch (NumberFormatException e) {
				resultado =new EntityResultWrong(ErrorMessage.NEGATIVE_OR_CERO_NOT_ALLOWED);
			}catch(Exception e){
				e.printStackTrace();
				resultado=new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
			}
			return resultado;	
		}


	@Override
	public EntityResult bedComboDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado=new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap,BedComboDao.ATTR_ID);
			
			EntityResult auxEntity= this.daoHelper.query(this.bedComboDao,
					EntityResultTools.keysvalues(BedComboDao.ATTR_ID,keyMap.get(BedComboDao.ATTR_ID)),
					EntityResultTools.attributes(BedComboDao.ATTR_ID));
		if(auxEntity.calculateRecordNumber()==0) {
			resultado=new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
		}else {
				resultado=this.daoHelper.delete(this.bedComboDao, keyMap);
				resultado.setMessage("Tipo de cama borrada");
				
		}
		}catch (MissingFieldsException e) {
			resultado=new EntityResultWrong(ErrorMessage.DELETE_ERROR+e.getMessage());
		}catch(DataIntegrityViolationException e) {
			resultado=new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		}catch(Exception e) {
			resultado=new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}
//	public EntityResult bedDataQuery(Map<String, Object> keysValues, List<String> attrList) {
//		EntityResult queryRes = this.daoHelper.query(this.bedComboDao,
//				EntityResultTools.keysvalues("bdc_id", keysValues.get("bdc_id")),
//				EntityResultTools.attributes(BedComboDao.ATTR_ID, BedComboDao.ATTR_NAME, BedComboDao.ATTR_SLOTS),
//				"queryHotel");
//		return queryRes;
//	}
	
	

}