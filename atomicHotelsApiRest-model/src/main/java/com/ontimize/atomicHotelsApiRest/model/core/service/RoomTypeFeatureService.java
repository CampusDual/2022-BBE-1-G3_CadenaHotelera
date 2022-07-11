package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;

@Service("RoomTypeFeatureService")
@Lazy
public class RoomTypeFeatureService implements IRoomTypeFeatureService{
	
	@Autowired
	private RoomTypeFeatureDao roomTypeFeatureDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Override
	public EntityResult roomTypeFeatureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		  return this.daoHelper.query(this.roomTypeFeatureDao, keyMap, attrList);		
	}
	@Override
	public EntityResult roomTypeFeatureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.insert(this.roomTypeFeatureDao, attrMap);			
		}catch (DuplicateKeyException e) {
			return new EntityResultWrong("El registro ya existe");
		}catch (DataIntegrityViolationException e) {
			return new EntityResultWrong("Clave foranea erronea");
		}
		
	}
	@Override
	public EntityResult roomTypeFeatureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		  return this.daoHelper.update(this.roomTypeFeatureDao, attrMap, keyMap);
	}
	@Override
	public EntityResult roomTypeFeatureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.roomTypeFeatureDao, keyMap); 
	}

}
