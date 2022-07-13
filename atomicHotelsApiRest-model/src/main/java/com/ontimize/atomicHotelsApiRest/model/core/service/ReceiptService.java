package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class ReceiptService implements IReceiptService{
	
	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;


	@Override
	public EntityResult recepitQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityResult recepitInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(attrMap, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET, HotelDao.ATTR_CITY,
					HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY);			
			//ValidateFields.emptyFields(attrMap);			
			resultado = this.daoHelper.insert(this.hotelDao, attrMap);
			resultado.setMessage("Hotel registrado");

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);

		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult recepitUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, HotelDao.ATTR_ID);
			resultado = this.daoHelper.update(this.hotelDao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Hotel actualizado");
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
	public EntityResult recepitDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, HotelDao.ATTR_ID);

			EntityResult auxEntity = this.daoHelper.query(this.hotelDao,
					EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)),
					EntityResultTools.attributes(HotelDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.hotelDao, keyMap);
				resultado.setMessage("Hotel eliminado");
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
