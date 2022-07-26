package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IReceiptService {
	
	 public EntityResult receiptQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult receiptInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
//	 public EntityResult receiptUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult receiptDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult completeReceiptQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
}
