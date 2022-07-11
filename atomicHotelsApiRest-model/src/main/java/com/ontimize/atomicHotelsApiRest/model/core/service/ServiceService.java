package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;


import com.ontimize.atomicHotelsApiRest.api.core.service.IServiceService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServiceDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;

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
		try {
			resultado = this.daoHelper.insert(this.serviceDao, attrMap);
			resultado.setMessage("Service registrado");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al crear Service - El registro ya existe");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al crear Service - Falta algún campo obligatorio");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al registrar Service");
		}

		return resultado;

	}

	@Override
	public EntityResult serviceUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.update(this.serviceDao, attrMap, keyMap);
			resultado.setMessage("Service actualizado");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al actualizar Service - No es posible duplicar un registro");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al actualizar Serive - Falta algún campo obligatorio");
		} catch (SQLWarningException e) {
			resultado = new EntityResultWrong(
					"Error al actualizar Servuce - Falta el srv_id (PK) del Service a actualizar");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al actualizar Service");
		}
		return resultado;
	}

	@Override
	public EntityResult serviceDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();		
		try {
			if (keyMap.containsKey(ServiceDao.ATTR_ID)) {
				EntityResult auxEntity = this.daoHelper.query(this.serviceDao,
						EntityResultTools.keysvalues(ServiceDao.ATTR_ID, keyMap.get(ServiceDao.ATTR_ID)),
						EntityResultTools.attributes(ServiceDao.ATTR_ID));
				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
					resultado = new EntityResultWrong("Error al eliminar Serivice - La Feature a eliminar no existe");
				} else {
					resultado = this.daoHelper.delete(this.serviceDao, keyMap);
					resultado.setMessage("Service eliminado");
				}
			}else {
				resultado = new EntityResultWrong("Error al eliminar Service - Falta el srv_id (PK) del Service a eliminar");
			}
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al eliminar Service");
		}
		return resultado;
	}

}