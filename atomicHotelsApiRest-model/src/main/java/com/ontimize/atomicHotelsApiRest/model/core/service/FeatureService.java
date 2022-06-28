package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;

@Service("FeatureService")
@Lazy
public class FeatureService implements IFeatureService{
	
	@Autowired
	private FeatureDao featureDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult featureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.featureDao, keyMap, attrList);
		// System.out.println("keyMap:" + keyMap.toString()); // TODO eliminar
		// System.out.println("attrList:" + attrList.toString());// TODO eliminar
		resultado.setMessage("mensaje cambiado"); // TODO eliminar
		return resultado;
	}

	@Override
	public EntityResult featureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();

		// OPCION A (comprobando si el registro ya existe)
//		if (attrMap.containsKey(FeatureDao.ATTR_NAME)) {
//			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
//			List<String> auxAttrList = new ArrayList<String>();
//			auxKeyMap.put(FeatureDao.ATTR_NAME, attrMap.get(FeatureDao.ATTR_NAME));
//			auxAttrList.add(FeatureDao.ATTR_NAME);
//			EntityResult auxEntity = featureQuery(auxKeyMap, auxAttrList);
//			// System.out.println("coincidencias:" + auxEntity.calculateRecordNumber());//
//			// TODO eliminar
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = this.daoHelper.insert(this.featureDao, attrMap);
//			} else {
//				resultado.setCode(EntityResult.OPERATION_WRONG);
//				resultado.setMessage("Error al crear Feature - El registro ya existe");
//			}
//		}

		// TODO limpiar pruebas de setMessage

		 //OPCION B (capturando excepción duplicateKey)
//		try {
//			resultado = this.daoHelper.insert(this.featureDao, attrMap);
//			if (resultado != null && resultado.getCode() == EntityResult.OPERATION_WRONG) {
//				resultado.setMessage("Error al insertar datos");
//			} else {
//				resultado.setMessage("mensaje cambiado2 desde insert");
//			}
//		} catch (DuplicateKeyException e) {
//			resultado.setCode(EntityResult.OPERATION_WRONG);
//			resultado.setMessage("Error al crear Feature - El registro ya existe");
//		}
		
		// OPCION C (comprobando si el registro ya existe)
		if (attrMap.containsKey(FeatureDao.ATTR_NAME)) {
			EntityResult auxEntity = this.daoHelper.query(this.featureDao,
					EntityResultTools.keysvalues(featureDao.ATTR_NAME, attrMap.get(featureDao.ATTR_NAME)),
					EntityResultTools.attributes(FeatureDao.ATTR_NAME));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = this.daoHelper.insert(this.featureDao, attrMap);
			} else {
				resultado.setCode(EntityResult.OPERATION_WRONG);
				resultado.setMessage("Error al crear Feature - El registro ya existe");
			}
		}
		return resultado;
	}

	@Override
	public EntityResult featureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.featureDao, attrMap, keyMap);
	}

	@Override
	public EntityResult featureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.featureDao, keyMap);
	}

}
