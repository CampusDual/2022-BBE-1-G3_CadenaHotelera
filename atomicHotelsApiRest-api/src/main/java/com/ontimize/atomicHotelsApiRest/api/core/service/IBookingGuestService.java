package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IBookingGuestService {
	
	 public EntityResult bookingGuestQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult bookingGuestInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult bookingGuestDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 //public EntityResult guestCountQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult bookingGuestsInfoQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

}
