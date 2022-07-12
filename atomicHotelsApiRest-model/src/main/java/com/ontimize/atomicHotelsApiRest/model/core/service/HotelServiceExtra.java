package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;

@Service("HotelServiceExtra")
@Lazy
public class HotelServiceExtra implements IHotelServiceExtraService {

	@Autowired
	private HotelServiceExtraDao hotelServiceExtraDao;
	
	@Autowired 
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Override
	public EntityResult hotelServiceExtraHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public EntityResult hotelServiceExtraQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.query(this.hotelServiceExtraDao, keyMap, attrList);
	}
	
	@Override
	public EntityResult hotelServiceExtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException, MissingFieldsException {
		
		EntityResult resultado = new EntityResultMapImpl();
		ValidateFields.required(attrMap, HotelDao.ATTR_ID, ServicesXtraDao.ATTR_ID);
		try {
				resultado.setMessage("Servicio registrado");
				return this.daoHelper.insert(hotelServiceExtraDao, attrMap);
		}catch (DuplicateKeyException e) {
			return new EntityResultWrong("El registro ya existe");
		}catch (DataIntegrityViolationException e) {
			return new EntityResultWrong("Clave foranea erronea");
		}
	}
	@Override
	public EntityResult hotelServiceExtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public EntityResult hotelServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
