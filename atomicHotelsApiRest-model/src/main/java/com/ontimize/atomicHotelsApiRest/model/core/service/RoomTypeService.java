package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgresql.xml.EmptyStringEntityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class RoomTypeService implements IHotelService {

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
		resultado.setMessage("mensaje cambiado"); // TODO eliminar
		return resultado;
	}

	@Override
	public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();

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

		// TODO limpiar pruebas de setMessage

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
		
		// OPCION C (comprobando si el registro ya existe)
		if (attrMap.containsKey(HotelDao.ATTR_NAME)) {
			EntityResult auxEntity = this.daoHelper.query(this.hotelDao,
					EntityResultTools.keysvalues(HotelDao.ATTR_NAME, attrMap.get(HotelDao.ATTR_NAME)),
					EntityResultTools.attributes(HotelDao.ATTR_NAME));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = this.daoHelper.insert(this.hotelDao, attrMap);
			} else {
				resultado.setCode(EntityResult.OPERATION_WRONG);
				resultado.setMessage("Error al crear Hotel - El registro ya existe");
			}
		}
		return resultado;
	}

	@Override
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.hotelDao, attrMap, keyMap);
	}

	@Override
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.hotelDao, keyMap);
	}
	

	public EntityResult hotelDataQuery(Map<String, Object> keysValues, List<String> attrList) {
		 EntityResult queryRes = this.daoHelper.query(this.hotelDao, EntityResultTools.keysvalues("htl_id", keysValues.get("htl_id")), 
				 EntityResultTools.attributes("htl_id", "htl_name", "htl_street","htl_city","htl_postal_code","htl_state","htl_country","htl_phone","htl_email"),"queryHotel");
    return queryRes;
	}

}