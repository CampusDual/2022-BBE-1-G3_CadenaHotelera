package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IHotelServiceService {
	 public EntityResult hotelServiceQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult hotelServiceInfoQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult hotelServiceInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult hotelServiceDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	

}
