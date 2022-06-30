package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.ontimize.atomicHotelsApiRest.model.core.ontimizeExtra.EntityResultWrong;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingService;

import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
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

	@Override
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		if (attrMap.containsKey(BookingDao.ATTR_CHECKIN) && attrMap.containsKey(BookingDao.ATTR_CHECKOUT)) {
			if (((String) attrMap.get(BookingDao.ATTR_CHECKIN))
					.compareTo((String) attrMap.get(BookingDao.ATTR_CHECKOUT)) >= 0) {
				resultado.setCode(EntityResult.OPERATION_WRONG);
				resultado.setMessage("Checkin no puede ser posterior a checkout");
			} else {
				resultado = this.daoHelper.insert(this.bookingDao, attrMap);
			}
		} else {
			resultado.setCode(EntityResult.OPERATION_WRONG);
			resultado.setMessage("Faltan campos necesarios");
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
//TODO ESTO NO FUNCIONA
		if (keyMap.containsKey("range_checkin") && keyMap.containsKey("range_checkout")) {
			BasicField checkin = new BasicField(BookingDao.ATTR_CHECKIN);
			BasicField checkout = new BasicField(BookingDao.ATTR_CHECKOUT);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			System.err.println(keyMap);
			try {
				Date rangeCheckin = dateFormat.parse((String) keyMap.get("range_checkin"));
				Date rangeCheckout = dateFormat.parse((String) keyMap.get("range_checkout"));
				// BasicField rangeCheckout = new BasicField(((String)
				// keyMap.get("range_checkout")));

				/*
				 * (range_checkin >= bkg_checkin AND range_chekout <= bkg_checkout) OR
				 * (range_checkin >= bkg_checkin AND range_chekin < bkg_checkout) OR
				 * (range_checkout <= bkg_checkout AND range_chekout > bkg_checkin)
				 */
				BasicExpression exp01 = new BasicExpression(rangeCheckin, BasicOperator.MORE_EQUAL_OP, checkin);
				BasicExpression exp02 = new BasicExpression(rangeCheckout, BasicOperator.LESS_EQUAL_OP, checkout);
				BasicExpression exp03 = new BasicExpression(rangeCheckin, BasicOperator.LESS_OP, checkout);
				BasicExpression exp04 = new BasicExpression(rangeCheckout, BasicOperator.MORE_OP, checkin);

				BasicExpression groupExp01 = new BasicExpression(exp01, BasicOperator.AND_OP, exp02);
				BasicExpression groupExp02 = new BasicExpression(exp01, BasicOperator.AND_OP, exp03);
				BasicExpression groupExp03 = new BasicExpression(exp02, BasicOperator.AND_OP, exp04);

				BasicExpression auxfinalExp = new BasicExpression(groupExp01, BasicOperator.OR_OP, groupExp02);
				BasicExpression finalExp = new BasicExpression(auxfinalExp, BasicOperator.OR_OP, groupExp03);
				keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, finalExp);
				keyMap.remove("range_checkin");
				keyMap.remove("range_checkout");
				System.err.println(keyMap);
				System.err.println(attrList);
				System.err.println("finalExp: " + finalExp.toString());
				
				resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			resultado = new EntityResultWrong("Faltan campos necesarios, range_checkin o range_checkout");
		}
		return resultado;
	}

}