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
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServiceDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("HotelServiceService")
@Lazy
public class HotelServiceService implements IHotelServiceService {

	@Autowired
	private HotelServiceDao hotelserviceDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult hotelServiceInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			ControlFields cf = new ControlFields();
			cf.addBasics(HotelServiceDao.fields);
			cf.addBasics(HotelDao.fields);
			cf.addBasics(ServiceDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.hotelserviceDao, keyMap, attrList, "queryhotelservice");
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult hotelServiceQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			ControlFields cf = new ControlFields();
			cf.addBasics(HotelServiceDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.hotelserviceDao, keyMap, attrList);
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult hotelServiceInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			ControlFields cf = new ControlFields();
			List<String> required = new ArrayList<String>() {
				{
					add(HotelServiceDao.ATTR_ID_HTL);
					add(HotelServiceDao.ATTR_ID_SRV);
				}
			};
			cf.addBasics(HotelServiceDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.hotelserviceDao, attrMap);
			resultado.setMessage("HotelService registrado");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
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
	public EntityResult hotelServiceDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			
			List<String> required = new ArrayList<String>() {
				{
					add(HotelServiceDao.ATTR_ID_HTL);
					add(HotelServiceDao.ATTR_ID_SRV);
				}
			};
			ControlFields cf = new ControlFields();
			cf.addBasics(HotelServiceDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(HotelServiceDao.ATTR_ID_HTL, keyMap.get(HotelServiceDao.ATTR_ID_HTL));
					put(HotelServiceDao.ATTR_ID_SRV, keyMap.get(HotelServiceDao.ATTR_ID_SRV));
				}
			};

			EntityResult auxEntity = hotelServiceQuery(consultaKeyMap, EntityResultTools.attributes(HotelServiceDao.ATTR_ID_HTL,HotelServiceDao.ATTR_ID_SRV));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.hotelserviceDao, keyMap);
				resultado.setMessage("HotelService eliminado");
			}
		
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

}