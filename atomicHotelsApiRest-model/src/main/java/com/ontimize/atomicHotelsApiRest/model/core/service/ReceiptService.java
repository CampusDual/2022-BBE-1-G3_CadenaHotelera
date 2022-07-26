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
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingColumnsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IReceiptService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
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
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private BookingServiceExtraService bookingServiceExtraService;

	@Override
	public EntityResult receiptQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();

		try {

			ControlFields cf = new ControlFields();
			cf.addBasics(ReceiptDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.receiptDao, keyMap, attrList);
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult completeReceiptQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult reciboCompleto = new EntityResultWrong();

		try {

			// Filtro
			List<String> required = new ArrayList<String>() {
				{
					add(ReceiptDao.ATTR_ID);
				}
			};
			ControlFields cf = new ControlFields();
			cf.addBasics(ReceiptDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			// Dá igual qué se escriba en el attrList ya que siempre se devuelve todo
			List<String> lista = new ArrayList<String>();
			lista.add(ReceiptDao.ATTR_BOOKING_ID);
			lista.add(ReceiptDao.ATTR_DATE);
			lista.add(ReceiptDao.ATTR_DIAS);
			lista.add(ReceiptDao.ATTR_TOTAL_ROOM);
			lista.add(ReceiptDao.ATTR_TOTAL_SERVICES);
			lista.add(ReceiptDao.ATTR_TOTAL);

			EntityResult reciboSimple = this.daoHelper.query(this.receiptDao, keyMap, lista);

			Map<String, Object> calculoReceipt = reciboSimple.getRecordValues(0);

			// Datos Servicios extras
			Map<String, Object> keyMapServciosExtra = new HashMap<String, Object>();
			keyMapServciosExtra.put(BookingServiceExtraDao.ATTR_ID_BKG,
					reciboSimple.getRecordValues(0).get(ReceiptDao.ATTR_BOOKING_ID));

			List<String> listaServiciosExtra = new ArrayList<String>();
			listaServiciosExtra.add(ServicesXtraDao.ATTR_NAME);
			listaServiciosExtra.add(ServicesXtraDao.ATTR_DESCRIPTION);
			listaServiciosExtra.add(BookingServiceExtraDao.ATTR_ID_UNITS);
			listaServiciosExtra.add(BookingServiceExtraDao.ATTR_PRECIO);
			listaServiciosExtra.add(BookingServiceExtraDao.ATTR_DATE);

			// El resultado de esto se añade dentro del resultado de la siguente
			EntityResult serviciosExtra = bookingServiceExtraService
					.ExtraServicesNameDescriptionUnitsPriceDateQuery(keyMapServciosExtra, listaServiciosExtra);

			List<Object> servicios = new ArrayList<Object>();
			for (int i = 0; i < serviciosExtra.calculateRecordNumber(); i++) {
				Object servicio = serviciosExtra.getRecordValues(i);
				servicios.add(servicio);
			}

			calculoReceipt.put("serviciosExtra", servicios);

			reciboCompleto.addRecord(calculoReceipt);

		} catch (ValidateException e) {
			reciboCompleto = new EntityResultWrong(ErrorMessage.RESULT_REQUIRED + e.getMessage());
		} catch (Exception e) {
			reciboCompleto = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		return reciboCompleto;
	}

	@Override
	public EntityResult receiptInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			ControlFields cf = new ControlFields();
			List<String> required = new ArrayList<String>() {
				{
					add(ReceiptDao.ATTR_BOOKING_ID);

				}
			};
			cf.addBasics(ReceiptDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(attrMap);

			if (bookingService.getBookingStatus(attrMap.get(ReceiptDao.ATTR_BOOKING_ID))
					.equals(BookingDao.Status.COMPLETED)) {

				Object reservaEnRecibo = attrMap.get(ReceiptDao.ATTR_BOOKING_ID);
				Map<String, Object> bkg_id = new HashMap<String, Object>();
				bkg_id.put(BookingDao.ATTR_ID, reservaEnRecibo);

				Map<String, Object> bsx_bkg_id = new HashMap<String, Object>();
				bsx_bkg_id.put(BookingServiceExtraDao.ATTR_ID_BKG, reservaEnRecibo);

				// Cálculo del precio total de la habitación
				List<String> reciboHabitacion = new ArrayList<String>();
				reciboHabitacion.add(BookingDao.ATTR_ID);
				reciboHabitacion.add(RoomTypeDao.ATTR_PRICE);
				reciboHabitacion.add(ReceiptDao.ATTR_DIAS);

				EntityResult habitacion = bookingService.bookingDaysUnitaryRoomPriceQuery(bkg_id, reciboHabitacion);

				int reservaHabitacion = (int) habitacion.getRecordValues(0).get(BookingDao.ATTR_ID);
				BigDecimal precioHabitacion = (BigDecimal) habitacion.getRecordValues(0).get(RoomTypeDao.ATTR_PRICE);
				int dias = (int) habitacion.getRecordValues(0).get(ReceiptDao.ATTR_DIAS);

				BigDecimal totalHabitacion = precioHabitacion.multiply(new BigDecimal(dias));

				attrMap.put(ReceiptDao.ATTR_TOTAL_ROOM, totalHabitacion);
				attrMap.put(ReceiptDao.ATTR_DIAS, dias);

				// Cálculo del precio total de los servcios extras
				List<String> reciboServiciosExtra = new ArrayList<String>();
				reciboServiciosExtra.add(BookingServiceExtraDao.ATTR_ID_BKG);
				reciboServiciosExtra.add(BookingServiceExtraDao.ATTR_PRECIO);
				reciboServiciosExtra.add(BookingServiceExtraDao.ATTR_ID_UNITS);
				reciboServiciosExtra.add("total");

				EntityResult servicios = bookingServiceExtraService.bookingExtraServicePriceUnitsTotalQuery(bsx_bkg_id,
						reciboServiciosExtra);

				BigDecimal totalTodosServiciosExtra = new BigDecimal(0);

				for (int i = 0; i < servicios.calculateRecordNumber(); i++) {

					int reservaServiciosExtra = (int) servicios.getRecordValues(i)
							.get(BookingServiceExtraDao.ATTR_ID_BKG);
					BigDecimal precioServicioExtra = (BigDecimal) servicios.getRecordValues(i)
							.get(BookingServiceExtraDao.ATTR_PRECIO);
					int unidadesServicioExtra = (int) servicios.getRecordValues(i)
							.get(BookingServiceExtraDao.ATTR_ID_UNITS);

					BigDecimal totalServicioExtra = precioServicioExtra.multiply(new BigDecimal(unidadesServicioExtra));
					totalTodosServiciosExtra = totalTodosServiciosExtra.add(totalServicioExtra);

				}

				attrMap.put(ReceiptDao.ATTR_TOTAL_SERVICES, totalTodosServiciosExtra);

				// Total del recibo
				BigDecimal superTotal = totalHabitacion.add(totalTodosServiciosExtra);
				attrMap.put(ReceiptDao.ATTR_TOTAL, superTotal);

				resultado = this.daoHelper.insert(this.receiptDao, attrMap);
				resultado.setMessage("Receipt registrada");

			} else {
				resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR
						+ "- No se puede generar un recibo de una reserva que no está completada");
			}
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());

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

//	@Override
//	public EntityResult receiptUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
//			throws OntimizeJEERuntimeException {
//		EntityResult resultado = new EntityResultWrong();
//		try {
//			ValidateFields.required(keyMap, ReceiptDao.ATTR_ID);
//			resultado = this.daoHelper.update(this.receiptDao, attrMap, keyMap);
//			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
//				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
//			} else {
//				resultado.setMessage("Receipt actualizada");
//			}
//		} catch (MissingFieldsException e) {
//			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + e.getMessage());
//		} catch (DuplicateKeyException e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
//		} catch (DataIntegrityViolationException e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(
//					ErrorMessage.UPDATE_ERROR_MISSING_FK + " / " + ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
//		}
//		return resultado;
//	}

	@Override
	public EntityResult receiptDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(ReceiptDao.ATTR_ID);
				}
			};
			ControlFields cf = new ControlFields();
			cf.addBasics(ReceiptDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> consultaKeyMap = new HashMap<>() {
				{
					put(ReceiptDao.ATTR_ID, keyMap.get(ReceiptDao.ATTR_ID));
				}
			};

			EntityResult auxEntity = receiptQuery(consultaKeyMap, EntityResultTools.attributes(HotelDao.ATTR_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.receiptDao, keyMap);
				resultado.setMessage("Receipt eliminada");
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


}
