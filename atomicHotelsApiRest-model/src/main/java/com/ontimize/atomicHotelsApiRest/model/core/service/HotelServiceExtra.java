package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceExtra;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public class HotelServiceExtra implements IHotelServiceExtra {

	@Override
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
