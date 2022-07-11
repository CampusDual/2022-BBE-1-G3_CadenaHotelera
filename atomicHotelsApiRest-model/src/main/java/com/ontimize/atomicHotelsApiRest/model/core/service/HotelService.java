package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgresql.xml.EmptyStringEntityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.hotelDao, keyMap, attrList);
		// System.out.println("keyMap:" + keyMap.toString()); // TODO eliminar
		// System.out.println("attrList:" + attrList.toString());// TODO eliminar
//		resultado.setMessage("mensaje cambiado"); // TODO eliminar
		return resultado;
	}

	@Override
	public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();

		try {
			resultado = this.daoHelper.insert(this.hotelDao, attrMap);
			resultado.setMessage("Hotel registrado");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al crear Hotel - El registro ya existe");
		
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al crear Hotel - Falta algún campo obligatorio");
		
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al registrar Hotel");
		}

		// OPCION A (comprobando si el registro ya existe)
//		if (attrMap.containsKey(HotelDao.ATTR_NAME)) {
//			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
//			List<String> auxAttrList = new ArrayList<String>();
//			auxKeyMap.put(HotelDao.ATTR_NAME, attrMap.get(HotelDao.ATTR_NAME));
//			auxAttrList.add(HotelDao.ATTR_NAME);
//			EntityResult auxEntity = hotelQuery(auxKeyMap, auxAttrList);
//			// System.out.println("coincidencias:" + auxEntity.calculateRecordNumber());//
//			// TODO eliminar
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			} else {
//				resultado.setCode(EntityResult.OPERATION_WRONG);
//				resultado.setMessage("Error al crear Hotel - El registro ya existe");
//			}
//		}

		// OPCION B (capturando excepción duplicateKey)
//		try {
//			resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			if (resultado != null && resultado.getCode() == EntityResult.OPERATION_WRONG) {
//				resultado.setMessage("Error al insertar datos");
//			} else {
//				resultado.setMessage("mensaje cambiado2 desde insert");
//			}
//		} catch (DuplicateKeyException e) {
//			resultado.setCode(EntityResult.OPERATION_WRONG);
//			resultado.setMessage("Error al crear Hotel - El registro ya existe");
//		}

		// TODO limpiar pruebas de setMessage

//		// OPCION C (comprobando si el registro ya existe)
//		if (attrMap.containsKey(HotelDao.ATTR_NAME)) {
//			EntityResult auxEntity = this.daoHelper.query(this.hotelDao,
//					EntityResultTools.keysvalues(HotelDao.ATTR_NAME, attrMap.get(HotelDao.ATTR_NAME)),
//					EntityResultTools.attributes(HotelDao.ATTR_NAME));
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			} else {				
//				resultado = new EntityResultWrong("Error al crear Hotel - El registro ya existe");
//			}
//		}
		return resultado;
	}

	@Override
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.update(this.hotelDao, attrMap, keyMap);
			if(resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong("Error al actualizar Hotel - El regsitro que pretende actualizar no existe.");			
			}else {
			resultado.setMessage("Hotel actualizado");
			}
		
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong("Error al actualizar Hotel - No es posible duplicar un registro");		
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong("Error al actualizar Hotel - Falta algún campo obligatorio");
		} catch (SQLWarningException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong("Error al actualizar Hotel - "+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong("Error al actualizar Hotel");
		}
		return resultado;
	}

	@Override
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultMapImpl();		
		try {
			if (keyMap.containsKey(HotelDao.ATTR_ID)) {
				EntityResult auxEntity = this.daoHelper.query(this.hotelDao,
						EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)),
						EntityResultTools.attributes(HotelDao.ATTR_ID));
				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
					resultado = new EntityResultWrong("Error al eliminar Hotel - El Hotel a eliminar no existe");
				} else {
					resultado = this.daoHelper.delete(this.hotelDao, keyMap);
					resultado.setMessage("Hotel eliminado");
				}
			}else {
				resultado = new EntityResultWrong("Error al eliminar Hotel - Falta el htl_id (PK) del Hotel a eliminar");
			}
		}catch(DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al eliminar Hotel - Está referenciado en alguna otra tabla (FK)");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al eliminar Hotel");
		}
		return resultado;
	}

	public EntityResult hotelDataQuery(Map<String, Object> keysValues, List<String> attrList) {
		EntityResult queryRes = this.daoHelper.query(this.hotelDao,
				EntityResultTools.keysvalues("htl_id", keysValues.get("htl_id")),
				EntityResultTools.attributes(HotelDao.ATTR_ID, HotelDao.ATTR_NAME, HotelDao.ATTR_STREET,
						HotelDao.ATTR_CITY, HotelDao.ATTR_CP, HotelDao.ATTR_STATE, HotelDao.ATTR_COUNTRY,
						HotelDao.ATTR_PHONE, HotelDao.ATTR_EMAIL, HotelDao.ATTR_DESCRIPTION, HotelDao.ATTR_IS_OPEN),
				"queryHotel");
		return queryRes;
	}

}