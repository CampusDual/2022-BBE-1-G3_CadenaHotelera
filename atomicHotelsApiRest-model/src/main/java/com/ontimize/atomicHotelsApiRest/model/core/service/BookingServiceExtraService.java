package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingServiceExtraService")
@Lazy
public class BookingServiceExtraService implements IBookingServiceExtraService {

	@Autowired
	private BookingServiceExtraDao bookingServiceExtraDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private HotelServiceExtraService hotelServiceExtraService;
	
	@Autowired
	ControlFields cf;

	public EntityResult bookingServiceExtraQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();
			cf.addBasics(BookingServiceExtraDao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.bookingServiceExtraDao, keyMap, attrList);

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	@Override
	// Registra el servicio si la reserva esta activa en progreso.
	public EntityResult bookingServiceExtraInsert(Map<String, Object> attrMap)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			List<String> required = Arrays.asList(BookingServiceExtraDao.ATTR_ID_SXT,BookingServiceExtraDao.ATTR_ID_BKG,BookingServiceExtraDao.ATTR_ID_UNITS);
			cf.addBasics(BookingServiceExtraDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(attrMap);

			if (bookingService.getBookingStatus(attrMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.CANCELED)) {

				resultado = new EntityResultWrong("La reserva esta cancelada, no se pueden añadir servicios extra.");

			} else if (bookingService.getBookingStatus(attrMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.COMPLETED)) {

				resultado = new EntityResultWrong("La reserva esta completada, no se pueden añadir servicios extra");

			} else if (bookingService.getBookingStatus(attrMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.CONFIRMED)) {

				resultado = new EntityResultWrong("La reserva esta confirmada, no se pueden añadir servicios extra.");

			} else if (bookingService.getBookingStatus(attrMap.get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.IN_PROGRESS)) {

				// Hotel de la reserva
				Map<String, Object> keyMapReserva = new HashMap<String, Object>() {
					{
						put(BookingDao.ATTR_ID, attrMap.get(BookingServiceExtraDao.ATTR_ID_BKG));
					}
				};

				List<String> listaRefrenciaHotel = new ArrayList<String>() {
					{
						add(HotelDao.ATTR_ID);
					}
				};
				EntityResult hotel = bookingService.bookingsHotelsQuery(keyMapReserva, listaRefrenciaHotel);

				// Precio del servicio
				Map<String, Object> keyMapServicioDelHotel = new HashMap<String, Object>() {
					{
						put(HotelServiceExtraDao.ATTR_ID_SXT, attrMap.get(BookingServiceExtraDao.ATTR_ID_SXT));
						put(HotelServiceExtraDao.ATTR_ID_HTL, hotel.getRecordValues(0).get(HotelDao.ATTR_ID));
					}
				};
				List<String> listaPrecioServicio = new ArrayList<String>() {
					{
						add(HotelServiceExtraDao.ATTR_PRECIO);
						add(HotelServiceExtraDao.ATTR_ID_SXT);
					}
				};

				// El precio hay que ir a buscarlo a la tabla hotels_services_extra
				EntityResult precio = hotelServiceExtraService.hotelServiceExtraQuery(keyMapServicioDelHotel,
						listaPrecioServicio);

				// Si da un resultado vacío es que el servcio no está asociado al hotel de la reserva
				if (precio.getRecordValues(0).get(HotelServiceExtraDao.ATTR_ID_SXT) != null) {

//					EntityResult precio = this.daoHelper.query(bookingServiceExtraDao, EntityResultTools.keysvalues(BookingServiceExtraDao.ATTR_ID_BKG,attrMap.get(BookingServiceExtraDao.ATTR_ID_BKG)), 
//							EntityResultTools.attributes(BookingServiceExtraDao.ATTR_PRECIO));

					attrMap.put(BookingServiceExtraDao.ATTR_PRECIO,
							precio.getRecordValues(0).get(HotelServiceExtraDao.ATTR_PRECIO));
					System.err.println(attrMap.entrySet());
					resultado = this.daoHelper.insert(this.bookingServiceExtraDao, attrMap); // en progreso añadimos
																								// servicio extra
					System.err.println(resultado);
					resultado.setMessage("Servicio extra registrado");

				} else {
					resultado = new EntityResultWrong("El servcio solicitado no está en el hotel de la reserva");
				}
			}
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(ErrorMessage.RESULT_REQUIRED + " " + ErrorMessage.NO_BOOKING_ID);
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
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
	 * Se elimina servicio extra de BOOKING IN PROGRESS las otros estados no deja eliminarlo, si no esta o esta eliminado
	 * se muestra mensaje de servicio 
	 */

	@SuppressWarnings("unchecked")
	@Override
	public EntityResult bookingServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {

				cf.reset();
				List<String> required = Arrays.asList(BookingServiceExtraDao.ATTR_ID);
				cf.addBasics(BookingServiceExtraDao.fields);
				cf.setRequired(required);
				cf.setOptional(false);
				cf.validate(keyMap);
		
			
			List<String> filter = Arrays.asList(bookingServiceExtraDao.ATTR_ID_BKG);
			EntityResult registro = this.daoHelper.query(this.bookingServiceExtraDao, keyMap, filter);
			// Si da un resultado vacío es que el servcio no está asociado al hotel de la reserva
			if (bookingService.getBookingStatus(registro.getRecordValues(0).get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.CANCELED)) {
				resultado = new EntityResultWrong("La reserva de este servicio esta cancelada no se pueden eliminar.");
			} else if (bookingService.getBookingStatus(registro.getRecordValues(0).get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.COMPLETED)) {
				resultado = new EntityResultWrong("La reserva de este servicio esta completada no se pueden eliminar.");
			} else if (bookingService.getBookingStatus(registro.getRecordValues(0).get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.IN_PROGRESS)) {
				System.err.println("eliminando");
				resultado = this.daoHelper.delete(this.bookingServiceExtraDao, keyMap); // eliminamos servicio extra.
				resultado.setMessage(" Servicio " + keyMap.get(BookingServiceExtraDao.ATTR_ID) + "  Extra eliminado.");
			} else if (bookingService.getBookingStatus(registro.getRecordValues(0).get(bookingServiceExtraDao.ATTR_ID_BKG))
					.equals(BookingDao.Status.CONFIRMED)) {
				resultado = new EntityResultWrong("La reserva de este servicio esta confirmada no se pueden eliminar.");
			} 
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			//nullable no existe.
			resultado = new EntityResultWrong("El servicio no existe pruebe con otro - "+ErrorMessage.DELETE_ERROR);
		}
		return resultado;
	}


