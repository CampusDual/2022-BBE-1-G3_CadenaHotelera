package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;

@Service("BedComboService")
@Lazy
public class BedComboService implements IBedComboService{
	
	@Autowired
	private BedComboDao bedComboDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult bedComboQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
	
		return this.daoHelper.query(this.bedComboDao, keyMap, attrList);
	}

	@Override
	public EntityResult bedComboInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException { 
	
		try {
			ValidateFields.required(attrMap,BedComboDao.ATTR_NAME,BedComboDao.ATTR_SLOTS);
			return this.daoHelper.insert(this.bedComboDao, attrMap);
		} catch (MissingFieldsException e) {
			return new EntityResultWrong(e.getMessage());
			
		}
		
//		
//		System.err.println(attrMap);
//		if(attrMap.containsKey(BedComboDao.ATTR_NAME) &&attrMap.get(BedComboDao.ATTR_NAME)!="") {
//			return this.daoHelper.insert(this.bedComboDao, attrMap);
//		}else {
//			return new EntityResultWrong("falta porner el nombre anormal , no vela nulo sub");
//		}
		
		
		
	}

	@Override
	public EntityResult bedComboUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.bedComboDao, attrMap, keyMap);
	}

	@Override
	public EntityResult bedComboDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.bedComboDao, keyMap);
	}

}