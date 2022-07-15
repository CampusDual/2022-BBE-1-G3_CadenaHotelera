package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.swing.text.Keymap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("ReceiptService")
@Lazy
public class ReceiptService implements IReceiptService {

	@Autowired
	private ReceiptDao receiptDao;

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private BookingService bookingService;

	@Override
	public EntityResult bookingExtraServicePriceAundUnitsQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, BookingDao.ATTR_ID);
			resultado = this.daoHelper.query(this.bookingDao, keyMap, attrList, "queryServciosExtraPrecioUnidades");
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(e.getMessage());
		}
		return resultado;
	}

	@Override
	public EntityResult receiptQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = this.daoHelper.query(this.receiptDao, keyMap, attrList);
		return resultado;
	}

	@Override
	public EntityResult receiptInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			// TODO restringir que meta solo ese y ninguno más
			ValidateFields.required(attrMap, ReceiptDao.ATTR_BOOKING_ID);

			if (bookingService.getBookingStatus(attrMap.get(ReceiptDao.ATTR_BOOKING_ID))
					.equals(BookingDao.Status.COMPLETED)) {

				Object a = attrMap.get(ReceiptDao.ATTR_BOOKING_ID);
				Map<String, Object> idBooking = new HashMap<String, Object>();
				idBooking.put(BookingDao.ATTR_ID, a);

				// Cálculo del precio total de la habitación
				List<String> lista = new ArrayList<String>();
				lista.add(BookingDao.ATTR_ID);
				lista.add(RoomTypeDao.ATTR_PRICE);
				lista.add(ReceiptDao.ATTR_DIAS);

				EntityResult habitacion = bookingService.bookingDaysUnitaryRoomPriceQuery(idBooking, lista);

				int reserva = (int) habitacion.getRecordValues(0).get(BookingDao.ATTR_ID);
				BigDecimal precio = (BigDecimal) habitacion.getRecordValues(0).get(RoomTypeDao.ATTR_PRICE);
				int dias = (int) habitacion.getRecordValues(0).get(ReceiptDao.ATTR_DIAS);

				BigDecimal total = precio.multiply(new BigDecimal(dias));

				attrMap.put(ReceiptDao.ATTR_TOTAL_ROOM, total);
				attrMap.put(ReceiptDao.ATTR_DIAS, dias);

				// Cálculo del precio total de los servcios extras
				List<String> lista2 = new ArrayList<String>();
				lista.add(BookingDao.ATTR_ID);
				lista.add(BookingServiceExtraDao.ATTR_PRECIO);
				lista.add(BookingServiceExtraDao.ATTR_ID_UNITS);

				EntityResult servicios = bookingExtraServicePriceAundUnitsQuery(idBooking, lista2);

				int reserva2 = (int) servicios.getRecordValues(0).get(BookingDao.ATTR_ID);
				BigDecimal precio2 = (BigDecimal) servicios.getRecordValues(0).get(BookingServiceExtraDao.ATTR_PRECIO);
				int dias2 = (int) servicios.getRecordValues(0).get(BookingServiceExtraDao.ATTR_ID_UNITS);

				BigDecimal total2 = precio2.multiply(new BigDecimal(dias2));
				
				attrMap.put(ReceiptDao.ATTR_TOTAL_SERVICES, total2);

				resultado = this.daoHelper.insert(this.receiptDao, attrMap);
				resultado.setMessage("Receipt registrada");

			} else {
				resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR
						+ "- No se puede generar un recibo de una reserva que no está completada");
			}
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + "-" + e.getMessage());

		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + "-" + e.getMessage());

		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);

		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult receiptUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, ReceiptDao.ATTR_ID);
			resultado = this.daoHelper.update(this.receiptDao, attrMap, keyMap);
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Receipt actualizada");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(
					ErrorMessage.UPDATE_ERROR_MISSING_FK + " / " + ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult receiptDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, ReceiptDao.ATTR_ID);

			EntityResult auxEntity = this.daoHelper.query(this.receiptDao,
					EntityResultTools.keysvalues(ReceiptDao.ATTR_ID, keyMap.get(ReceiptDao.ATTR_ID)),
					EntityResultTools.attributes(ReceiptDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.receiptDao, keyMap);
				resultado.setMessage("Receipt eliminada");
			}
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}

//	public BookingDao.Status getBookingStatus(Object bookingId) throws EntityResultRequiredException {
//		Map<String, Object> keyMap = new HashMap<>();
//		keyMap.put(BookingDao.ATTR_ID, bookingId);
//
//		List<String> attrList = new ArrayList<>();
//		attrList.add(BookingDao.ATTR_START);
//		attrList.add(BookingDao.ATTR_END);
//		attrList.add(BookingDao.ATTR_CHECKIN);
//		attrList.add(BookingDao.ATTR_CHECKOUT);
//		attrList.add(BookingDao.ATTR_CANCELED);
//		attrList.add(BookingDao.ATTR_CREATED);
//
//		EntityResult consultaER = this.daoHelper.query(this.bookingDao, keyMap, attrList);
//
//		if (consultaER.calculateRecordNumber() == 1) {
//			if (consultaER.getRecordValues(0).get(BookingDao.ATTR_CANCELED) != null) {
//				return BookingDao.Status.CANCELED;
//			} else if (consultaER.getRecordValues(0).get(BookingDao.ATTR_CHECKOUT) != null) {
//				return BookingDao.Status.COMPLETED;
//			} else if (consultaER.getRecordValues(0).get(BookingDao.ATTR_CHECKIN) != null) {
//				return BookingDao.Status.IN_PROGRESS;
//			} else {
//				return BookingDao.Status.CONFIRMED;
//			}
//		} else {
//			throw new EntityResultRequiredException("Error al consultar estado de la reserva");
//		}

}
