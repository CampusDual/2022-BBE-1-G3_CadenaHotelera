package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IRoomTypeFeatureService {
	 public EntityResult roomTypeFeatureQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult roomTypeFeatureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult roomTypeFeatureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
