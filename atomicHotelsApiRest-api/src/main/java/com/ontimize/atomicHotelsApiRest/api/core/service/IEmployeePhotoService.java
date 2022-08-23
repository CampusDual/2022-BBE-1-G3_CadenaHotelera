package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IEmployeePhotoService {

	 public EntityResult employeePhotoQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult employeePhotoInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult employeePhotoDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public ResponseEntity getPicture(Map<String, Object> filter, List<String> columns) throws OntimizeJEERuntimeException;

}
