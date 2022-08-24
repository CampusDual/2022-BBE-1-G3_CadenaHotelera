package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IQuestionService {
	public EntityResult questionPublicQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult questionQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult questionInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

	public EntityResult questionUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

	public EntityResult questionDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
