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
import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("RoomTypeFeatureService")
@Lazy
public class RoomTypeFeatureService implements IRoomTypeFeatureService{
	
	@Autowired
	private RoomTypeFeatureDao roomTypeFeatureDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Override
	public EntityResult roomTypeFeatureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		  return this.daoHelper.query(this.roomTypeFeatureDao, keyMap, attrList);		
	}
	@Override
	public EntityResult roomTypeFeatureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			
			ValidateFields.required(attrMap, RoomTypeFeatureDao.ATTR_FEATURE_ID, RoomTypeFeatureDao.ATTR_ROOM_ID);	
			resultado = this.daoHelper.insert(this.roomTypeFeatureDao, attrMap);	
			resultado.setMessage("RoomTypeFeature registrada");

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FK);		
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		
		return resultado;
	}
	
	
	@Override
	public EntityResult roomTypeFeatureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, RoomTypeFeatureDao.ATTR_FEATURE_ID,RoomTypeFeatureDao.ATTR_ROOM_ID);

			EntityResult auxEntity = this.daoHelper.query(this.roomTypeFeatureDao,
					EntityResultTools.keysvalues(RoomTypeFeatureDao.ATTR_FEATURE_ID, keyMap.get(RoomTypeFeatureDao.ATTR_FEATURE_ID),RoomTypeFeatureDao.ATTR_ROOM_ID, keyMap.get(RoomTypeFeatureDao.ATTR_ROOM_ID)),
					EntityResultTools.attributes(RoomTypeFeatureDao.ATTR_FEATURE_ID,RoomTypeFeatureDao.ATTR_ROOM_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.roomTypeFeatureDao, keyMap);
				resultado.setMessage("RoomTypeFeature eliminada");
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
