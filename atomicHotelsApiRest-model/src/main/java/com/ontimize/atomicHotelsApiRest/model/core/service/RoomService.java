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

import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
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
		EntityResult resultado = this.daoHelper.query(this.roomDao, keyMap, attrList,"queryEnabledRooms");
		return resultado;
	}

	@Override
	public EntityResult roomInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.roomDao, keyMap, attrList,"queryInfoRooms");
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

	@Override
	public EntityResult roomsUnbookedInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
//		EntityResult resultado;
//
//		if (keyMap.containsKey(BookingDao.NON_ATTR_START_DATE) && keyMap.containsKey(BookingDao.NON_ATTR_END_DATE)) {
//			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
//			auxKeyMap.put(BookingDao.NON_ATTR_START_DATE, keyMap.get(BookingDao.NON_ATTR_START_DATE));
//			auxKeyMap.put(BookingDao.NON_ATTR_END_DATE, keyMap.get(BookingDao.NON_ATTR_END_DATE));
//
//			// omite reservas con estados cancelados
//			BasicExpression bookingStatusFilter = new BasicExpression(new BasicField(BookingDao.ATTR_STATUS_ID),
//					BasicOperator.NOT_EQUAL_OP, BookingDao.STATUS_CANCELED);
//			auxKeyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, bookingStatusFilter);
//
//			EntityResult bookedRoomsER = bookingService.bookingsInRangeQuery(auxKeyMap,
//					EntityResultTools.attributes(BookingDao.ATTR_ROOM_ID));
//
//			keyMap.remove(BookingDao.NON_ATTR_START_DATE);
//			keyMap.remove(BookingDao.NON_ATTR_END_DATE);
//
//			List<Object> bookedRoomsIdList = EntityResultExtraTools.listFromColumn(bookedRoomsER,
//					BookingDao.ATTR_ROOM_ID);
//
//			BasicExpression finalExp = new BasicExpression(new BasicField(RoomDao.ATTR_ID), BasicOperator.NOT_IN_OP,
//					bookedRoomsIdList);
//			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, finalExp);
//			resultado = this.daoHelper.query(this.roomDao, keyMap, attrList, "queryRooms");
//		} else {
//			resultado = new EntityResultWrong("Faltan campos necesarios, checkin o checkout");
//		}
		try {
			return roomsUnbookedgInRange(keyMap, attrList);
		} catch (MissingFieldsException e) {
			e.printStackTrace();
			return new EntityResultWrong("Faltan campos requeridos");
		} catch (EntityResultRequiredException e) {
			e.printStackTrace();
			return new EntityResultWrong("Error al realizar consultas dependientes.");
		}
	}

	/**
	 * Metodo para obtener una lista de habitaciones disponibles para reservar.
	 * 
	 * @param keyMap   Requiere mínimo BookingDao.NON_ATTR_START_DATE y
	 *                 BookingDao.NON_ATTR_END_DATE
	 * @param attrList columnas devueltas
	 * @return EntityResult
	 * @throws OntimizeJEERuntimeException
	 * @throws MissingFieldsException
	 * @throws EntityResultRequiredException 
	 */
	public EntityResult roomsUnbookedgInRange(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException, MissingFieldsException, EntityResultRequiredException {
		EntityResult resultado;
		if (keyMap.containsKey(BookingDao.NON_ATTR_START_DATE) && keyMap.containsKey(BookingDao.NON_ATTR_END_DATE)) {
			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
			if (keyMap.containsKey(RoomDao.ATTR_ID)) {
				auxKeyMap.put(BookingDao.ATTR_ROOM_ID, keyMap.get(RoomDao.ATTR_ID));
			}			
			if (keyMap.containsKey(RoomDao.ATTR_HOTEL_ID)) {
				auxKeyMap.put(BookingDao.ATTR_ROOM_ID, keyMap.get(RoomDao.ATTR_ID));
			} //comprobar filtro id hotel en reservas
			
			List<Object> bookedRoomsIdList = roomsBookedInRange((String) keyMap.get(BookingDao.NON_ATTR_START_DATE), (String) 
					keyMap.get(BookingDao.NON_ATTR_END_DATE), auxKeyMap);

			//todo comprobar punteros
			keyMap.remove(BookingDao.NON_ATTR_START_DATE);
			keyMap.remove(BookingDao.NON_ATTR_END_DATE);
			BasicExpression finalExp = new BasicExpression(new BasicField(RoomDao.ATTR_ID), BasicOperator.NOT_IN_OP,
					bookedRoomsIdList);
			EntityResultExtraTools.putBasicExpression(keyMap, finalExp);

			resultado = this.daoHelper.query(this.roomDao, keyMap, attrList, "queryInfoRooms");
		} else {
			throw new MissingFieldsException("Faltan campos necesarios, checkin o checkout");
		}

		return resultado;
	}

	/**
	 * Metodo para obtener una lista de las habitaciones ocupadas total o
	 * parcialmente, en un rango de fechas y filtros (attributos BookingDao). Puede
	 * contener duplicados.
	 * 
	 * @param startDate fecha de inicio (tiene en cuenta las salidas el mismo día
	 *                  que ya son antes de las entradas)
	 * @param endDate   fecha de fin de rango
	 * @return List<Object> relación de ID de habitación que tiene reservas en esas
	 *         fechas. Puede contener duplicados.
	 * @throws OntimizeJEERuntimeException
	 * @throws EntityResultRequiredException
	 */
	public List<Object> roomsBookedInRange(String startDate, String endDate, Map<String, Object> bookingKeyMap)
			throws OntimizeJEERuntimeException, EntityResultRequiredException {
		bookingKeyMap.put(BookingDao.NON_ATTR_START_DATE, startDate);
		bookingKeyMap.put(BookingDao.NON_ATTR_END_DATE, endDate);

		// omite reservas con estados cancelados
		BasicExpression bookingStatusFilter = new BasicExpression(new BasicField(BookingDao.ATTR_STATUS_ID),
				BasicOperator.NOT_EQUAL_OP, BookingDao.STATUS_CANCELED);
		EntityResultExtraTools.putBasicExpression(bookingKeyMap, bookingStatusFilter);

		EntityResult bookedRoomsER = bookingService.bookingsInRangeQuery(bookingKeyMap,
				EntityResultTools.attributes(BookingDao.ATTR_ROOM_ID));
		if (bookedRoomsER.isWrong()) {
			throw new EntityResultRequiredException();
		}
		// bookedRoomsER.get(BookingDao.ATTR_ROOM_ID); //todo comprobar.		
		return EntityResultExtraTools.listFromColumn(bookedRoomsER, BookingDao.ATTR_ROOM_ID);
	}

	public List<Object> roomsBookedgInRange(String startDate, String endDate)
			throws OntimizeJEERuntimeException, EntityResultRequiredException {
		Map<String, Object> bookingKeyMap = new HashMap<>();
		return roomsBookedInRange(startDate, endDate, bookingKeyMap);
	}

	/**
	 * Comprueba si una habitación en concreto está disponible en un periodo de
	 * tiempo.
	 * 
	 * @param keyMap requiere checkin, checkout, room_id. El resto los ignora.
	 * @return True si la room_id está libre en esa franja de fechas.
	 * @throws OntimizeJEERuntimeException
	 * @throws EntityResultRequiredException
	 * @throws MissingFieldsException
	 */
	 @Override
	public boolean isRoomUnbookedgInRangeQuery(String startDate, String endDate, Integer roomId)
			throws OntimizeJEERuntimeException, EntityResultRequiredException {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(BookingDao.ATTR_ROOM_ID, roomId);
		List<Object> bookedRoomsIdList = roomsBookedInRange(startDate, endDate, keyMap);

		return bookedRoomsIdList.isEmpty();

	}

}
