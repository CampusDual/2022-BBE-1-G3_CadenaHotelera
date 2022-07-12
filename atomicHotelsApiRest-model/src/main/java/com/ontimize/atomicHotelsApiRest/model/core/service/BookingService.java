package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.awt.print.Book;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultExtraTools;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
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

	@Autowired
	IRoomService roomService;

	@Override
	public EntityResult bookingQuery(Map<Object, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bookingDao, keyMap, attrList);
	}

	@Override
	public EntityResult bookingInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryInfoBooking");
	}

	@Override
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(attrMap, BookingDao.ATTR_START, BookingDao.ATTR_END, BookingDao.ATTR_ROOM_ID,
					BookingDao.ATTR_CUSTOMER_ID);
			ValidateFields.restricted(attrMap, BookingDao.ATTR_CHECKIN, BookingDao.ATTR_CHECKOUT,
					BookingDao.ATTR_CANCELED, BookingDao.ATTR_CREATED);

			int validateRange = ValidateFields.dataRange(attrMap.get(BookingDao.ATTR_START),
					attrMap.get(BookingDao.ATTR_END));
			switch (validateRange) {
			case 1:
				resultado = new EntityResultWrong("Checkin no puede ser posterior a checkout");
				break;
			case 2:
				resultado = new EntityResultWrong("Checkin no puede ser anterior a hoy");
				break;

			case 0:
				if (roomService.isRoomUnbookedgInRangeQuery((String) attrMap.get(BookingDao.ATTR_CHECKIN),
						(String) attrMap.get(BookingDao.ATTR_CHECKOUT),
						(Integer) attrMap.get(BookingDao.ATTR_ROOM_ID))) {
					resultado = this.daoHelper.insert(this.bookingDao, attrMap);
				} else {
					resultado = new EntityResultWrong("La habitación ya está reservada en esa franja de fechas.");
				}
			}
		} catch (EntityResultRequiredException | MissingFieldsException | InvalidFieldsValuesException e) {
			System.err.println(e.getMessage());
			resultado = new EntityResultWrong(e.getMessage());
		}

		return resultado;
	}

	@Override
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		try {
			ValidateFields.required(keyMap, BookingDao.ATTR_ID);
			ValidateFields.restricted(attrMap, BookingDao.ATTR_CHECKIN, BookingDao.ATTR_CHECKOUT,
					BookingDao.ATTR_CANCELED, BookingDao.ATTR_CREATED);
			EntityResult subConsulta = bookingQuery(
					EntityResultTools.keysvalues(BookingDao.ATTR_ID, keyMap.get(BookingDao.ATTR_ID)),
					EntityResultTools.attributes(BookingDao.ATTR_CANCELED, BookingDao.ATTR_CHECKOUT));
			if ((subConsulta.get(BookingDao.ATTR_CANCELED) == null)
					&& (subConsulta.get(BookingDao.ATTR_CHECKOUT) == null)) {
				return this.daoHelper.update(this.bookingDao, attrMap, keyMap);
			} else {
				return new EntityResultWrong("No se pueden modificar reservas canceladas o finalizadas");
			}
		} catch (MissingFieldsException e) {
			return new EntityResultWrong(e.getMessage());
		}
	}

	@Override
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
//		return this.daoHelper.delete(this.bookingDao, keyMap);
		return new EntityResultWrong("No se pueden eliminar reservas, debe cancelarla");
	}

	@Override
	public EntityResult bookingsInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException, InvalidFieldsValuesException {
		try {
			bookingsInRangeBuilder(keyMap, attrList);
			return this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryBasicBooking");
		} catch (MissingFieldsException | InvalidFieldsValuesException e) {
			System.err.println(e.getMessage());
			return new EntityResultWrong(e.getMessage());
		}
	}

	@Override
	public EntityResult bookingsInRangeInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {

			bookingsInRangeBuilder(keyMap, attrList);
			return this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryInfoBooking");
		} catch (MissingFieldsException | InvalidFieldsValuesException e) {
			System.err.println(e.getMessage());
			return new EntityResultWrong(e.getMessage());
		}
	}

	/**
	 * Modifica las keyMap y attrList, para realizar las basic expresionsn
	 * 
	 * @param keyMap
	 * @param attrList
	 * @throws MissingFieldsException
	 * @throws InvalidFieldsValuesException
	 */
	public void bookingsInRangeBuilder(Map<String, Object> keyMap, List<String> attrList)
			throws MissingFieldsException, InvalidFieldsValuesException {
		ValidateFields.required(keyMap, BookingDao.ATTR_START, BookingDao.ATTR_END);

		Date rangeStart = ValidateFields.stringToDate((String) keyMap.get(BookingDao.ATTR_START));
		Date rangeEnd = ValidateFields.stringToDate((String) keyMap.get(BookingDao.ATTR_END));
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

}