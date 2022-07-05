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
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
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
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
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
		if (attrMap.containsKey(BookingDao.ATTR_CHECKIN) && attrMap.containsKey(BookingDao.ATTR_CHECKOUT)
				&& attrMap.containsKey(BookingDao.ATTR_ROOM_ID)) {
			if (((String) attrMap.get(BookingDao.ATTR_CHECKIN))
					.compareTo((String) attrMap.get(BookingDao.ATTR_CHECKOUT)) >= 0) {
				resultado.setCode(EntityResult.OPERATION_WRONG);
				resultado.setMessage("Checkin no puede ser posterior a checkout");
			} else {

				try {
					if (roomService.isRoomUnbookedgInRangeQuery((String) attrMap.get(BookingDao.ATTR_CHECKIN),
							(String) attrMap.get(BookingDao.ATTR_CHECKOUT),
							(Integer) attrMap.get(BookingDao.ATTR_ID))) {
						resultado = this.daoHelper.insert(this.bookingDao, attrMap);
					} else {
						resultado = new EntityResultWrong("La habitación ya está reservada en esa franja de fechas.");
					}
				} catch (EntityResultRequiredException e) {
					resultado = new EntityResultWrong("Error al realizar consulta dependiente");
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
		BasicExpression notCanceled = new BasicExpression(new BasicField(BookingDao.ATTR_STATUS_ID),BasicOperator.NOT_EQUAL_OP, BookingDao.STATUS_CANCELED);
		//todo no funciona basic expression, pero el sql si
		//update bookings SET bkg_stb_id = 4 Where bkg_id = 1 AND bkg_stb_id != 3;
		EntityResultExtraTools.putBasicExpression(keyMap, notCanceled);
//		System.err.println(keyMap);
		return this.daoHelper.update(this.bookingDao, attrMap, keyMap);
	}

	@Override
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.bookingDao, keyMap);
	}

	@Override
	public EntityResult bookingsInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			bookingsInRangeBuilder(keyMap, attrList);
			return this.daoHelper.query(this.bookingDao, keyMap, attrList);
		} catch (MissingFieldsException e) {
			e.printStackTrace();
			return new EntityResultWrong("Faltan campos necesarios");
		}
	}

	@Override
	public EntityResult bookingsInRangeInfoQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			bookingsInRangeBuilder(keyMap, attrList);
			return this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryInfoBooking");
		} catch (MissingFieldsException e) {
			e.printStackTrace();
			return new EntityResultWrong("Faltan campos necesarios");
		}
	}

	/**
	 * Modifica las keyMap y attrList, para realizar las basic expresionsn
	 * 
	 * @param keyMap
	 * @param attrList
	 * @throws MissingFieldsException
	 */
	public void bookingsInRangeBuilder(Map<String, Object> keyMap, List<String> attrList)
			throws MissingFieldsException {

		if (keyMap.containsKey(BookingDao.NON_ATTR_START_DATE) && keyMap.containsKey(BookingDao.NON_ATTR_END_DATE)) {
			BasicField checkin = new BasicField(BookingDao.ATTR_CHECKIN);
			BasicField checkout = new BasicField(BookingDao.ATTR_CHECKOUT);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date rangeCheckin = dateFormat.parse((String) keyMap.get(BookingDao.NON_ATTR_START_DATE));
				Date rangeCheckout = dateFormat.parse((String) keyMap.get(BookingDao.NON_ATTR_END_DATE));

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
				BasicExpression auxFilterRangeBE = new BasicExpression(groupExp01, BasicOperator.OR_OP, groupExp02);
				BasicExpression filterRangeBE = new BasicExpression(auxFilterRangeBE, BasicOperator.OR_OP, groupExp03);

				EntityResultExtraTools.putBasicExpression(keyMap, filterRangeBE); // nuevo metodo

				keyMap.remove(BookingDao.NON_ATTR_START_DATE);
				keyMap.remove(BookingDao.NON_ATTR_END_DATE);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			throw new MissingFieldsException("Faltan campos necesarios");
		}
	}

}