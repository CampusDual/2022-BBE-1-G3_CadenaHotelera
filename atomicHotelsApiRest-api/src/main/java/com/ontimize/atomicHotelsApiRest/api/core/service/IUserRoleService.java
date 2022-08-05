package com.ontimize.atomicHotelsApiRest.api.core.service;


import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;


public interface IUserRoleService {
	 public EntityResult userRoleQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult userRoleInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
//	 public EntityResult userRoleUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult userRoleDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
