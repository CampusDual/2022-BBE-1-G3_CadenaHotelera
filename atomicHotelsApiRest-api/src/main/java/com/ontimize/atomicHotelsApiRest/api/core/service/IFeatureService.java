package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IFeatureService {
	public EntityResult featureQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult featureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult featureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult featureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
