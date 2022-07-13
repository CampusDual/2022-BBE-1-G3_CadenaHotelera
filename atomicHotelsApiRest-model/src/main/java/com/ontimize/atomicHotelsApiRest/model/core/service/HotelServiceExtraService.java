package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttachmentRef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;

@Service("HotelServiceExtraService")
@Lazy
public class HotelServiceExtraService implements IHotelServiceExtraService {

	@Autowired
	private HotelServiceExtraDao hotelServiceExtraDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	public EntityResult hotelServiceExtraQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.query(this.hotelServiceExtraDao, keyMap, attrList);
	}

	@Override
	public EntityResult hotelServiceExtraInsert(Map<String, Object> attrMap)
			throws OntimizeJEERuntimeException, MissingFieldsException {

		EntityResult resultado = new EntityResultMapImpl();
		try {

			ValidateFields.required(attrMap, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT,
					HotelServiceExtraDao.ATTR_PRECIO);
			resultado = this.daoHelper.insert(this.hotelServiceExtraDao, attrMap);
			resultado.setMessage("HotelSeriveExtra registrada");

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}

		return resultado;
	}


	@Override
	public EntityResult hotelServiceExtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, HotelServiceExtraDao.ATTR_ID_SXT, HotelServiceExtraDao.ATTR_ID_HTL);
			resultado = this.daoHelper.update(this.hotelServiceExtraDao, attrMap, keyMap);
			System.err.println(resultado);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Service Extra actualizado");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FK);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult hotelServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT);
			EntityResult auxEntity = this.daoHelper.query(this.hotelServiceExtraDao,
					EntityResultTools.keysvalues(HotelServiceExtraDao.ATTR_ID_HTL,
							keyMap.get(HotelServiceExtraDao.ATTR_ID_HTL), HotelServiceExtraDao.ATTR_ID_SXT,
							keyMap.get(HotelServiceExtraDao.ATTR_ID_SXT)),
					EntityResultTools.attributes(HotelServiceExtraDao.ATTR_ID_HTL, HotelServiceExtraDao.ATTR_ID_SXT));
			System.err.println(auxEntity.entrySet());
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.hotelServiceExtraDao, keyMap);
				resultado.setMessage("Service Extra eliminado");
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
