package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface ICreditCardService {
	 public EntityResult creditCardQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult creditCardInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult creditCardDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
