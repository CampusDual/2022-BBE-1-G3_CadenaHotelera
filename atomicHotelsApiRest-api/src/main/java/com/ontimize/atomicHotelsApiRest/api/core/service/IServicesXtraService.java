package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IServicesXtraService {
	
	public EntityResult servicesXtraQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult servicesXtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	public EntityResult servicesXtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult servicesXtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	
}
