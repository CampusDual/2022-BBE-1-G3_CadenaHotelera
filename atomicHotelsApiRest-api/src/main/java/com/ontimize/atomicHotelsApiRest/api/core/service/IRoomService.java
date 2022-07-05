package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IRoomService {
	public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult roomInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult roomUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult roomsUnbookedInRangeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public boolean isRoomUnbookedgInRangeQuery(String startDate, String endDate, Integer roomId) throws OntimizeJEERuntimeException, EntityResultRequiredException;
	EntityResult roomInfoQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

}
