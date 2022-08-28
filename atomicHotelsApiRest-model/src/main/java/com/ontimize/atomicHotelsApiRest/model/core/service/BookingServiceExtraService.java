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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingGuestDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ServicesXtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingServiceExtraService")
@Lazy
public class BookingServiceExtraService implements IBookingServiceExtraService {

	@Autowired
	private BookingServiceExtraDao dao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private HotelServiceExtraService hotelServiceExtraService;

	@Autowired
	ControlFields cf;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingServiceExtraQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			cf.reset();

			
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);

			cf.addBasics(dao.fields);
			cf.validate(keyMap);

			cf.validate(attrList);

			resultado = this.daoHelper.query(this.dao, keyMap, attrList);

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	// Registra el servicio si la reserva esta activa en progreso.
	public EntityResult bookingServiceExtraInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			List<String> required = Arrays.asList(dao.ATTR_ID_SXT,
					dao.ATTR_ID_BKG, dao.ATTR_ID_UNITS);
			List<String> restricted = Arrays.asList(dao.ATTR_ID, dao.ATTR_PRECIO);
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.setOptional(false);
			cf.validate(attrMap);

			
			Map<String, Object> consultaBookingStatus = new HashMap<String, Object>() {
				{
					put(BookingDao.ATTR_ID, attrMap.get(dao.ATTR_ID_BKG));
				}
			};
			cf.reset();
			
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER, UserRoleDao.ROLE_STAFF);
			cf.addBasics(BookingDao.fields);
			cf.validate(consultaBookingStatus);
			
