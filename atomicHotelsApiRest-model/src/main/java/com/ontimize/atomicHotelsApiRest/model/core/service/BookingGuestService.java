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
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
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
		
		}catch(ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		
		return resultado;
	}
	
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

			//Poner todas las subconsultas que no aceptan attrList asi!!
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

	@Override
	public EntityResult bookingGuestInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			List<String> required =  Arrays.asList(BookingGuestDao.ATTR_BKG_ID,BookingGuestDao.ATTR_CST_ID);
			List<String> restricted = Arrays.asList(BookingGuestDao.ATTR_ID,BookingGuestDao.ATTR_REGISTRATION_DATE);
			cf.addBasics(BookingGuestDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.setOptional(false);
			cf.validate(attrMap);

			if (bookingService.getBookingStatus(attrMap.get(BookingGuestDao.ATTR_BKG_ID))
					.equals(BookingDao.Status.CONFIRMED)) {
				
				List<String> listaCualquiera = new ArrayList<String>();

				Map<String, Object> reservaGuest = new HashMap<String, Object>() {
					{
						put(BookingGuestDao.ATTR_BKG_ID, attrMap.get(BookingGuestDao.ATTR_BKG_ID));
					}
				};
				
				//Devuelve el atributo total_guests indepnedientemente de lo que se introduzca, por se le pasa una lista cualquiera
				EntityResult guestCount = this.guestCountQuery(reservaGuest, listaCualquiera);
				
				long totalG = (long) guestCount.getRecordValues(0).get(BookingGuestDao.ATTR_TOTAL_GUESTS);

				Map<String, Object> reserva = new HashMap<String, Object>() {
					{
						put(BookingDao.ATTR_ID, attrMap.get(BookingGuestDao.ATTR_BKG_ID));
					}
				};

				List<String> totalSlots = new ArrayList<String>() {
					{
						add(BookingGuestDao.ATTR_TOTAL_SLOTS);
					}
				};

				EntityResult slotsCount = bookingService.bookingSlotsInfoQuery(reserva, totalSlots);

				long totalS = (long) slotsCount.getRecordValues(0).get(BookingGuestDao.ATTR_TOTAL_SLOTS);

				if (totalG < totalS) {
					resultado = this.daoHelper.insert(this.bookingGuestDao, attrMap);
				} else {
					resultado = new EntityResultWrong("La reserva está completa. No admite más huéspedes");
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

		resultado = this.daoHelper.delete(this.bookingGuestDao, keyMap);
		return resultado;
	}

}
