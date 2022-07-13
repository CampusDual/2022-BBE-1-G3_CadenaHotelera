package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IReceiptService {
	
	 public EntityResult recepitQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult recepitInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult recepitUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult recepitDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