	@Override
	/**
	 * Dado un bsx_bkg_id devuelve los servcios extra de esa reserva, con sus
	 * precios, las unidades y el total de cada registro
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
			List<String> required = Arrays.asList(BookingServiceExtraDao.ATTR_ID_BKG);
			cf.reset();
			cf.addBasics(BookingServiceExtraDao.fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			resultado = this.daoHelper.query(this.bookingServiceExtraDao, keyMap, attrList,
					"queryServciosExtraPrecioUnidadesTotal");
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		}catch(Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}
	


	@Override
	/**
	 * Dado un bsx_bkg_id devuelve los servcios extra de la reserva (nombre,
	 * descripcion, unidades, precio y fecha)
	 * 
	 * @param keyMap
	 * @param attrList
	 * @return EntityResult
	 * @throws OntimizeJEERuntimeException
	 */
	public EntityResult ExtraServicesNameDescriptionUnitsPriceDateQuery(Map<String, Object> keyMap,
			List<String> attrList) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();

		try {
			List<String> required = Arrays.asList(BookingServiceExtraDao.ATTR_ID_BKG);
			cf.reset();
			cf.addBasics(BookingServiceExtraDao.fields);
			cf.addBasics(ServicesXtraDao.fields);
			cf.setRequired(required);
			cf.validate(keyMap);
			
			resultado = this.daoHelper.query(this.bookingServiceExtraDao, keyMap, attrList,
					"queryServiciosExtraNombreDescripcionUnidadesPrecioFecha");
		} catch (ValidateException e) {
			resultado = new EntityResultWrong(e.getMessage());
		}catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

}
