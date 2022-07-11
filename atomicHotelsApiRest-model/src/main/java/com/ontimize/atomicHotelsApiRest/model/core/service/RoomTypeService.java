package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("RoomTypeService")
@Lazy
public class RoomTypeService implements IRoomTypeService {

	@Autowired
	private RoomTypeDao roomTypeDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult roomTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.roomTypeDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult roomTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		
		try {
			if (attrMap.containsKey(RoomTypeDao.ATTR_NAME) && attrMap.containsKey(RoomTypeDao.ATTR_PRICE)&& attrMap.containsKey(RoomTypeDao.ATTR_BEDS_COMBO)) {
			resultado = this.daoHelper.insert(this.roomTypeDao, attrMap);
			resultado.setMessage("RoomType registrada");
			}else {
				resultado = new EntityResultWrong("Error al crear RoomType - Faltan campos obligatorios.");
			}
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al crear RoomType - El registro ya existe");
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong("Error al crear RoomType - No existe la referencia a la tabla beds_combo (FK (rmt_bdc_id))");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al registrar RoomType");
		}

		return resultado;
		
	}

	@Override
	public EntityResult roomTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.update(this.roomTypeDao, attrMap, keyMap);
			resultado.setMessage("RoomType actualizada");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al actualizar RoomType - No es posible duplicar un registro");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al actualizar RoomType - Falta algún campo obligatorio");
		} catch (SQLWarningException e) {
			resultado = new EntityResultWrong(
					"Error al actualizar RoomType - Falta el rmt_id (PK) de la RoomType a actualizar");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al actualizar RoomType");
		}
		return resultado; 
		
	}

	@Override
	public EntityResult roomTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();		
		try {
			if (keyMap.containsKey(RoomTypeDao.ATTR_ID)) {
				EntityResult auxEntity = this.daoHelper.query(this.roomTypeDao,
						EntityResultTools.keysvalues(RoomTypeDao.ATTR_ID, keyMap.get(RoomTypeDao.ATTR_ID)),
						EntityResultTools.attributes(RoomTypeDao.ATTR_ID));
				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
					resultado = new EntityResultWrong("Error al eliminar RoomType - La RoomType a eliminar no existe");
				} else {
					resultado =  this.daoHelper.delete(this.roomTypeDao, keyMap);
					resultado.setMessage("RoomType eliminada");
				}
			}else {
				resultado = new EntityResultWrong("Error al eliminar RoomType - Falta el rmt_id (PK) de la RoomType a eliminar");
			}
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al eliminar RoomType");
		}
		return resultado;
		
	}
	
	@Override
	public EntityResult infoQuery(Map<String, Object> keyMap, List<String> attrList) {
		return this.daoHelper.query(this.roomTypeDao, keyMap, attrList, "queryRoomTypes");
	}

}