package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultExtraTools;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("RoomService")
@Lazy
public class RoomService implements IRoomService {
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	@Autowired
	IBookingService bookingService;

	@Override
	public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.roomDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult roomInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.insert(this.roomDao, attrMap);
			resultado.setMessage("Room registrada");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al crear Room - El registro ya existe");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong("Error al crear Room - Falta algún campo obligatorio");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al registrar Room");
		}

		return resultado;

	}

	@Override
	public EntityResult roomUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			resultado = this.daoHelper.update(this.roomDao, attrMap, keyMap);
			resultado.setMessage("Room actualizada");
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong("Error al actualizar Room - No es posible duplicar un registro");
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(
					"Error al actualizar Room - No existe el tipo de habitación (FK rm_rmt_id)");
		} catch (SQLWarningException e) {
			resultado = new EntityResultWrong("Error al actualizar Room - Falta el rm_id (PK) de la Room a actualizar");
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al actualizar Room");
		}
		return resultado;
	}

	@Override
	public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			if (keyMap.containsKey(RoomDao.ATTR_ID)) {
				EntityResult auxEntity = this.daoHelper.query(this.roomDao,
						EntityResultTools.keysvalues(RoomDao.ATTR_ID, keyMap.get(RoomDao.ATTR_ID)),
						EntityResultTools.attributes(RoomDao.ATTR_ID));
				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
					resultado = new EntityResultWrong("Error al eliminar Room - La Room a eliminar no existe");
				} else {
					resultado = this.daoHelper.delete(this.roomDao, keyMap);
					resultado.setMessage("Room eliminada");
				}
			} else {
				resultado = new EntityResultWrong("Error al eliminar Room - Falta el rm_id (PK) de la Room a eliminar");
			}
		} catch (Exception e) {
			resultado = new EntityResultWrong("Error al eliminar Room");
		}
		return resultado;
	}

	public EntityResult roomDataQuery(Map<String, Object> keysValues, List<String> attrList) {
		return this.daoHelper.query(this.roomDao, keysValues, attrList, "queryRooms");
	}

	@Override
	public EntityResult roomsUnbookedgInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();

		if (keyMap.containsKey(BookingDao.NON_ATTR_START_DATE) && keyMap.containsKey(BookingDao.NON_ATTR_END_DATE)) {
			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
			auxKeyMap.put(BookingDao.NON_ATTR_START_DATE, keyMap.get(BookingDao.NON_ATTR_START_DATE));
			auxKeyMap.put(BookingDao.NON_ATTR_END_DATE, keyMap.get(BookingDao.NON_ATTR_END_DATE));

			EntityResult bookedRoomsER = bookingService.bookingsInRangeQuery(auxKeyMap,
					EntityResultTools.attributes(BookingDao.ATTR_ROOM_ID));
			keyMap.remove(BookingDao.NON_ATTR_START_DATE);
			keyMap.remove(BookingDao.NON_ATTR_END_DATE);				
			
			List<Object> bookedRoomsIdList = EntityResultExtraTools.listFromColumn(bookedRoomsER, BookingDao.ATTR_ROOM_ID);			
			BasicExpression finalExp = new BasicExpression(new BasicField(RoomDao.ATTR_ID), BasicOperator.NOT_IN_OP,bookedRoomsIdList );
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, finalExp);
			resultado = this.daoHelper.query(this.roomDao, keyMap, attrList);
		} else {
			resultado = new EntityResultWrong("Faltan campos necesarios, checkin o checkout");
		}

		return resultado;
	}

}
