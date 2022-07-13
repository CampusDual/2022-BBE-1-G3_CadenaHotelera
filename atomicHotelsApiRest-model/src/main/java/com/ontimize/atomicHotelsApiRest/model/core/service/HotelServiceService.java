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
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("HotelServiceService")
@Lazy
public class HotelServiceService implements IHotelServiceService{
	
	@Autowired
	private HotelServiceDao hotelserviceDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult hotelServiceQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.hotelserviceDao, keyMap, attrList);
	}

	@Override
	public EntityResult hotelServiceInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException { 
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			
			ValidateFields.required(attrMap, HotelServiceDao.ATTR_ID_HTL, HotelServiceDao.ATTR_ID_SRV);	
			resultado = this.daoHelper.insert(this.hotelserviceDao, attrMap);	
			resultado.setMessage("HotelService registrado");

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
	public EntityResult hotelServiceDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, RoomTypeFeatureDao.ATTR_FEATURE_ID,RoomTypeFeatureDao.ATTR_ROOM_ID);

			EntityResult auxEntity = this.daoHelper.query(this.hotelserviceDao,
					EntityResultTools.keysvalues(HotelServiceDao.ATTR_ID_HTL, keyMap.get(HotelServiceDao.ATTR_ID_HTL),HotelServiceDao.ATTR_ID_SRV, keyMap.get(HotelServiceDao.ATTR_ID_SRV)),
					EntityResultTools.attributes(HotelServiceDao.ATTR_ID_HTL,HotelServiceDao.ATTR_ID_SRV));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.hotelserviceDao, keyMap);
				resultado.setMessage("HotelService eliminado");
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