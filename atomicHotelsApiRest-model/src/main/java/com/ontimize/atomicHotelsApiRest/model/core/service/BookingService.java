package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultExtraTools;
import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService;

import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.exceptions.MissingFieldsException;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingService")
@Lazy
public class BookingService implements IBookingService {

	@Autowired
	private BookingDao bookingDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		if (attrMap.containsKey(BookingDao.ATTR_CHECKIN) && attrMap.containsKey(BookingDao.ATTR_CHECKOUT)
				&& attrMap.containsKey(BookingDao.ATTR_ROOM_ID)) {
			if (((String) attrMap.get(BookingDao.ATTR_CHECKIN))
					.compareTo((String) attrMap.get(BookingDao.ATTR_CHECKOUT)) >= 0) {
				resultado.setCode(EntityResult.OPERATION_WRONG);
				resultado.setMessage("Checkin no puede ser posterior a checkout");
			} else {
				try {
					if (isRoomUnbookedgInRangeQuery(attrMap)) {
						resultado = this.daoHelper.insert(this.bookingDao, attrMap);
					} else {
						resultado = new EntityResultWrong("La habitación ya está reservada en esa franja de fechas.");
					}
				} catch (MissingFieldsException e) {
					resultado = new EntityResultWrong("Error al comprobar reservas de la habitación");
					e.printStackTrace();
				}
			}

		} else {
			resultado = new EntityResultWrong("Faltan campos necesarios");
		}
		return resultado;
	}

	@Override
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.bookingDao, attrMap, keyMap);
	}

	@Override
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.bookingDao, keyMap);
	}

	@Override
	public EntityResult bookingsInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		if (keyMap.containsKey(BookingDao.NON_ATTR_START_DATE) && keyMap.containsKey(BookingDao.NON_ATTR_END_DATE)) {
			BasicField checkin = new BasicField(BookingDao.ATTR_CHECKIN);
			BasicField checkout = new BasicField(BookingDao.ATTR_CHECKOUT);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date rangeCheckin = dateFormat.parse((String) keyMap.get(BookingDao.NON_ATTR_START_DATE));
				Date rangeCheckout = dateFormat.parse((String) keyMap.get(BookingDao.NON_ATTR_END_DATE));
				// BasicField rangeCheckout = new BasicField(((String)
				// keyMap.get("range_checkout")));

				/*
				 * (range_checkin >= bkg_checkin AND range_checkin < bkg_checkout) OR
				 * (range_checkout > bkg_checkin AND range_checkout <= bkg_checkout) OR
				 * (range_checkin < bkg_checkin AND range_chekout > bkg_checkout)
				 */

				// (range_checkin >= bkg_checkin AND range_checkin < bkg_checkout) OR
				BasicExpression exp01 = new BasicExpression(checkin, BasicOperator.LESS_EQUAL_OP, rangeCheckin);
				BasicExpression exp02 = new BasicExpression(checkout, BasicOperator.MORE_OP, rangeCheckin);
				BasicExpression groupExp01 = new BasicExpression(exp01, BasicOperator.AND_OP, exp02); // dentro
																										// rangeCheckin

				// (range_checkout > bkg_checkin AND range_checkout <= bkg_checkout) OR
				BasicExpression exp03 = new BasicExpression(checkout, BasicOperator.MORE_EQUAL_OP, rangeCheckout);
				BasicExpression exp04 = new BasicExpression(checkin, BasicOperator.LESS_OP, rangeCheckout);
				BasicExpression groupExp02 = new BasicExpression(exp03, BasicOperator.AND_OP, exp04);// dentro
																										// rangeCheckout

				BasicExpression exp05 = new BasicExpression(checkin, BasicOperator.MORE_OP, rangeCheckin);
				BasicExpression exp06 = new BasicExpression(checkout, BasicOperator.LESS_OP, rangeCheckout);
				BasicExpression groupExp03 = new BasicExpression(exp05, BasicOperator.AND_OP, exp06);// dentro checkin y
																										// checkout

				// las uno
				BasicExpression auxfinalExp = new BasicExpression(groupExp01, BasicOperator.OR_OP, groupExp02);
				BasicExpression finalExp = new BasicExpression(auxfinalExp, BasicOperator.OR_OP, groupExp03);

				keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, finalExp);
				keyMap.remove(BookingDao.NON_ATTR_START_DATE);
				keyMap.remove(BookingDao.NON_ATTR_END_DATE);

				resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			resultado = new EntityResultWrong("Faltan campos necesarios");
		}
		return resultado;
	}

	public EntityResult bookingsInRangeQuery(Object checkin, Object checkout, Object roomId) {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(bookingDao.NON_ATTR_START_DATE, checkin);
		keyMap.put(bookingDao.NON_ATTR_END_DATE, checkin);
		keyMap.put(BookingDao.ATTR_ROOM_ID, roomId);
		return bookingsInRangeQuery(keyMap, EntityResultTools.attributes(BookingDao.ATTR_ROOM_ID));
	}

	/**
	 * Comprueba si una habitación en concreto está vacia.
	 * 
	 * @param keyMap requiere checkin, checkout, room_id. El resto los ignora.
	 * @return True si la room_id está libre en esa franja de fechas.
	 * @throws OntimizeJEERuntimeException
	 * @throws MissingFieldsException
	 */
	public boolean isRoomUnbookedgInRangeQuery(Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException, MissingFieldsException {
		EntityResult resultado = new EntityResultMapImpl();

		if (keyMap.containsKey(BookingDao.ATTR_CHECKIN) && keyMap.containsKey(BookingDao.ATTR_CHECKOUT)
				&& keyMap.containsKey(BookingDao.ATTR_ROOM_ID)) {
			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
			auxKeyMap.put(BookingDao.ATTR_CHECKIN, keyMap.get(BookingDao.ATTR_CHECKIN));
			auxKeyMap.put(BookingDao.ATTR_CHECKOUT, keyMap.get(BookingDao.ATTR_CHECKOUT));
			auxKeyMap.put(BookingDao.ATTR_ROOM_ID, keyMap.get(BookingDao.ATTR_ROOM_ID));
			resultado = bookingsInRangeQuery(keyMap.get(BookingDao.ATTR_CHECKIN), keyMap.get(BookingDao.ATTR_CHECKOUT),
					keyMap.get(BookingDao.ATTR_ROOM_ID));
		} else {
			throw new MissingFieldsException(null);
		}
		if (resultado.isWrong()) {
			return false;
		}
		return resultado.calculateRecordNumber() == 0;

	}
}