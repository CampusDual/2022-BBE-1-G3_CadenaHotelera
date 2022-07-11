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
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;

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
		return resultado;
	}

	@Override
	public EntityResult featureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.insert(this.featureDao, attrMap);
			resultado.setMessage("Feature registrada");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al crear Feature - El registro ya existe");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al crear Feature - Falta algún campo obligatorio");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al registrar Feature");
		}

		return resultado;
	}

	@Override
	public EntityResult featureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			
			resultado = this.daoHelper.update(this.featureDao, attrMap, keyMap);
			if(resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong("Error al actualizar Feature - El regsitro que pretende actualizar no existe.");		
			}else {
				resultado.setMessage("Feature actualizada");
			}			
			
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al actualizar Feature - No es posible duplicar un registro");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al actualizar Feature - Falta algún campo obligatorio");
		} catch (SQLWarningException e) {
			resultado = new EntityResultWrong("Error al actualizar Feature - "+e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al actualizar Feature");
		}
		return resultado; 
	}

	@Override
	public EntityResult featureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();		
		try {
			if (keyMap.containsKey(FeatureDao.ATTR_ID)) {
				EntityResult auxEntity = this.daoHelper.query(this.featureDao,
						EntityResultTools.keysvalues(FeatureDao.ATTR_ID, keyMap.get(FeatureDao.ATTR_ID)),
						EntityResultTools.attributes(FeatureDao.ATTR_ID));
				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
					resultado = new EntityResultWrong("Error al eliminar Feature - La Feature a eliminar no existe");
				} else {
					resultado = this.daoHelper.delete(this.featureDao, keyMap);
					resultado.setMessage("Feature eliminada");
				}
			}else {
				resultado = new EntityResultWrong("Error al eliminar Feature - Falta el ftr_id (PK) de la Feature a eliminar");
			}
		}catch(DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al eliminar Feature - Está referenciada en alguna otra tabla (FK)");
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong("Error al eliminar Feature");
		}
		return resultado;
	}

}
