package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface ICustomerCreditCardService {
	 public EntityResult customerCreditCardQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult customerCreditCardInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult customerCreditCardDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
