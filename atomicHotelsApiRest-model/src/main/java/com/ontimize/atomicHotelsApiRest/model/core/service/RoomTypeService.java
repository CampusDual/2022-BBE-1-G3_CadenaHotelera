package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
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

		if (attrMap.containsKey(RoomTypeDao.ATTR_NAME)) {
			EntityResult auxEntity = this.daoHelper.query(this.roomTypeDao,
					EntityResultTools.keysvalues(RoomTypeDao.ATTR_NAME, attrMap.get(RoomTypeDao.ATTR_NAME)),
					EntityResultTools.attributes(RoomTypeDao.ATTR_NAME));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = this.daoHelper.insert(this.roomTypeDao, attrMap);
			} else {
				resultado.setCode(EntityResult.OPERATION_WRONG);
				resultado.setMessage("Error al crear RoomType - El registro ya existe");
			}
		}
		return resultado;
	}

	@Override
	public EntityResult roomTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.roomTypeDao, attrMap, keyMap);
	}

	@Override
	public EntityResult roomTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.roomTypeDao, keyMap);
	}

	public EntityResult roomTypeDataQuery(Map<String, Object> keyMap, List<String> attrList) {
		Map<String, Object> auxKeyMap = new HashMap<String, Object>();
		if (keyMap.containsKey(RoomTypeDao.ATTR_ID)) {
			auxKeyMap.put(RoomTypeDao.ATTR_ID, keyMap.get(RoomTypeDao.ATTR_ID));
		}
		
		EntityResult queryRes = this.daoHelper.query(
				this.roomTypeDao, auxKeyMap, EntityResultTools.attributes(RoomTypeDao.ATTR_ID, RoomTypeDao.ATTR_NAME, RoomTypeDao.ATTR_DESCRIPTION,
						RoomTypeDao.ATTR_PRICE, "bdc_name", "bdc_slots"),
				"queryRoomType");
		return queryRes;
	}

}