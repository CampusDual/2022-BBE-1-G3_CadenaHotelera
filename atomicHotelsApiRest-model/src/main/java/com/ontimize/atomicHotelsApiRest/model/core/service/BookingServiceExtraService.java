package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;

import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
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

	@Override
	public EntityResult bookingServiceExtraInsert(Map<String, Object> attrMap)
			throws OntimizeJEERuntimeException, MissingFieldsException {

		EntityResult resultado = new EntityResultMapImpl();
		try {

				ValidateFields.required(attrMap,BookingServiceExtraDao.ATTR_ID_SXT,BookingServiceExtraDao.ATTR_ID_BKG, BookingServiceExtraDao.ATTR_ID_UNITS);
				ValidateFields.formatprice(attrMap.get(BookingServiceExtraDao.ATTR_PRECIO));
				resultado = this.daoHelper.insert(this.bookingServiceExtraDao, attrMap);
				resultado.setMessage("Reserva (booking) ServiceExtra registrado");
				
		}
		catch(NumberFormatException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR  +e.getMessage());
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
 * * Comprobar que un recibo ha sido o no facturado.
 */
/*	@Override
	public EntityResult bookingServiceExtraUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultMapImpl();
		try {
			
			ValidateFields.required(keyMap, BookingServiceExtraDao.ATTR_ID_BKGHSX);
			ValidateFields.formatprice(attrMap.get(BookingServiceExtraDao.ATTR_PRECIO));
			resultado = this.daoHelper.update(this.bookingServiceExtraDao, attrMap, keyMap);;
			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
					resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado.setMessage("Service Extra actualizado");
				}
		}
		catch(NumberFormatException e) {
				resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR  +e.getMessage());
		}catch (MissingFieldsException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {//Puede ser que se meta una FK que no exista o se le ponga null al precio cuando no se debería permitir
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FK+" / "+ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR);
		}
		return resultado;
	}*/

	@Override
	public EntityResult bookingServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			ValidateFields.required(keyMap, BookingServiceExtraDao.ATTR_ID_BKGHSX);
			EntityResult auxEntity = this.daoHelper.query(this.bookingServiceExtraDao,
					EntityResultTools.keysvalues(BookingServiceExtraDao.ATTR_ID_BKGHSX, keyMap.get(BookingServiceExtraDao.ATTR_ID_BKGHSX)),
					EntityResultTools.attributes(BookingServiceExtraDao.ATTR_ID_BKGHSX));
			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.bookingServiceExtraDao, keyMap);
				resultado.setMessage(" Servicio "+keyMap.get(BookingServiceExtraDao.ATTR_ID_BKGHSX) + "  Extra eliminado.");
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
	
	

}
