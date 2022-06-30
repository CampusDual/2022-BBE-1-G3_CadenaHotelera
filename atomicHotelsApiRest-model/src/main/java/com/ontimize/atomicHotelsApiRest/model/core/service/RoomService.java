package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;

@Service("RoomService")
@Lazy
public class RoomService implements IRoomService{
	
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.roomDao, keyMap, attrList);
		return resultado;
	}

	@Override//TODO cambiar la comprobación de clave duplicada por las uniques
	public EntityResult roomInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.insert(this.roomDao, attrMap);
			if (resultado != null && resultado.getCode() == EntityResult.OPERATION_WRONG) {
				resultado.setMessage("Error al insertar datos");
			} else {
				resultado.setMessage("Inserción realizada");
			}
		} catch (DuplicateKeyException e) {
			resultado.setCode(EntityResult.OPERATION_WRONG);
			resultado.setMessage("Error al crear Room - El registro ya existe");
		}

		return resultado;
	}

	@Override//TODO cambiar la comprobación de clave duplicada por las uniques y tratar las otras excepciones de la manera adecuada.
	public EntityResult roomUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.update(this.roomDao, attrMap, keyMap);
			if (resultado != null && resultado.getCode() == EntityResult.OPERATION_WRONG) {
				resultado.setMessage("Error al actualizar datos");
			} else {
				resultado.setMessage("Actualización realizada");
			}
		} catch (DuplicateKeyException e) {
			resultado.setCode(EntityResult.OPERATION_WRONG);
			resultado.setMessage("Error al actualizar Room - No se puede duplicar un registro");
		}catch(org.springframework.dao.DataIntegrityViolationException e) {
			resultado.setCode(EntityResult.OPERATION_WRONG);
			resultado.setMessage("Error al actualizar Room - Prentende asignarle una FK que no existe");
		}catch(org.springframework.jdbc.SQLWarningException e) {
			resultado.setCode(EntityResult.OPERATION_WRONG);
			resultado.setMessage("Error al actualizar Room - Debe introducir el id del hotel y el número de la habitación");
		}
		return resultado;
	}

	@Override
	public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado=this.daoHelper.delete(this.roomDao, keyMap);
		}catch(org.springframework.jdbc.SQLWarningException e) {
			resultado.setCode(EntityResult.OPERATION_WRONG);
			resultado.setMessage("Error al eliminar Room - Debe introducir el id del hotel y el número de la habitación");
		}
		return resultado;
	}
	
	public EntityResult roomDataQuery(Map<String, Object> keysValues, List<String> attrList) {
		 EntityResult queryRes = this.daoHelper.query(this.roomDao, keysValues, 
				 EntityResultTools.attributes(RoomDao.ATTR_HOTEL_ID, RoomDao.ATTR_NUMBER,RoomDao.ATTR_SQUARE_METERS,RoomDao.ATTR_STATUS,RoomDao.ATTR_ROOM_TYPE_ID,HotelDao.ATTR_NAME,RoomTypeDao.ATTR_NAME,RoomTypeDao.ATTR_PRICE),"queryRooms");
   return queryRes;
	}
	


}

