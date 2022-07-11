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

import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;

@Service("ServicesXtraService")
@Lazy
public class ServicesXtraService implements IServicesXtraService{
	
	@Autowired
	private ServicesXtraDao servicesXtraDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult servicesXtraQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.servicesXtraDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult servicesXtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.insert(this.servicesXtraDao, attrMap);
			resultado.setMessage("Servicio extra registrado correctamente.");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al crear el servicio extra - El servicio ya existe.");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al crear el servicio extra - Falta algún campo obligatorio.");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al registrar el servicio extra.");
		}

		return resultado;
	}

	@Override
	public EntityResult servicesXtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.update(this.servicesXtraDao, attrMap, keyMap);
			resultado.setMessage("Servicio extra actualizada");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al actualizar el servicio extra - No es posible duplicar un registro.");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al actualizar el servicio extra - Falta algún campo obligatorio.");
		} catch (SQLWarningException e) { 
			e.printStackTrace();
			resultado = new EntityResultWrong(
					"Error al actualizar el servicio extra - Falta el sxt_id (PK) del servicio extra a actualizar.");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al actualizar el servicio extra.");
		}
		return resultado; 
	}

	@Override
	public EntityResult servicesXtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();		
		try {
			if (keyMap.containsKey(ServicesXtraDao.ATTR_ID)) {
				EntityResult auxEntity = this.daoHelper.query(this.servicesXtraDao,
						EntityResultTools.keysvalues(ServicesXtraDao.ATTR_ID, keyMap.get(ServicesXtraDao.ATTR_ID)),
						EntityResultTools.attributes(ServicesXtraDao.ATTR_ID));
				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
					resultado = new EntityResultWrong("Error al eliminar el servicio extra - El servicio extra a eliminar no existe");
				} else {
					resultado = this.daoHelper.delete(this.servicesXtraDao, keyMap);
					resultado.setMessage("Servicio extra eliminado");
				}
			}else {
				resultado = new EntityResultWrong("Error al eliminar el servicio extra - Falta el sxt_id (PK) del servicio extra a eliminar");
			}
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al eliminar el servicio extra");
		}
		return resultado;
	}

}
