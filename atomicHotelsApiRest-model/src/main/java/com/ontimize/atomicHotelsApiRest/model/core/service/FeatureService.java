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
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("FeatureService")
@Lazy
public class FeatureService implements IFeatureService {

	@Autowired
	private FeatureDao featureDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult featureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultWrong();
		try {

			ControlFields cf = new ControlFields();
			cf.addBasics(FeatureDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.featureDao, keyMap, attrList);
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult featureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {
			List<String> required = new ArrayList<String>() {{
				add(FeatureDao.ATTR_NAME);
			}};
			ControlFields cf = new ControlFields();
			cf.addBasics(FeatureDao.fields);
			cf.setRequired(required);
			cf.validate(attrMap);


			resultado = this.daoHelper.insert(this.featureDao, attrMap);

			resultado.setMessage("Feature registrada");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());

		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);

		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}

		return resultado;
	}

	@Override
	public EntityResult featureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			
			//ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {{
				add(FeatureDao.ATTR_ID);
			}};	
			ControlFields cf = new ControlFields();		
			cf.addBasics(FeatureDao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);//No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);	
			
			
			
			//ControlFields de los nuevos datos
			List<String> restrictedData = new ArrayList<String>() {{
				add(FeatureDao.ATTR_ID);//El id no se puede actualizar
			}};
			ControlFields cd = new ControlFields();
			cd.addBasics(FeatureDao.fields);
			cd.setRestricted(restrictedData);
			cd.validate(attrMap);
			
			resultado = this.daoHelper.update(this.featureDao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Feature actualizada");
			}
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult featureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			
			List<String> required = new ArrayList<String>() {{
				add(FeatureDao.ATTR_ID);
			}};
			ControlFields cf = new ControlFields();
			cf.addBasics(FeatureDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			
			Map<String, Object> consultaKeyMap = new HashMap<>() { {
				put(FeatureDao.ATTR_ID, keyMap.get(FeatureDao.ATTR_ID));
				}
			};
			
			EntityResult auxEntity = featureQuery(consultaKeyMap, 
					EntityResultTools.attributes(FeatureDao.ATTR_ID));
			
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.featureDao, keyMap);
				resultado.setMessage("Feature eliminada");
			}
			
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

}
