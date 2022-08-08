package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingGuestService")
@Lazy
public class BookingGuestService implements IBookingGuestService {

	@Autowired
	private BookingGuestDao bookingGuestDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	ControlFields cf;

	@Override
	public EntityResult bookingGuestQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(BookingGuestDao.fields);
			cf.validate(keyMap);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingGuestDao, keyMap, attrList);

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}

		return resultado;
	}

	/**
	 * Dado el id de una reserva, devuelve el número de huéspedes que ya están asociados a
	 * esa reserva
	 * 
	 * @param keyMap   (BookingGuestDao.ATTR_ATTR_BKG_ID)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException 
	 * @return EntityResult (BookingGuestDao.ATTR_TOTAL_GUESTS)
	 */
	@Override
	public EntityResult guestCountQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = Arrays.asList(BookingGuestDao.ATTR_BKG_ID);

			cf.reset();
			cf.addBasics(BookingGuestDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingGuestDao, keyMap, attrList, "queryGuestCount");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}
	
	/**
	 * Dado el id de una reserva (bgs_bkg_id), devuelve los nombres, apellidos y nifs de los huéspedes que ya están asociados a
	 * esa reserva
	 * 
	 * @param keyMap   (BookingGuestDao.ATTR_ATTR_BKG_ID)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException 
	 * @return EntityResult (CustomerDao.ATTR_NAME,CustomerDao.ATTR_SURNAME, CustomerDao.ATTR_IDEN_DOC)
	 */
	@Override
	public EntityResult bookingGuestsInfoQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException{
		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = Arrays.asList(BookingGuestDao.ATTR_BKG_ID);

			cf.reset();
			cf.addBasics(BookingGuestDao.fields);
			cf.addBasics(CustomerDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingGuestDao, keyMap, attrList, "queryBookingGuestsInfo");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult bookingGuestInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			List<String> required = Arrays.asList(BookingGuestDao.ATTR_BKG_ID, BookingGuestDao.ATTR_CST_ID);
			List<String> restricted = Arrays.asList(BookingGuestDao.ATTR_ID, BookingGuestDao.ATTR_REGISTRATION_DATE);
			cf.addBasics(BookingGuestDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.setOptional(false);
			cf.validate(attrMap);

			// Para que una reserva admita un nuevo huesped, esta tiene que estar en estado
			// 'CONFIRMED'
			if (bookingService.getBookingStatus(attrMap.get(BookingGuestDao.ATTR_BKG_ID))
					.equals(BookingDao.Status.CONFIRMED)) {

				Map<String, Object> customerId = new HashMap<String, Object>() {
					{
						put(CustomerDao.ATTR_ID, attrMap.get(BookingGuestDao.ATTR_CST_ID));
					}
				};
				List<String> customerIdentityDocument = Arrays.asList(CustomerDao.ATTR_IDEN_DOC);

				// Devuelve el documento de identidad de un cliente en caso de que este lo tenga
				EntityResult regularCustomer = customerService.customerQuery(customerId, customerIdentityDocument);

				// Si el cliente no tiene documento de identidad, no es un persona física y por
				// lo tanto no podrá ser un huesped
				if (regularCustomer.getRecordValues(0).get(CustomerDao.ATTR_IDEN_DOC) != null) {

					Map<String, Object> reservaGuest = new HashMap<String, Object>() {
						{
							put(BookingGuestDao.ATTR_BKG_ID, attrMap.get(BookingGuestDao.ATTR_BKG_ID));
						}
					};

					// Devuelve el atributo total_guests indepnedientemente de lo que se introduzca,
					// por se le pasa una lista cualquiera
					EntityResult guestCount = this.guestCountQuery(reservaGuest, new ArrayList<String>());

					long totalG = 0;

					// Si ya hay huespedes en esa reserva, se recoge cuantos son
					if (guestCount.calculateRecordNumber() > 0) {
						totalG = (long) guestCount.getRecordValues(0).get(BookingGuestDao.ATTR_TOTAL_GUESTS);
					} else {
						// Si no hay huéspedes todavía en la reserva, el número actual será cero
						totalG = 0;
					}

					Map<String, Object> reserva = new HashMap<String, Object>() {
						{
							put(BookingDao.ATTR_ID, attrMap.get(BookingGuestDao.ATTR_BKG_ID));
						}
					};

//					List<String> totalSlots = new ArrayList<String>() { // Podría ser cualquier lista
//						{
//							add(BookingGuestDao.ATTR_TOTAL_SLOTS);
//						}
//					};

					// Devuelve la capacidad total de todas las habitaciones que forman la reserva
					EntityResult slotsCount = bookingService.bookingSlotsInfoQuery(reserva, new ArrayList<String>());
					
					Long totalS=0L;
					
					// Recoge la capacidad de total de la reserva
					if (slotsCount.calculateRecordNumber() > 0) {
						totalS = (Long) slotsCount.getRecordValues(0).get(BookingGuestDao.ATTR_TOTAL_SLOTS);
					} else {
						// Si no hay ninguna habitación asociada, será cero
						totalS = 0L;
					}

					// Si el número de huéspedes que ya están asignados a las habitaciones de la
					// reserva es inferior a la capacidad de la reserva, se podrán añadir más
					if ((totalG < totalS) || (totalG==0 && totalS==0)) {
						resultado = this.daoHelper.insert(this.bookingGuestDao, attrMap);
					} else {
						resultado = new EntityResultWrong(ErrorMessage.BOOKING_COMPLETED_NO_MORE_GUESTS_ALLOWED);
					}

				} else {
					resultado = new EntityResultWrong(ErrorMessage.GUEST_MUST_BE_AN_INDIVIDUAL);
				}

			} else {
				resultado = new EntityResultWrong(ErrorMessage.NO_GUEST_IN_NOT_CONFIRMED_BOOKING);
			}

		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(ErrorMessage.NO_BOOKING_ID);
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.NO_CUSTOMER_ID);
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult bookingGuestDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(BookingGuestDao.fields);
			List<String> required = Arrays.asList(BookingGuestDao.ATTR_ID);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(BookingGuestDao.ATTR_ID, keyMap.get(BookingGuestDao.ATTR_ID));

				}
			};
			
			EntityResult auxEntity = bookingGuestQuery(consultaKeyMap,
					EntityResultTools.attributes(BookingGuestDao.ATTR_ID, BookingGuestDao.ATTR_BKG_ID));
			if (auxEntity.calculateRecordNumber() == 0) {
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				
				if (bookingService.getBookingStatus(auxEntity.getRecordValues(0).get(BookingGuestDao.ATTR_BKG_ID))
						.equals(BookingDao.Status.CONFIRMED)) {

					resultado = this.daoHelper.delete(this.bookingGuestDao, keyMap);
					resultado.setMessage("Asociación de Reserva y huésped borrado");

				} else {
					resultado = new EntityResultWrong("Sólo se puede borrar huésped antes del Check_in");

				}
			}
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}
}
