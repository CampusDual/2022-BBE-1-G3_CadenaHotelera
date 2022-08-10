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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingColumnsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.FeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeFeatureDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultExtraTools;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
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

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {

			cf.reset();

			cf.setCPHtlColum(RoomDao.ATTR_HOTEL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.addBasics(RoomDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);
			resultado = this.daoHelper.query(this.roomDao, keyMap, attrList);

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomInfoQuery(Map<String, Object> keysValues, List<String> attrList) {

		EntityResult queryRes = new EntityResultWrong();
		try {

			cf.reset();

			cf.setCPHtlColum(RoomDao.ATTR_HOTEL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);

			cf.addBasics(RoomDao.fields, RoomTypeDao.fields, HotelDao.fields);
			cf.validate(keysValues);

			cf.validate(attrList);

			queryRes = this.daoHelper.query(this.roomDao, keysValues, attrList, "queryInfoRooms");
		} catch (Exception e) {
			queryRes = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return queryRes;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			List<String> required = new ArrayList<String>() {
				{
					add(RoomDao.ATTR_HOTEL_ID);
					add(RoomDao.ATTR_NUMBER);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(RoomDao.ATTR_ID);
				}
			};

			cf.setCPHtlColum(RoomDao.ATTR_HOTEL_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			
			cf.addBasics(RoomDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.roomDao, attrMap);
			resultado.setMessage("Room registrada");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}

		return resultado;
	}

	@Override 
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			// ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {
				{
					add(RoomDao.ATTR_ID);
				}
			};

			cf.reset();
			cf.addBasics(RoomDao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			List<String> restrictedData = new ArrayList<String>() {
				{
					add(RoomDao.ATTR_ID);// El id no se puede actualizar
				}
			};
			cf.reset();

			cf.addBasics(RoomDao.fields);
			cf.setRestricted(restrictedData);

			cf.validate(attrMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(RoomDao.ATTR_ID, keyMap.get(RoomDao.ATTR_ID));
				}
			};
			EntityResult auxEntity = roomQuery(subConsultaKeyMap,
					EntityResultTools.attributes(RoomDao.ATTR_ID)); //aquí validamos la resctricción por permisos
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.update(this.roomDao, attrMap, keyMap);
				if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
					resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
				} else {
					resultado = new EntityResultMapImpl();
					resultado.setMessage("Room actualizada");
				}
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(RoomDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(RoomDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(RoomDao.ATTR_ID, keyMap.get(RoomDao.ATTR_ID));
				}
			};

			EntityResult auxEntity = roomQuery(consultaKeyMap, EntityResultTools.attributes(RoomDao.ATTR_ID)); //aquí se controlan las restricciones

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.roomDao, keyMap);
				resultado.setMessage("Room eliminada");
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

	@Override //TODO PERMISOS
	public EntityResult roomsUnbookedInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			return roomsUnbookedgInRange(keyMap, attrList);
		} catch (ValidateException | EntityResultRequiredException e) {
			System.err.println(e.getMessage());
			return new EntityResultWrong(e.getMessage());
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
	 * @throws InvalidFieldsValuesException
	 */
	public EntityResult roomsUnbookedgInRange(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException, EntityResultRequiredException, ValidateException {
		EntityResult resultado = new EntityResultWrong();

		List<String> required = new ArrayList<String>() {
			{
				add(BookingDao.ATTR_START);
				add(BookingDao.ATTR_END);
			}
		};
		cf.reset();
		cf.addBasics(BookingDao.fields);
		cf.setRequired(required);
		cf.setOptional(false);

		try {
			cf.validate(keyMap);

			ValidateFields.dataRange(keyMap.get(BookingDao.ATTR_START), keyMap.get(BookingDao.ATTR_END));
//		if (ValidateFields.dataRange(keyMap.get(BookingDao.ATTR_START), keyMap.get(BookingDao.ATTR_END)) == 1) {
//			throw new InvalidFieldsValuesException(ErrorMessage.DATA_START_BEFORE_TODAY);
//		}

			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
			if (keyMap.containsKey(RoomDao.ATTR_ID)) {
				auxKeyMap.put(BookingDao.ATTR_ROOM_ID, keyMap.get(RoomDao.ATTR_ID));
			}
			if (keyMap.containsKey(RoomDao.ATTR_HOTEL_ID)) {
				auxKeyMap.put(RoomDao.ATTR_HOTEL_ID, keyMap.get(RoomDao.ATTR_HOTEL_ID));
			}

			List<Object> bookedRoomsIdList = roomsBookedInRange((String) keyMap.get(BookingDao.ATTR_START),
					(String) keyMap.get(BookingDao.ATTR_END), auxKeyMap);

			keyMap.remove(BookingDao.ATTR_START);
			keyMap.remove(BookingDao.ATTR_END);
			BasicExpression finalExp = new BasicExpression(new BasicField(RoomDao.ATTR_ID), BasicOperator.NOT_IN_OP,
					bookedRoomsIdList);
			EntityResultExtraTools.putBasicExpression(keyMap, finalExp);

			resultado = this.daoHelper.query(this.roomDao, keyMap, attrList, "queryInfoRooms");

		} catch (LiadaPardaException e) {
			resultado = new EntityResultWrong(e.getMessage());
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
	 * @throws InvalidFieldsValuesException
	 */
	public List<Object> roomsBookedInRange(Object startDate, Object endDate, Map<String, Object> bookingKeyMap)
			throws OntimizeJEERuntimeException, EntityResultRequiredException, InvalidFieldsValuesException {

		ValidateFields.dataRange(startDate, endDate);
		bookingKeyMap.put(BookingDao.ATTR_START, startDate);
		bookingKeyMap.put(BookingDao.ATTR_END, endDate);
		BasicExpression notCancelled = new BasicExpression(new BasicField(BookingDao.ATTR_CANCELED),
				BasicOperator.NULL_OP, null);
		EntityResultExtraTools.putBasicExpression(bookingKeyMap, notCancelled);

		EntityResult bookedRoomsER = bookingService.bookingsInRangeQuery(bookingKeyMap,
				EntityResultTools.attributes(BookingDao.ATTR_ROOM_ID));

		if (bookedRoomsER.isWrong()) {
			throw new EntityResultRequiredException(ErrorMessage.RESULT_REQUIRED + " - " + bookedRoomsER.getMessage());
		}
		return EntityResultExtraTools.listFromColumn(bookedRoomsER, BookingDao.ATTR_ROOM_ID);
	}

	public List<Object> roomsBookedgInRange(Object startDate, Object endDate)
			throws OntimizeJEERuntimeException, EntityResultRequiredException, InvalidFieldsValuesException {
		return roomsBookedInRange(startDate, endDate, new HashMap<>());
	}

	/**
	 * Comprueba si una habitación en concreto está disponible en un periodo de
	 * tiempo.
	 * 
	 * @param keyMap requiere checkin, checkout, room_id. El resto los ignora.
	 * @return True si la room_id está libre en esa franja de fechas.
	 * @throws OntimizeJEERuntimeException
	 * @throws EntityResultRequiredException
	 * @throws InvalidFieldsValuesException
	 * @throws MissingFieldsException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public boolean isRoomUnbookedgInRange(Object startDate, Object endDate, Object roomId)
			throws OntimizeJEERuntimeException, EntityResultRequiredException, InvalidFieldsValuesException,
			MissingFieldsException {
		if (startDate == null || endDate == null || roomId == null) {
			throw new MissingFieldsException(ErrorMessage.REQUIRED_FIELD);
		}
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(BookingDao.ATTR_ROOM_ID, roomId);
		List<Object> bookedRoomsIdList = roomsBookedInRange(startDate, endDate, keyMap);
		return bookedRoomsIdList.isEmpty();
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult infoHotelFeaturesQuery(Map<String, Object> keyMap, List<String> attrList) {
		EntityResult resultado = new EntityResultWrong();

		try {

			cf.reset();
			cf.addBasics(RoomDao.fields);
			cf.addBasics(RoomTypeDao.fields);
			cf.addBasics(RoomTypeFeatureDao.fields);
			cf.addBasics(FeatureDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.roomDao, keyMap, attrList, "queryHotelFeaturesTypes");

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

}
