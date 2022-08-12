package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IRoomTypeService {
	 public EntityResult roomTypeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult roomTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult roomTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult roomTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult infoQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
//	 public EntityResult infoRoomFeaturesQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 
}
