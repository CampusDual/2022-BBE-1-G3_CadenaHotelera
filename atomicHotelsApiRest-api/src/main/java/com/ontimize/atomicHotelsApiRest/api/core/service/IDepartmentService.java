package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IDepartmentService {
	
	public EntityResult departmentQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult departmentInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	public EntityResult departmentUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult departmentDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	
}
