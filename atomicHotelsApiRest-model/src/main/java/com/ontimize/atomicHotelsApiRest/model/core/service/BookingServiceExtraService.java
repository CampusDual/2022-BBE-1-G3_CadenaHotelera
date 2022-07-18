package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;

import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;


@Service("BookingServiceExtraService")
@Lazy
public class BookingServiceExtraService implements IBookingServiceExtraService {

	@Autowired
	private BookingServiceExtraDao bookingServiceExtraDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

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
			List<String> columns = Arrays.asList("bsx_id");
			EntityResult inProgress = bookingInProgressQuery(null, columns);

			if (((List<Integer>) inProgress.get(BookingDao.ATTR_ID))
					.contains(attrMap.get(BookingServiceExtraDao.ATTR_ID_BKG))) {
				resultado = this.daoHelper.insert(this.bookingServiceExtraDao, attrMap);
				resultado.setMessage(
						"Reserva " + attrMap.get(BookingServiceExtraDao.ATTR_ID_BKG) + " Service Extra registrado");
			} else {
				resultado.setMessage("Esta reserva no es valida para registar el servicio");
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
			ValidateFields.required(keyMap, BookingServiceExtraDao.ATTR_ID_BKGHSX, BookingServiceExtraDao.ATTR_ID_BKG);
			List<String> columns = Arrays.asList("bsx_id");
			EntityResult inProgress = bookingInProgressQuery(null, columns);

			if (((List<Integer>) inProgress.get(BookingDao.ATTR_ID)).contains(keyMap.get(BookingServiceExtraDao.ATTR_ID_BKG))) {
				EntityResult auxEntity = this.daoHelper.query(this.bookingServiceExtraDao, EntityResultTools.keysvalues(BookingServiceExtraDao.ATTR_ID_BKGHSX, keyMap.get(BookingServiceExtraDao.ATTR_ID_BKGHSX)),
						EntityResultTools.attributes(BookingServiceExtraDao.ATTR_ID_BKGHSX));
				if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
					resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
				} else {
					resultado = this.daoHelper.delete(this.bookingServiceExtraDao, keyMap);
					resultado.setMessage(
							" Servicio " + keyMap.get(BookingServiceExtraDao.ATTR_ID_BKGHSX) + "  Extra eliminado.");
				}
			}else {
				resultado.setMessage("Reserva no esta activa el servicio no se puede eliminar.");
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
