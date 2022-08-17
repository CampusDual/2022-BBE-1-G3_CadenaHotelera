package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface ICustomerService {
	
	public EntityResult customerQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
//	public EntityResult customerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
//	public EntityResult customerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult customerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult mailAgreementQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
//	public EntityResult mailAgreementBasicExpressionQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
//	public EntityResult checkCountryQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult businessCustomerInsert (Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	public EntityResult regularCustomerInsert (Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	EntityResult customerCancelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;
	//public boolean isCustomerValidBookingHolder(Object customerId) throws OntimizeJEERuntimeException,  EntityResultRequiredException;
	EntityResult customerBusinessUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;
	EntityResult customerRegularUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

}
