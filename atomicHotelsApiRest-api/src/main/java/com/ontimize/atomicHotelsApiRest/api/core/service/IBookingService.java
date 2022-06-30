package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IBookingService {
	
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult bookingsInRangeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

}
