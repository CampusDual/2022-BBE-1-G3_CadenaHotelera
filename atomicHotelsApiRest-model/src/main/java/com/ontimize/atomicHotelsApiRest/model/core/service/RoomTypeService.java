package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("RoomTypeService")
@Lazy
public class RoomTypeService implements IRoomTypeService {

	@Autowired
	private RoomTypeDao roomTypeDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult roomTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.roomTypeDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult roomTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();

		try {

			ValidateFields.required(attrMap, RoomTypeDao.ATTR_NAME, RoomTypeDao.ATTR_PRICE, RoomTypeDao.ATTR_BEDS_COMBO);
		
			resultado = this.daoHelper.insert(this.roomTypeDao, attrMap);

			resultado.setMessage("RoomType registrada");

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());	
			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		}catch(DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		
		return resultado;
		
	}

	@Override
	public EntityResult roomTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, RoomTypeDao.ATTR_ID);
			resultado = this.daoHelper.update(this.roomTypeDao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("RoomType actualizada");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {//Pendiente controlar mejor (Hacer query de clave foránea para controlar eso por otro lado??)
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FK+" / "+ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
		}
		return resultado; 
	}

	@Override
	public EntityResult roomTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, RoomTypeDao.ATTR_ID);

			EntityResult auxEntity = this.daoHelper.query(this.roomTypeDao,
					EntityResultTools.keysvalues(RoomTypeDao.ATTR_ID, keyMap.get(RoomTypeDao.ATTR_ID)),
					EntityResultTools.attributes(RoomTypeDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.roomTypeDao, keyMap);
				resultado.setMessage("RoomType eliminado");
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
	
	@Override
	public EntityResult infoQuery(Map<String, Object> keyMap, List<String> attrList) {
		return this.daoHelper.query(this.roomTypeDao, keyMap, attrList, "queryRoomTypes");
	}

}