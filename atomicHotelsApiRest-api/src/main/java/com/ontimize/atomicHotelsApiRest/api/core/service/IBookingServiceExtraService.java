package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IBookingServiceExtraService {
	
	 public EntityResult bookingServiceExtraQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult bookingServiceExtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	// public EntityResult bookingServiceExtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult bookingServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 //public EntityResult bookingExtraServicePriceUnitsTotalQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	// public EntityResult extraServicesNameDescriptionUnitsPriceDateQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
}