//Todo refactorizar esto a un switch... hay un ejemplo en booking
			if (bookingService.getBookingStatus(consultaBookingStatus)
					.equals(BookingDao.Status.CANCELED)) {

				resultado = new EntityResultWrong("La reserva esta cancelada, no se pueden añadir servicios extra.");

			} else if (bookingService.getBookingStatus(consultaBookingStatus)
					.equals(BookingDao.Status.COMPLETED)) {

				resultado = new EntityResultWrong("La reserva esta completada, no se pueden añadir servicios extra");

			} else if (bookingService.getBookingStatus(consultaBookingStatus)
					.equals(BookingDao.Status.CONFIRMED)) {

				resultado = new EntityResultWrong("La reserva esta confirmada, no se pueden añadir servicios extra.");

			} else if (bookingService.getBookingStatus(consultaBookingStatus)
					.equals(BookingDao.Status.IN_PROGRESS)) {

				// Hotel de la reserva
				Map<String, Object> keyMapReserva = new HashMap<String, Object>() {
					{
						put(BookingDao.ATTR_ID, attrMap.get(dao.ATTR_ID_BKG));
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
						put(HotelServiceExtraDao.ATTR_ID_SXT, attrMap.get(dao.ATTR_ID_SXT));
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

				// Si da un resultado vacío es que el servcio no está asociado al hotel de la
				// reserva
				if (precio.getRecordValues(0).get(HotelServiceExtraDao.ATTR_ID_SXT) != null) {

					attrMap.put(dao.ATTR_PRECIO,
							precio.getRecordValues(0).get(HotelServiceExtraDao.ATTR_PRECIO));
					System.err.println(attrMap.entrySet());
					resultado = this.daoHelper.insert(this.dao, attrMap); // en progreso añadimos
																								// servicio extra
					System.err.println(resultado);
					resultado.setMessage("Servicio extra registrado");

				} else {
					resultado = new EntityResultWrong("El servicio solicitado no está en el hotel de la reserva");
				}
			}
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(ErrorMessage.RESULT_REQUIRED + " " + ErrorMessage.NO_BOOKING_ID);
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_MISSING_FK);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	/*
	 * Se elimina servicio extra de BOOKING IN PROGRESS las otros estados no deja
	 * eliminarlo, si no esta o esta eliminado se muestra mensaje de servicio
	 */

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingServiceExtraDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();

		try {

			cf.reset();
			List<String> required = Arrays.asList(dao.ATTR_ID);
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(dao.ATTR_ID, keyMap.get(dao.ATTR_ID));
				}
			};
			
			EntityResult auxEntity = bookingServiceExtraQuery(subConsultaKeyMap,
					EntityResultTools.attributes(dao.ATTR_ID, dao.ATTR_ID_BKG));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);

			} else {

				if (bookingService
						.getBookingStatus(auxEntity.getRecordValues(0).get(dao.ATTR_ID_BKG))
						.equals(BookingDao.Status.CANCELED)) {
					resultado = new EntityResultWrong(
							"La reserva de este servicio esta cancelada, no se pueden eliminar su servicio extra asociado.");

				} else if (bookingService
						.getBookingStatus(auxEntity.getRecordValues(0).get(dao.ATTR_ID_BKG))
						.equals(BookingDao.Status.COMPLETED)) {
					resultado = new EntityResultWrong(
							"La reserva de este servicio esta completada, no se pueden eliminar su servicio extra asociado.");

				} else if (bookingService
						.getBookingStatus(auxEntity.getRecordValues(0).get(dao.ATTR_ID_BKG))
						.equals(BookingDao.Status.CONFIRMED)) {
					resultado = new EntityResultWrong(
							"La reserva de este servicio esta confirmada, no se pueden eliminar su servicio extra asociado.");

				} else if (bookingService
						.getBookingStatus(auxEntity.getRecordValues(0).get(dao.ATTR_ID_BKG))
						.equals(BookingDao.Status.IN_PROGRESS)) {
					System.err.println("eliminando");
					resultado = this.daoHelper.delete(this.dao, keyMap); // eliminamos servicio
																							// extra.
					resultado.setMessage("Booking Service Extra eliminado");

				}

			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (EntityResultRequiredException e) {
			resultado = new EntityResultWrong(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	/**
	 * Dado un bsx_bkg_id devuelve los servicios extra de esa reserva, con sus
	 * precios, las unidades y el total de cada registro
	 * 
	 * @param keyMap   (dao.ATTR_ID_BKG)
	 * @param attrList (anyList())
	 * @return EntityResult (dao.ATTR_ID_BKG,
	 *         dao.ATTR_ID_UNITS,
	 *         dao.ATTR_PRECIO,total)
	 * @throws OntimizeJEERuntimeException
	 */
	public EntityResult bookingExtraServicePriceUnitsTotalQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();
		try {
			List<String> required = Arrays.asList(dao.ATTR_ID_BKG);
			cf.reset();
			cf.addBasics(dao.fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.dao, keyMap, attrList,
					"queryServciosExtraPrecioUnidadesTotal");
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	/**
	 * Dado un bsx_bkg_id devuelve los servcios extra de la reserva (nombre,
	 * descripcion, unidades, precio y fecha)
	 * 
	 * @param keyMap   (dao.ATTR_ID_BKG)
	 * @param attrList (anyList())
	 * @return EntityResult (ServicesXtraDao.ATTR_NAME,
	 *         ServicesXtraDao.ATTR_DESCRIPTION,
	 *         dao.ATTR_ID_UNITS,
	 *         dao.ATTR_PRECIO, dao.ATTR_DATE)
	 * @throws OntimizeJEERuntimeException
	 */
	public EntityResult extraServicesNameDescriptionUnitsPriceDateQuery(Map<String, Object> keyMap,
			List<String> attrList) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultMapImpl();

		try {
			List<String> required = Arrays.asList(dao.ATTR_ID_BKG);
			cf.reset();
			cf.addBasics(dao.fields);
			cf.addBasics(ServicesXtraDao.fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.dao, keyMap, attrList,
					"queryServiciosExtraNombreDescripcionUnidadesPrecioFecha");
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}
		return resultado;
	}

}
