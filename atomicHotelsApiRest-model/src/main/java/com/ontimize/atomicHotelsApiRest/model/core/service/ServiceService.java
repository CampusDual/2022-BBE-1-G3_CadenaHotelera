package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServiceDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("ServiceService")
@Lazy
public class ServiceService implements IServiceService{
	
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult serviceQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado=new EntityResultWrong();
		try {
		ControlFields filterAndColumns=new ControlFields();
		filterAndColumns.addBasics(ServiceDao.fields);
		filterAndColumns.validate(keyMap);//Validamos el filtro
		filterAndColumns.validate(attrList);//Validamos las columnas
		}catch(ValidateException e) {
			e.getMessage();
			resultado=new EntityResultWrong(ErrorMessage.ERROR);
		}catch(Exception e) {
			e.getStackTrace();
			resultado=new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
		}
	
	
	@Override
	public EntityResult serviceInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ControlFields controllerData=new ControlFields();
			controllerData.addBasics(ServiceDao.fields);
			List<String> required=new ArrayList<>() {
				{
				
				add(ServiceDao.ATTR_NAME);
			}
			};
			controllerData.setRequired(required);
			List<String> restricted=new ArrayList<>(){
				{
				add(ServiceDao.ATTR_ID);
				}
				};
			controllerData.setRestricted(restricted);
			controllerData.validate(attrMap);
			resultado = this.daoHelper.insert(this.serviceDao, attrMap);
			resultado.setMessage("Service registrado");

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
	public EntityResult serviceUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, ServiceDao.ATTR_ID);
			resultado = this.daoHelper.update(this.serviceDao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
					resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
					resultado.setMessage("Service actualizado");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult serviceDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, ServiceDao.ATTR_ID);

			EntityResult auxEntity = this.daoHelper.query(this.serviceDao,
					EntityResultTools.keysvalues(ServiceDao.ATTR_ID, keyMap.get(ServiceDao.ATTR_ID)),
					EntityResultTools.attributes(ServiceDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.serviceDao, keyMap);
				resultado.setMessage("Service eliminado");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

}