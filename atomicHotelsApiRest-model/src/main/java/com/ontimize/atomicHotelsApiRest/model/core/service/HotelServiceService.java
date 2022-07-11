package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceDao;

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
		return this.daoHelper.insert(this.hotelserviceDao, attrMap);
	}

	@Override
	public EntityResult hotelServiceUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.hotelserviceDao, attrMap, keyMap);
	}

	@Override
	public EntityResult hotelServiceDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.hotelserviceDao, keyMap);
	}

}