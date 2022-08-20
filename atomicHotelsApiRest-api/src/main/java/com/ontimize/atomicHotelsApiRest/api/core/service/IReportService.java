package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IReportService {
	
//	public EntityResult reportPruebaQuery(Map<String, Object> keyMap, List<String> attrList)
//			throws OntimizeJEERuntimeException;
	public ResponseEntity test(Map<String, Object> filter, List<String> columns) throws OntimizeJEERuntimeException;
	public ResponseEntity testChar(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

}
