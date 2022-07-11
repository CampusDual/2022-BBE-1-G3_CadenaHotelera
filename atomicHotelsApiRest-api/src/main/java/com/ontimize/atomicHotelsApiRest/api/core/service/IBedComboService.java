package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IBedComboService {
	 public EntityResult bedComboQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult bedComboInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult bedComboUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult bedComboDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
