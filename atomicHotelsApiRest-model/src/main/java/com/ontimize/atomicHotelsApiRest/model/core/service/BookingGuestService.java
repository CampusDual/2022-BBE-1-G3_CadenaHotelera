package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		
		return resultado;
	}

	public EntityResult bookingGuestInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			
			cf.reset();
			List<String> required = new ArrayList<String>() {
				{
					add(BookingGuestDao.ATTR_BKG_ID);
					add(BookingGuestDao.ATTR_CST_ID);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(BookingGuestDao.ATTR_ID);
					add(BookingGuestDao.ATTR_REGISTRATION_DATE);
				}
			};

			cf.addBasics(BookingGuestDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.setOptional(false);
			cf.validate(attrMap);
			
			if (bookingService.getBookingStatus(attrMap.get(BookingGuestDao.ATTR_BKG_ID))
					.equals(BookingDao.Status.CONFIRMED)) {
				
				resultado = this.daoHelper.insert(this.bookingGuestDao, attrMap);
			}else {
				resultado = new EntityResultWrong(ErrorMessage.NO_GUEST_IN_NOT_CONFIRMED_BOOKING);
			}
			
			
		
		
		}catch(ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		}catch(Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	public EntityResult bookingGuestDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		resultado = this.daoHelper.delete(this.bookingGuestDao, keyMap);
		return resultado;
	}

}
