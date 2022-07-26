package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sound.midi.SysexMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;


@Service("BookingServiceExtraService")
@Lazy
public class BookingServiceExtraService implements IBookingServiceExtraService {

	@Autowired
	private BookingServiceExtraDao bookingServiceExtraDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	private BookingService bookingService;
	

	public EntityResult bookingServiceExtraQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.query(this.bookingServiceExtraDao, keyMap, attrList);
	}

	@SuppressWarnings("unchecked")
	@Override
	// Registra el servicio si la reserva esta activa en progreso.
	public EntityResult bookingServiceExtraInsert(Map<String, Object> attrMap)
			throws OntimizeJEERuntimeException, MissingFieldsException {
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(attrMap, BookingServiceExtraDao.ATTR_ID_SXT, BookingServiceExtraDao.ATTR_ID_BKG,
					BookingServiceExtraDao.ATTR_ID_UNITS);
			ValidateFields.formatprice(attrMap.get(BookingServiceExtraDao.ATTR_PRECIO));
			if(bookingService.getBookingStatus(attrMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.CANCELED)) {
				resultado.setMessage("La reserva esta cancelada, no se pueden añadir servicios extra.");
			}else if(bookingService.getBookingStatus(attrMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.COMPLETED)) {
				resultado.setMessage("La reserva esta completada, no se pueden añadir servicios extra");
			}else if(bookingService.getBookingStatus(attrMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.IN_PROGRESS)) {
				System.err.println("hola");
				resultado = this.daoHelper.insert(this.bookingServiceExtraDao, attrMap);// en progreso añadimos servicio extra
				System.err.println(resultado);
				resultado.setMessage("Servicio extra registrado");
			}else if(bookingService.getBookingStatus(attrMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.IN_PROGRESS)) {
				resultado.setMessage("La reserva esta confirmada, no se pueden añadir servicios extra.");
			}
		} catch (NumberFormatException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR);
		}
		return resultado;
	}


	/*
	 * Se puede eliminar servicios extra de reservas en progreso.
	 * en el json introducir bsx_bkg_id -> reserva activo
	 * y bsx_id -> número de servicios asignadoa a reserva.
	 */

	@SuppressWarnings("unchecked")
	@Override
	public EntityResult bookingServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
			
		
		
		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, ReceiptDao.ATTR_ID);
			ValidateFields.onlyThis(keyMap, ReceiptDao.ATTR_ID);
			ValidateFields.isInt(keyMap, ReceiptDao.ATTR_ID);

			EntityResult auxEntity = this.daoHelper.query(this.receiptDao,
					EntityResultTools.keysvalues(ReceiptDao.ATTR_ID, keyMap.get(ReceiptDao.ATTR_ID)),
					EntityResultTools.attributes(ReceiptDao.ATTR_ID));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.receiptDao, keyMap);
				resultado.setMessage("Receipt eliminada");
			}
			
			EntityResult resultado = new EntityResultMapImpl();
			try {
				ValidateFields.required(keyMap,BookingServiceExtraDao.ATTR_ID_BKGHSX);
		
				if(bookingService.getBookingStatus(keyMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
						.equals(BookingDao.Status.CANCELED)) {
					resultado.setMessage("La reserva de este servicio esta cancelada no se pueden eliminar.");
				}else if(bookingService.getBookingStatus(keyMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
						.equals(BookingDao.Status.COMPLETED)) {
					resultado.setMessage("La reserva de este servicio esta completada no se se pueden eliminar.");
				}else if(bookingService.getBookingStatus(keyMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
						.equals(BookingDao.Status.IN_PROGRESS)) {
					System.err.println("eliminando");
					resultado = this.daoHelper.delete(this.bookingServiceExtraDao, keyMap); //eliminamos servicio extra.
					resultado.setMessage(" Servicio " + keyMap.get(BookingServiceExtraDao.ATTR_ID_BKGHSX) + "  Extra eliminado.");
				}else if(bookingService.getBookingStatus(keyMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
						.equals(BookingDao.Status.CONFIRMED)) {
					resultado.setMessage("La Reserva esta confirmada no se puede eliminar servicios extra.");
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

	@Override
	/*
	 * RETURNS devuelve las reservas en progreso.
	 */
	public EntityResult bookingInProgressQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bookingServiceExtraDao, keyMap, attrList,"booking_inprocess");
		
	}
	
	
	@Override
	/**
	 * Dado un bsx_bkg_id devuelve los servcios extra de esa reserva, con sus precios, las unidades y el total de cada registro
	 * 
	 * @param keyMap
	 * @param attrList
	 * @return EntityResult
	 * @throws OntimizeJEERuntimeException
	 */
	public EntityResult bookingExtraServicePriceUnitsTotalQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, BookingServiceExtraDao.ATTR_ID_BKG);
			resultado = this.daoHelper.query(this.bookingServiceExtraDao, keyMap, attrList,
					"queryServciosExtraPrecioUnidadesTotal");
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.RESULT_REQUIRED+e.getMessage());
		}
		return resultado;
	}
	
	@Override
	/**
	 * Dado un bsx_bkg_id devuelve los servcios extra de la reserva (nombre, descripcion, unidades, precio y fecha)
	 * 
	 * @param keyMap
	 * @param attrList
	 * @return EntityResult
	 * @throws OntimizeJEERuntimeException
	 */
	public EntityResult ExtraServicesNameDescriptionUnitsPriceDateQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		
		try {
			ValidateFields.required(keyMap, BookingServiceExtraDao.ATTR_ID_BKG);
			resultado = this.daoHelper.query(this.bookingServiceExtraDao, keyMap, attrList,
					"queryServiciosExtraNombreDescripcionUnidadesPrecioFecha");
		} catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.RESULT_REQUIRED+e.getMessage());
		}
		return resultado;
	}
	
	

}
