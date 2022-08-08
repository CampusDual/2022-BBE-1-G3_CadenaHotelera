package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BedComboDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao.Action;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlPermissions;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultExtraTools;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingService")
@Lazy
public class BookingService implements IBookingService {

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private BookingGuestService bookingGuestsService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	IRoomService roomService;

	@Autowired
	ControlFields cf;

	@Autowired
	ControlPermissions cp;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultWrong();
//		String a = SecurityContextHolder.getContext().toString();
//		String b = SecurityContextHolder.getContext().getAuthentication().toString();
//		String c = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//		UserInformation ui = ((UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//		Map<Object,Object> otrosDatos = ((UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getOtherData();
		
//		String usuario = ((UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin(); 
//		System.err.println(usuario);
//		System.err.println(((UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));		
		
		try {
			cp.reset();
			cp.setHtlColum(RoomDao.ATTR_HOTEL_ID);//hacer join en default
			cp.setRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER,UserRoleDao.ROLE_STAFF);
			cp.restrict(keyMap);
			
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.addBasics(RoomDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList);
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.addBasics(RoomDao.fields);
			cf.addBasics(RoomTypeDao.fields);
			cf.addBasics(HotelDao.fields);
			cf.addBasics(CustomerDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryInfoBooking");
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingInsert(Map<String, Object> attrMap)
			throws OntimizeJEERuntimeException, EntityResultRequiredException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			List<String> required = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_START);
					add(BookingDao.ATTR_END);
					add(BookingDao.ATTR_ROOM_ID);
					add(BookingDao.ATTR_CUSTOMER_ID);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_CHECKIN);
					add(BookingDao.ATTR_CHECKOUT);
					add(BookingDao.ATTR_CANCELED);
					add(BookingDao.ATTR_CREATED);
				}
			};

			cf.addBasics(BookingDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.setOptional(false);
			cf.validate(attrMap);
			Boolean ValidCredit = customerService.isCustomerValidBookingHolder(attrMap.get(BookingDao.ATTR_CUSTOMER_ID));

			if (ValidCredit.booleanValue()) {

				if (ValidateFields.dataRange(attrMap.get(BookingDao.ATTR_START),
						attrMap.get(BookingDao.ATTR_END)) == 0) {
					if (roomService.isRoomUnbookedgInRange(attrMap.get(BookingDao.ATTR_START),
							attrMap.get(BookingDao.ATTR_END), attrMap.get(BookingDao.ATTR_ROOM_ID))) {
						resultado = this.daoHelper.insert(this.bookingDao, attrMap);
					} else {
						resultado = new EntityResultWrong("La habitación ya está reservada en esa franja de fechas.");
					}
				} else {
					resultado = new EntityResultWrong(ErrorMessage.DATA_START_BEFORE_TODAY);
				}
			} else {
				resultado = new EntityResultWrong("No se puede reservar, tarjeta no valida.");
			}
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
			e.printStackTrace();
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	

	

	/**
	 * Requiere el campo de filtrado (keyMap) ID y el campo de data (attrMap)
	 * action. El resto los ignora. acciones válidas : CHECKIN,CHECKOUT,CANCEL
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingActionUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultadoER = new EntityResultWrong(ErrorMessage.INVALID_ACTION);
		try {
			// ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);
			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			List<String> requiredData = new ArrayList<String>() {
				{
					add(BookingDao.NON_ATTR_ACTION);
				}
			};
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.setRequired(requiredData);
			cf.setOptional(false);
			cf.validate(attrMap);

			// TODO pasar a ControlFields
//			BookingDao.Action action;
//			try {
//				action = BookingDao.Action.valueOf((String) attrMap.get(BookingDao.NON_ATTR_ACTION));
//				attrMap.remove(BookingDao.NON_ATTR_ACTION);
//			} catch (IllegalArgumentException e) {
//				throw new InvalidFieldsValuesException(
//						ErrorMessage.INVALID_ACTION + " - " + attrMap.get(BookingDao.NON_ATTR_ACTION));
//			}
			BookingDao.Action action = (Action) attrMap.get(BookingDao.NON_ATTR_ACTION);
			BookingDao.Status status = getBookingStatus(keyMap.get(BookingDao.ATTR_ID));

			switch (status) {
			case CANCELED:
				resultadoER = new EntityResultWrong("No se pueden modificar reservas canceladas");
				break;

			case COMPLETED:
				resultadoER = new EntityResultWrong("No se pueden modificar reservas finalizadas");
				break;

			case IN_PROGRESS:
				if (action == BookingDao.Action.CHECKOUT) {
					resultadoER = this.daoHelper.update(this.bookingDao,
							EntityResultTools.keysvalues(BookingDao.ATTR_CHECKOUT, new Date()), keyMap);
				} else if (action == BookingDao.Action.CHECKIN) {
					resultadoER = new EntityResultWrong("El checkin ya se ha realizado");
				} else if (action == BookingDao.Action.CANCEL) {
					resultadoER = new EntityResultWrong("No se puede cancelar una reserva en proceso");
				}
				break;

			case CONFIRMED:
				if (action == BookingDao.Action.CHECKIN) {
					resultadoER = this.daoHelper.update(this.bookingDao,
							EntityResultTools.keysvalues(BookingDao.ATTR_CHECKIN, new Date()), keyMap);
				} else if (action == BookingDao.Action.CANCEL) {
					resultadoER = this.daoHelper.update(this.bookingDao,
							EntityResultTools.keysvalues(BookingDao.ATTR_CANCELED, new Date()), keyMap);
				} else if (action == BookingDao.Action.CHECKOUT) {
					resultadoER = new EntityResultWrong(
							"Para realizar el checkout, primero debe realizarse el checkin");
				}
				break;
			}

		} catch (ValidateException | EntityResultRequiredException e) {
			System.err.println(e.getMessage());
			resultadoER = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultadoER = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultadoER;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
//		return this.daoHelper.delete(this.bookingDao, keyMap);
		return new EntityResultWrong("No se pueden eliminar reservas, debe cancelarla");
	}

//TODO ver bien el validador en los tres siguientes métodos
	@Override
	public EntityResult bookingsInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException, InvalidFieldsValuesException {
		try {
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.addBasics(RoomDao.fields);

			bookingsInRangeBuilder(keyMap, attrList);
			return this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryBasicBooking");
		} catch (ValidateException | LiadaPardaException e) {
			System.err.println(e.getMessage());
			return new EntityResultWrong(e.getMessage());
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingsInRangeInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(CustomerDao.fields);
			cf.addBasics(BookingDao.fields);
			cf.addBasics(RoomDao.fields);
			cf.addBasics(RoomTypeDao.fields);
			cf.addBasics(HotelDao.fields);

			bookingsInRangeBuilder(keyMap, attrList);
			resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryInfoBooking");
		} catch (ValidateException | LiadaPardaException e) {
			System.err.println(e.getMessage());
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	/**
	 * Modifica las keyMap y attrList, para realizar las basic expresions
	 * 
	 * @param keyMap
	 * @param attrList
	 * @throws MissingFieldsException
	 * @throws InvalidFieldsValuesException
	 */
	public void bookingsInRangeBuilder(Map<String, Object> keyMap, List<String> attrList)
			throws ValidateException, LiadaPardaException {

		List<String> required = new ArrayList<String>() {
			{
				add(BookingDao.ATTR_START);
				add(BookingDao.ATTR_END);
			}
		};
		cf.setRequired(required);
		cf.validate(keyMap);

//		Date rangeStart = ValidateFields.stringToDate((String) keyMap.get(BookingDao.ATTR_START));
//		Date rangeEnd = ValidateFields.stringToDate((String) keyMap.get(BookingDao.ATTR_END));

		Date rangeStart = (Date) keyMap.get(BookingDao.ATTR_START);
		Date rangeEnd = (Date) keyMap.get(BookingDao.ATTR_END);

		ValidateFields.dataRange(rangeStart, rangeEnd);

		BasicField bkgStart = new BasicField(BookingDao.ATTR_START);
		BasicField bkgEnd = new BasicField(BookingDao.ATTR_END);

		/*
		 * (range_checkin >= bkg_checkin AND range_checkin < bkg_checkout) OR
		 * (range_checkout > bkg_checkin AND range_checkout <= bkg_checkout) OR
		 * (range_checkin < bkg_checkin AND range_chekout > bkg_checkout)
		 */

		// (range_checkin >= bkg_checkin AND range_checkin < bkg_checkout) OR
		BasicExpression exp01 = new BasicExpression(bkgStart, BasicOperator.LESS_EQUAL_OP, rangeStart);
		BasicExpression exp02 = new BasicExpression(bkgEnd, BasicOperator.MORE_OP, rangeStart);
		BasicExpression groupExp01 = new BasicExpression(exp01, BasicOperator.AND_OP, exp02); // dentro
																								// rangeCheckin

		// (range_checkout > bkg_checkin AND range_checkout <= bkg_checkout) OR
		BasicExpression exp03 = new BasicExpression(bkgEnd, BasicOperator.MORE_EQUAL_OP, rangeEnd);
		BasicExpression exp04 = new BasicExpression(bkgStart, BasicOperator.LESS_OP, rangeEnd);
		BasicExpression groupExp02 = new BasicExpression(exp03, BasicOperator.AND_OP, exp04);// dentro
																								// rangeCheckout

		BasicExpression exp05 = new BasicExpression(bkgStart, BasicOperator.MORE_OP, rangeStart);
		BasicExpression exp06 = new BasicExpression(bkgEnd, BasicOperator.LESS_OP, rangeEnd);
		BasicExpression groupExp03 = new BasicExpression(exp05, BasicOperator.AND_OP, exp06);// dentro checkin y
																								// checkout

		// las uno
		BasicExpression auxFilterRangeBE = new BasicExpression(groupExp01, BasicOperator.OR_OP, groupExp02);
		BasicExpression filterRangeBE = new BasicExpression(auxFilterRangeBE, BasicOperator.OR_OP, groupExp03);

		EntityResultExtraTools.putBasicExpression(keyMap, filterRangeBE); // nuevo metodo

		keyMap.remove(BookingDao.ATTR_START);
		keyMap.remove(BookingDao.ATTR_END);
	}

//	public BookingDao.Status getBookingStatus(EntityResult consultaER) throws EntityResultRequiredException {
//		
//	}
//	
	/**
	 * Para obtener el estado un de una reserva
	 * 
	 * @param bookingId Id de la reserva (Object)
	 * @return BookigDao.Status (enum estado)
	 * @throws EntityResultRequiredException Si da error al hacer la consulta
	 */
	public BookingDao.Status getBookingStatus(Object bookingId) throws EntityResultRequiredException {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);

//		List<String> attrList = new ArrayList<>();
//		attrList.add(BookingDao.ATTR_START);
//		attrList.add(BookingDao.ATTR_END);
//		attrList.add(BookingDao.ATTR_CHECKIN);
//		attrList.add(BookingDao.ATTR_CHECKOUT);
//		attrList.add(BookingDao.ATTR_CANCELED);
//		attrList.add(BookingDao.ATTR_CREATED);
		List<String> attrList = Arrays.asList(BookingDao.ATTR_CHECKIN, BookingDao.ATTR_CHECKOUT,
				BookingDao.ATTR_CANCELED);
		EntityResult consultaER = this.daoHelper.query(this.bookingDao, keyMap, attrList);

		if (consultaER.calculateRecordNumber() == 1) {
			if (consultaER.getRecordValues(0).get(BookingDao.ATTR_CANCELED) != null) {
				return BookingDao.Status.CANCELED;
			} else if (consultaER.getRecordValues(0).get(BookingDao.ATTR_CHECKOUT) != null) {
				return BookingDao.Status.COMPLETED;
			} else if (consultaER.getRecordValues(0).get(BookingDao.ATTR_CHECKIN) != null) {
				return BookingDao.Status.IN_PROGRESS;
			} else {
				return BookingDao.Status.CONFIRMED;
			}
		} else {
			throw new EntityResultRequiredException("Error al consultar estado de la reserva");
		}
	}

	/**
	 * Dado un bkg_id devuelve los días de esa reserva y el precio diario de la
	 * habitación
	 * 
	 * @param keyMap   (BookingDao.ATTR_ID)
	 * @param attrList (anyList())
	 * @return EntityResult
	 *         (BookingDao.ATTR_ID,RoomTypeDao.ATTR_PRICE,ReceiptDao.ATTR_DIAS)
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDaysUnitaryRoomPriceQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.addBasics(RoomTypeDao.fields);
			cf.addBasics(RoomDao.fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryDiasPrecioUnitarioHabitacion");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingsHotelsQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.addBasics(RoomDao.fields);
			cf.addBasics(HotelDao.fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryBookingsHotel");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	/*
	 * @Override public EntityResult booking_now_by_room_numberQuery (Map<String,
	 * Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
	 * 
	 * EntityResult resultado = new EntityResultWrong(); try { List<String> required
	 * = new ArrayList<String>() { { add(BookingDao.ATTR_ID); } };
	 * 
	 * resultado = infoRangeBooking( keyMap, attrList);
	 *
	 * Map<String, Object> subConsultaKeyMap = new HashMap<>() { {
	 * put(BookingDao.ATTR_ID, keyMap.get(BookingDao.ATTR_ID)); } };
	 * 
	 * EntityResult auxEntity = bookingQuery(subConsultaKeyMap,
	 * EntityResultTools.attributes(BookingDao.ATTR_ID));
	 * 
	 * if (resultado.calculateRecordNumber() == 0) { // si no hay registros...
	 * resultado = new EntityResultWrong(ErrorMessage.INVALID_FILTER_FIELD_ID); }
	 * else { resultado.setMessage("Búsqueda correcta"); } } catch (Exception e) {
	 * resultado = new EntityResultWrong(ErrorMessage.ERROR); } return resultado; }
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult booking_now_by_room_numberQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			List<String> required = Arrays.asList(BookingDao.ATTR_ROOM_ID);
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.addBasics(RoomDao.fields);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList,
					"queryBookedRoomForAddingExtraServices");
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	/**
	 * Dado un número de reserva, la capacidad total de todas las habitaciones de
	 * esa reserva
	 * 
	 * @param keyMap   (BookingDao.ATTR_ID)
	 * @param attrList (anyList())
	 * @return EntityResult (BookingGuestDao.ATTR_TOTAL_SLOTS)
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingSlotsInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryBookingSlotsInfo");
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	/**
	 * Dado un número de reserva, devuelve datos relevantes de la misma
	 * 
	 * @param keyMap   (BookingDao.ATTR_ID)
	 * @param attrList (anyList())
	 * @return EntityResult
	 *         (BookingDao.ATTR_ID,HotelDao.ATTR_ID,CustomerDao.ATTR_NAME,CustomerDao.ATTR_SURNAME,
	 *         total_guests, total_slots, rooms, guests
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingCompleteInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultadoFinal = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			// Devuelve todas las habitaciones de la reserva
			EntityResult habitaciones = bookingHotelRoomRoomTypeQuery(keyMap, new ArrayList<String>());

			Map<String, Object> bookingGuestsId = new HashMap<String, Object>() {
				{
					put(BookingGuestDao.ATTR_BKG_ID, keyMap.get(BookingDao.ATTR_ID));
				}
			};

			// Devuelve todos los huéspedes de la reserva
			EntityResult huespedes = bookingGuestsService.bookingGuestsInfoQuery(bookingGuestsId,
					new ArrayList<String>());

			// Devuelve el número de huéspedes que ya están asociados a la reserva
			EntityResult totalGuests = bookingGuestsService.guestCountQuery(bookingGuestsId, new ArrayList<String>());

			if (totalGuests.isWrong()) {
				throw new EntityResultRequiredException(totalGuests.getMessage());
			}

			// Devuleve la capacidad total de las habitaciones de la reserva
			EntityResult totalSlots = this.bookingSlotsInfoQuery(keyMap, new ArrayList<String>());

			if (totalSlots.isWrong()) {
				throw new EntityResultRequiredException(totalSlots.getMessage());
			}

			List<String> listaGenericaBooking = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_ID);
					add(HotelDao.ATTR_ID);
					add(CustomerDao.ATTR_NAME);
					add(CustomerDao.ATTR_SURNAME);
				}
			};

			// Devuelve el número de reserva, el hotel y el nombre del cliente que paga la
			// reserva
			EntityResult resultadoGenerico = this.daoHelper.query(this.bookingDao, keyMap, listaGenericaBooking,
					"queryInfoBooking");

			Map<String, Object> mapFinal = new HashMap<String, Object>();

			List<Object> hab = new ArrayList<Object>();
			for (int i = 0; i < habitaciones.calculateRecordNumber(); i++) {
				Object h = habitaciones.getRecordValues(i);
				hab.add(h);
			}

			List<Object> huesp = new ArrayList<Object>();
			for (int i = 0; i < huespedes.calculateRecordNumber(); i++) {
				Object h = huespedes.getRecordValues(i);
				huesp.add(h);
			}

			mapFinal.putAll(resultadoGenerico.getRecordValues(0));
			mapFinal.putAll(totalGuests.getRecordValues(0));
			mapFinal.putAll(totalSlots.getRecordValues(0));
			mapFinal.put("rooms", hab);
			mapFinal.put("guests", huesp);

			resultadoFinal.addRecord(mapFinal);

		} catch (ValidateException e) {
			resultadoFinal = new EntityResultWrong(e.getMessage());
		} catch (EntityResultRequiredException e) {
			e.printStackTrace();
			resultadoFinal = new EntityResultWrong(ErrorMessage.ERROR);
		} catch (Exception e) {
			resultadoFinal = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultadoFinal;
	}

	/**
	 * Dado un número de reserva, devuelve los números de habitaciones asociadas y
	 * el tipo de habitaciones
	 * 
	 * @param keyMap   (BookingDao.ATTR_ID)
	 * @param attrList (anyList())
	 * @return EntityResult (RoomDao.ATTR_NUMBER,RoomTypeDao.ATTR_NAME)
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingHotelRoomRoomTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(BookingDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(BookingDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			List<String> lista = new ArrayList<String>() {
				{
					add(RoomDao.ATTR_NUMBER);
					add(RoomTypeDao.ATTR_NAME);

				}
			};

			resultado = this.daoHelper.query(this.bookingDao, keyMap, lista, "queryInfoBooking");
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

}