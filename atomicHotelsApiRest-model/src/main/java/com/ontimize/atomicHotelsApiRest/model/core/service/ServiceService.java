package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


import com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServiceDao;

@Service("ServiceService")
@Lazy
public class ServiceService implements IServiceService{
	
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult serviceQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.serviceDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult serviceInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();

		if (attrMap.containsKey(serviceDao.ATTR_NAME)) {
			EntityResult auxEntity = this.daoHelper.query(this.serviceDao,
					EntityResultTools.keysvalues(serviceDao.ATTR_NAME, attrMap.get(serviceDao.ATTR_NAME)),
					EntityResultTools.attributes(serviceDao.ATTR_NAME));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = this.daoHelper.insert(this.serviceDao, attrMap);
			} else {
				resultado.setCode(EntityResult.OPERATION_WRONG);
				resultado.setMessage("Error al crear Feature - El registro ya existe");
			}
		}
		return resultado;
	}

	@Override
	public EntityResult serviceUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.serviceDao, attrMap, keyMap);
	}

	@Override
	public EntityResult serviceDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.serviceDao, keyMap);
	}

}