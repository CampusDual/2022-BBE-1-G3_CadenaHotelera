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
import com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;

import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
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
		EntityResult resultado=new EntityResultMapImpl();
		try {
		ControlFields controllerFilterandColumns =new ControlFields();
		controllerFilterandColumns.addBasics(BedComboDao.fields);
		controllerFilterandColumns.validate(keyMap);
		controllerFilterandColumns.validate(attrList);
		return this.daoHelper.query(this.bedComboDao, keyMap, attrList);
		}catch(MissingFieldsException|RestrictedFieldException|InvalidFieldsException|InvalidFieldsValuesException|LiadaPardaException e) {
			e.getMessage();
			resultado=new EntityResultWrong(e.getMessage());
		}catch(Exception e) {
			e.getStackTrace();
			resultado=new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}
	 
	@Override
	public EntityResult bedComboInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {			
			ControlFields controllerData=new ControlFields();
			controllerData.addBasics(BedComboDao.fields);
			List<String> required=new ArrayList<>() {
				{
					add(BedComboDao.ATTR_NAME);
					add(BedComboDao.ATTR_SLOTS);
				}
			};
			controllerData.setRequired(required);
			List<String> restricted=new ArrayList<>() {
				{
				add(BedComboDao.ATTR_ID);
				}
				};
				controllerData.setRestricted(restricted);
			controllerData.validate(attrMap);
			
			resultado=this.daoHelper.insert(this.bedComboDao, attrMap);
			resultado.setMessage("Tipo de cama insertado");

		}catch (DuplicateKeyException e) {
			resultado =new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		}catch (RestrictedFieldException |MissingFieldsException |InvalidFieldsValuesException | InvalidFieldsException |LiadaPardaException e) {
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
	public EntityResult bedComboUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
			EntityResult resultado=new EntityResultMapImpl();
		try {
			ControlFields controllerFilter =new ControlFields();
			controllerFilter.addBasics(BedComboDao.fields);
			List<String> required=new ArrayList<>() {
				{
				add(BedComboDao.ATTR_ID);	
				}
				};
			controllerFilter.setRequired(required);
			controllerFilter.setOptional(false);
			controllerFilter.validate(keyMap);
			
			
			ControlFields controllerData=new ControlFields();
			controllerData.addBasics(BedComboDao.fields);
			List<String> restricted=new ArrayList<>() {
				{
				add(BedComboDao.ATTR_ID);	
				}
				};
			controllerData.setRestricted(restricted);
			controllerData.validate(attrMap);			
			resultado=this.daoHelper.update(bedComboDao, attrMap, keyMap);
			if(resultado.getCode()==EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado=new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD); 
			}else {
				resultado.setMessage("Tipo de cama actualizado");
			}
			//MissingFieldsException, RestrictedFieldException,
			//InvalidFieldsException, InvalidFieldsValuesException, LiadaPardaException
			}catch(MissingFieldsException|RestrictedFieldException|InvalidFieldsException| InvalidFieldsValuesException| LiadaPardaException e){
				e.printStackTrace();
				resultado = new EntityResultWrong(e.getMessage());
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
			ControlFields ControllerFilter=new ControlFields();
			ControllerFilter.addBasics(BedComboDao.fields);
			List<String> required=new ArrayList<>() {
				{
					add(BedComboDao.ATTR_ID);
				}
				};
			ControllerFilter.setRequired(required);
			ControllerFilter.setOptional(false);
			ControllerFilter.validate(keyMap);
			
			EntityResult auxEntity= this.daoHelper.query(this.bedComboDao,
					EntityResultTools.keysvalues(BedComboDao.ATTR_ID,keyMap.get(BedComboDao.ATTR_ID)),
					EntityResultTools.attributes(BedComboDao.ATTR_ID));
		if(auxEntity.calculateRecordNumber()==0) {
			resultado=new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
		}else {
				resultado=this.daoHelper.delete(this.bedComboDao, keyMap);
				resultado.setMessage("Tipo de cama borrada");
				
		}

		} catch (MissingFieldsException | RestrictedFieldException | InvalidFieldsException | InvalidFieldsValuesException | LiadaPardaException e) {
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