package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.xml.EmptyStringEntityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Service;

import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingColumnsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingServiceExtraDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomTypeDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultExtraTools;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	@Override
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {
			cf.reset();
			cf.addBasics(HotelDao.fields);
//			cf.setOptional(true);
			cf.validate(keyMap);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.hotelDao, keyMap, attrList);

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {

			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_NAME);
					add(HotelDao.ATTR_STREET);
					add(HotelDao.ATTR_CITY);
					add(HotelDao.ATTR_CP);
					add(HotelDao.ATTR_STATE);
					add(HotelDao.ATTR_COUNTRY);
				}
			};
			List<String> restricted = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_ID);// No quiero que meta el id porque quiero el id autogenerado de la base de
											// datos
				}
			};

			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.setRequired(required);
			cf.setRestricted(restricted);
			cf.validate(attrMap);

			resultado = this.daoHelper.insert(this.hotelDao, attrMap);
			resultado.setMessage("Hotel registrado");

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR + e.getMessage());
		} catch (DuplicateKeyException e) {
			resultado = new EntityResultWrong(ErrorMessage.CREATION_ERROR_DUPLICATED_FIELD);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}

		// OPCION A (comprobando si el registro ya existe)
//		if (attrMap.containsKey(HotelDao.ATTR_NAME)) {
//			Map<String, Object> auxKeyMap = new HashMap<String, Object>();
//			List<String> auxAttrList = new ArrayList<String>();
//			auxKeyMap.put(HotelDao.ATTR_NAME, attrMap.get(HotelDao.ATTR_NAME));
//			auxAttrList.add(HotelDao.ATTR_NAME);
//			EntityResult auxEntity = hotelQuery(auxKeyMap, auxAttrList);
//			// System.out.println("coincidencias:" + auxEntity.calculateRecordNumber());//
//			// TODO eliminar
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			} else {
//				resultado.setCode(EntityResult.OPERATION_WRONG);
//				resultado.setMessage("Error al crear Hotel - El registro ya existe");
//			}
//		}

		// OPCION B (capturando excepción duplicateKey)
//		try {
//			resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			if (resultado != null && resultado.getCode() == EntityResult.OPERATION_WRONG) {
//				resultado.setMessage("Error al insertar datos");
//			} else {
//				resultado.setMessage("mensaje cambiado2 desde insert");
//			}
//		} catch (DuplicateKeyException e) {
//			resultado.setCode(EntityResult.OPERATION_WRONG);
//			resultado.setMessage("Error al crear Hotel - El registro ya existe");
//		}

		// TODO limpiar pruebas de setMessage

//		// OPCION C (comprobando si el registro ya existe)
//		if (attrMap.containsKey(HotelDao.ATTR_NAME)) {
//			EntityResult auxEntity = this.daoHelper.query(this.hotelDao,
//					EntityResultTools.keysvalues(HotelDao.ATTR_NAME, attrMap.get(HotelDao.ATTR_NAME)),
//					EntityResultTools.attributes(HotelDao.ATTR_NAME));
//			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
//				resultado = this.daoHelper.insert(this.hotelDao, attrMap);
//			} else {				
//				resultado = new EntityResultWrong("Error al crear Hotel - El registro ya existe");
//			}
//		}
		return resultado;
	}

	@Override // data //filter
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {

			// ControlFields del filtro
			List<String> requiredFilter = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.setRequired(requiredFilter);
			cf.setOptional(false);// No será aceptado ningún campo que no esté en required
			cf.validate(keyMap);

			// ControlFields de los nuevos datos
			List<String> restrictedData = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_ID);// El id no se puede actualizar
				}
			};
			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.setRestricted(restrictedData);
			cf.validate(attrMap);

			resultado = this.daoHelper.update(this.hotelDao, attrMap, keyMap);

			if (resultado.getCode() == EntityResult.OPERATION_SUCCESSFUL_SHOW_MESSAGE) {
				resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_MISSING_FIELD);
			} else {
				resultado = new EntityResultMapImpl();
				resultado.setMessage("Hotel actualizado");
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR + " - " + e.getMessage());
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_DUPLICATED_FIELD);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UPDATE_ERROR_REQUIRED_FIELDS);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_ID);
				}
			};
			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.setRequired(required);
			cf.setOptional(false);
			cf.validate(keyMap);

			Map<String, Object> subConsultaKeyMap = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID));
				}
			};

			EntityResult auxEntity = hotelQuery(subConsultaKeyMap, EntityResultTools.attributes(HotelDao.ATTR_ID));

			if (auxEntity.calculateRecordNumber() == 0) { // si no hay registros...
				resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_MISSING_FIELD);
			} else {
				resultado = this.daoHelper.delete(this.hotelDao, keyMap);
				resultado.setMessage("Hotel eliminado");
			}

		} catch (ValidateException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR + e.getMessage());
		} catch (DataIntegrityViolationException e) {
			resultado = new EntityResultWrong(ErrorMessage.DELETE_ERROR_FOREING_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

//	@Override
//	public EntityResult hotelInfoQuery(Map<String, Object> keysValues, List<String> attrList)
//			throws OntimizeJEERuntimeException {
//// el InfoQuery lo utilizamos para obtener una query mas detallada con joins de
//// otras tablas.
//		EntityResult queryRes = new EntityResultWrong();
//		try {
//			ControlFields cf = new ControlFields();
//			cf.addBasics(HotelDao.fields);
//			cf.validate(keysValues);
//			cf.validate(attrList);
//
//			queryRes = this.daoHelper.query(this.hotelDao, keysValues, attrList, "queryHotel");
//		} catch (ValidateException e) {
//			queryRes = new EntityResultWrong(e.getMessage());
//		} catch (Exception e) {
//			queryRes = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
//		}
//
//		return queryRes;
////		return null;
//	}

	/**
	 * 
	 */
	@Override
	public EntityResult hotelOccupancyQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {

			Map<String, type> fields = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_FROM, type.DATE);
					put(HotelDao.ATTR_TO, type.DATE);
				}
			};

			List<String> required = Arrays.asList(HotelDao.ATTR_FROM, HotelDao.ATTR_TO);
			cf.reset();
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			BasicField checkin = new BasicField(BookingDao.ATTR_CHECKIN);
			Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);

			BasicField checkout = new BasicField(BookingDao.ATTR_CHECKOUT);
			Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(checkinData, checkoutData);

			/*
			 * (bkg_checkin <= checkinData AND bkg_checkout >checkinData) OR (bkg_checkin <
			 * checkoutData AND (bkg_checkout >= checkoutData OR bkg_checkout IS NULL)) OR
			 * (bkg_checkin >=checkinData AND bkg_checkout <= checkoutData)
			 */

			// (bkg_checkin <= checkinData AND bkg_checkout >checkinData)
			BasicExpression exp1 = new BasicExpression(checkin, BasicOperator.LESS_EQUAL_OP, checkinData);
			BasicExpression exp2 = new BasicExpression(checkout, BasicOperator.MORE_OP, checkinData);

			// (bkg_checkin < checkoutData AND (bkg_checkout >= checkoutData OR bkg_checkout
			// IS NULL))
			BasicExpression exp3 = new BasicExpression(checkin, BasicOperator.LESS_OP, checkoutData);
			BasicExpression exp4 = new BasicExpression(checkout, BasicOperator.MORE_EQUAL_OP, checkoutData);
			BasicExpression exp5 = new BasicExpression(checkout, BasicOperator.NULL_OP, null);

			// (bkg_checkin >=checkinData AND bkg_checkout <= checkoutData)
			BasicExpression exp6 = new BasicExpression(checkin, BasicOperator.MORE_EQUAL_OP, checkinData);
			BasicExpression exp7 = new BasicExpression(checkout, BasicOperator.LESS_EQUAL_OP, checkoutData);

			// OR DENTRO DE LA SEGUNDA FILA
			BasicExpression exp8 = new BasicExpression(exp4, BasicOperator.OR_OP, exp5);

			// ANDS DE TODAS LAS FILAS
			BasicExpression exp9 = new BasicExpression(exp1, BasicOperator.AND_OP, exp2);
			BasicExpression exp10 = new BasicExpression(exp3, BasicOperator.AND_OP, exp8);
			BasicExpression exp11 = new BasicExpression(exp6, BasicOperator.AND_OP, exp7);

			// LAS TRES FILAS UNIDAS POR OR
			BasicExpression auxFinal = new BasicExpression(exp9, BasicOperator.OR_OP, exp10);
			BasicExpression finalExpression = new BasicExpression(auxFinal, BasicOperator.OR_OP, exp11);

			keyMap.remove(HotelDao.ATTR_FROM);
			keyMap.remove(HotelDao.ATTR_TO);

			EntityResultExtraTools.putBasicExpression(keyMap, finalExpression);

			resultado = this.daoHelper.query(this.hotelDao, keyMap, attrList, "queryHotelOccupancy");

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	/**
	 * 
	 */
	@Override
	public EntityResult hotelMaximumCapacityQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {

			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.hotelDao, keyMap, attrList, "queryHotelMaximunCapacity");

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	public Map<String, Object> keyMapBasicExpresionDates(Map<String, Object> keyMap)
			throws InvalidFieldsValuesException {

		BasicField checkin = new BasicField(BookingDao.ATTR_CHECKIN);
		Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);

		BasicField checkout = new BasicField(BookingDao.ATTR_CHECKOUT);
		Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);

		ValidateFields.dataRange(checkinData, checkoutData);

		/*
		 * (bkg_checkin <= checkinData AND bkg_checkout >checkinData) OR (bkg_checkin <
		 * checkoutData AND (bkg_checkout >= checkoutData OR bkg_checkout IS NULL)) OR
		 * (bkg_checkin >=checkinData AND bkg_checkout <= checkoutData)
		 */

		// (bkg_checkin <= checkinData AND bkg_checkout >checkinData)
		BasicExpression exp1 = new BasicExpression(checkin, BasicOperator.LESS_EQUAL_OP, checkinData);
		BasicExpression exp2 = new BasicExpression(checkout, BasicOperator.MORE_OP, checkinData);

		// (bkg_checkin < checkoutData AND (bkg_checkout >= checkoutData OR bkg_checkout
		// IS NULL))
		BasicExpression exp3 = new BasicExpression(checkin, BasicOperator.LESS_OP, checkoutData);
		BasicExpression exp4 = new BasicExpression(checkout, BasicOperator.MORE_EQUAL_OP, checkoutData);
		BasicExpression exp5 = new BasicExpression(checkout, BasicOperator.NULL_OP, null);

		// (bkg_checkin >=checkinData AND bkg_checkout <= checkoutData)
		BasicExpression exp6 = new BasicExpression(checkin, BasicOperator.MORE_EQUAL_OP, checkinData);
		BasicExpression exp7 = new BasicExpression(checkout, BasicOperator.LESS_EQUAL_OP, checkoutData);

		// OR DENTRO DE LA SEGUNDA FILA
		BasicExpression exp8 = new BasicExpression(exp4, BasicOperator.OR_OP, exp5);

		// ANDS DE TODAS LAS FILAS
		BasicExpression exp9 = new BasicExpression(exp1, BasicOperator.AND_OP, exp2);
		BasicExpression exp10 = new BasicExpression(exp3, BasicOperator.AND_OP, exp8);
		BasicExpression exp11 = new BasicExpression(exp6, BasicOperator.AND_OP, exp7);

		// LAS TRES FILAS UNIDAS POR OR
		BasicExpression auxFinal = new BasicExpression(exp9, BasicOperator.OR_OP, exp10);
		BasicExpression finalExpression = new BasicExpression(auxFinal, BasicOperator.OR_OP, exp11);

//		keyMap.remove(HotelDao.ATTR_FROM);
//		keyMap.remove(HotelDao.ATTR_TO);

		Map<String, Object> keyMapBasicExpression = new HashMap<String, Object>();

		EntityResultExtraTools.putBasicExpression(keyMapBasicExpression, finalExpression);

		return keyMapBasicExpression;

	}

	@Override
	public EntityResult hotelOccupancyDailyRateQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		EntityResult resultadofinal = new EntityResultMapImpl();

		try {

			Map<String, type> fields = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_FROM, type.DATE);
					put(HotelDao.ATTR_TO, type.DATE);
				}
			};

			List<String> required = Arrays.asList(HotelDao.ATTR_FROM, HotelDao.ATTR_TO);
			cf.reset();
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);
			
			BasicField checkin = new BasicField(BookingDao.ATTR_CHECKIN);
			Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);	
			
//			BasicField checkout = new BasicField(BookingDao.ATTR_CHECKOUT);
			Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);
			
			ValidateFields.dataRange(checkinData, checkoutData);
			
			/*(bkg_checkin <= checkinData AND bkg_checkout >checkinData) OR
			(bkg_checkin < checkoutData AND (bkg_checkout >= checkoutData OR bkg_checkout IS NULL)) OR
			(bkg_checkin >=checkinData AND bkg_checkout <= checkoutData)*/
			
			//(bkg_checkin <= checkinData AND bkg_checkout >checkinData)
			BasicExpression exp1 = new BasicExpression(checkin,BasicOperator.MORE_EQUAL_OP, checkinData);
			BasicExpression exp2 = new BasicExpression(checkin,BasicOperator.LESS_EQUAL_OP, checkoutData);
			
			BasicExpression finalExpression = new BasicExpression(exp1,BasicOperator.AND_OP, exp2);
			
			Map<String,Object> keyMapBasicExpression = new HashMap<String,Object>();
			
			EntityResultExtraTools.putBasicExpression(keyMapBasicExpression, finalExpression);

			resultado = this.daoHelper.query(this.hotelDao, keyMapBasicExpression, attrList,
					"queryHotelOccupancyDailyRate");

			if (keyMap.get(HotelDao.ATTR_ID) != null) {
				resultadofinal = EntityResultTools.dofilter(resultado,
						EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)));
			}

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultadofinal;

	}

	@Override
	public EntityResult hotelOcupancyRateQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		EntityResult resultadofinal = new EntityResultMapImpl();

		try {

			Map<String, type> fields = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_FROM, type.DATE);
					put(HotelDao.ATTR_TO, type.DATE);
				}
			};

			List<String> required = Arrays.asList(HotelDao.ATTR_FROM, HotelDao.ATTR_TO);
			cf.reset();
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

//			BasicField checkin = new BasicField(BookingDao.ATTR_CHECKIN);
//			Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);	
//			
//			BasicField checkout = new BasicField(BookingDao.ATTR_CHECKOUT);
//			Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);
//			
//			ValidateFields.dataRange(checkinData, checkoutData);
//			
//			/*(bkg_checkin <= checkinData AND bkg_checkout >checkinData) OR
//			(bkg_checkin < checkoutData AND (bkg_checkout >= checkoutData OR bkg_checkout IS NULL)) OR
//			(bkg_checkin >=checkinData AND bkg_checkout <= checkoutData)*/
//			
//			//(bkg_checkin <= checkinData AND bkg_checkout >checkinData)
//			BasicExpression exp1 = new BasicExpression(checkin,BasicOperator.LESS_EQUAL_OP, checkinData);
//			BasicExpression exp2 = new BasicExpression(checkout,BasicOperator.MORE_OP, checkinData);
//			
//			//(bkg_checkin < checkoutData AND (bkg_checkout >= checkoutData OR bkg_checkout IS NULL)) 
//			BasicExpression exp3 = new BasicExpression(checkin,BasicOperator.LESS_OP, checkoutData);
//			BasicExpression exp4 = new BasicExpression(checkout,BasicOperator.MORE_EQUAL_OP, checkoutData);
//			BasicExpression exp5 = new BasicExpression(checkout, BasicOperator.NULL_OP, null);
//			
//			//(bkg_checkin >=checkinData AND bkg_checkout <= checkoutData)
//			BasicExpression exp6 = new BasicExpression(checkin,BasicOperator.MORE_EQUAL_OP, checkinData);
//			BasicExpression exp7 = new BasicExpression(checkout,BasicOperator.LESS_EQUAL_OP, checkoutData);
//			
//			//OR DENTRO DE LA SEGUNDA FILA
//			BasicExpression exp8 = new BasicExpression(exp4,BasicOperator.OR_OP,exp5);
//			
//			//ANDS DE TODAS LAS FILAS
//			BasicExpression exp9 = new BasicExpression(exp1,BasicOperator.AND_OP,exp2);
//			BasicExpression exp10 = new BasicExpression(exp3,BasicOperator.AND_OP,exp8);
//			BasicExpression exp11 = new BasicExpression(exp6,BasicOperator.AND_OP,exp7);
//			
//			//LAS TRES FILAS UNIDAS POR OR
//			BasicExpression auxFinal = new BasicExpression(exp9, BasicOperator.OR_OP, exp10);
//			BasicExpression finalExpression = new BasicExpression(auxFinal, BasicOperator.OR_OP, exp11);
//		
////			keyMap.remove(HotelDao.ATTR_FROM);
////			keyMap.remove(HotelDao.ATTR_TO);
//			
//			Map<String,Object> keyMapBasicExpression = new HashMap<String,Object>();
//			
//			EntityResultExtraTools.putBasicExpression(keyMapBasicExpression, finalExpression);

			resultado = this.daoHelper.query(this.hotelDao, keyMapBasicExpresionDates(keyMap), attrList,
					"queryHotelOccupancyRate");

			if (keyMap.get(HotelDao.ATTR_ID) != null) {
				resultadofinal = EntityResultTools.dofilter(resultado,
						EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)));
			}

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultadofinal;

	}
	
	public EntityResult poiQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(HotelDao.fields);
//			cf.setOptional(true);
			System.err.println(keyMap.entrySet());
			System.err.println(attrList.toString());
			cf.validate(keyMap);
			cf.validate(attrList);
			Map<String, Object> keyMapDireccion = new HashMap<>();
			System.err.println(keyMap.get(HotelDao.ATTR_ID));
			if (keyMap.get(HotelDao.ATTR_ID) != null) {
				keyMapDireccion.put(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID));
			}
			resultado = hotelQuery(keyMapDireccion, attrList);
			System.err.println(resultado.entrySet());
			
			
		} catch (MissingFieldsException | RestrictedFieldException | InvalidFieldsException
				| InvalidFieldsValuesException | LiadaPardaException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		String street = (String) resultado.getRecordValues(0).get(HotelDao.ATTR_STREET);
		String city =  (String) resultado.getRecordValues(0).get(HotelDao.ATTR_CITY);
		String urlEnpoint = "https://nominatim.openstreetmap.org/search?street="+URLEncoder.encode(street, StandardCharsets.UTF_8)+"&city="+URLEncoder.encode(city, StandardCharsets.UTF_8)+"&format=json";
		System.err.println(urlEnpoint);
		
		try {
			URL url = new URL(urlEnpoint);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			int responseCode = con.getResponseCode();
			if(responseCode!=200)
			{
				throw new RuntimeException("Ocurrio un error " + responseCode);
			}
			else
			{
				StringBuilder infor = new StringBuilder();
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext()){
					infor.append(sc.nextLine());
				}
				sc.close();
				System.err.println(infor);
				JSONArray jsonarray = new JSONArray(infor.toString());
				JSONObject jsonObject = jsonarray.getJSONObject(0);
				
				Map<String,Object> attrMapco = new HashMap<>() {{
				   put(HotelDao.ATTR_LAT,jsonObject.getString("lat"));
				   put(HotelDao.ATTR_LON,jsonObject.getString("lon"));
				}};
				System.out.println(jsonObject.getString("lat"));
				System.out.println(jsonObject.getString("lon"));
				EntityResult coorUpdate = hotelUpdate(attrMapco, keyMap);
				System.err.println(attrMapco.entrySet());
				System.err.println(coorUpdate);
				
				Amadeus amadeus = Amadeus.builder("h3nxa8Fz2gDyhWAhSY8nhlAGaZ43tGHv", "yTjGtt92Ww2ezfAT").build();
				String UrlEmpointAmadeus = "https://test.api.amadeus.com/v1/reference-data/locations/pois?latitude=41.397158&longitude=2.160873&page[limit]=30";
				String consulta = amadeus.post(UrlEmpointAmadeus).getBody();
				System.err.println(consulta);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*

		Response response = null;
		try {
			response = amadeus.post("test.api.amadeus.com", city.toString());
			System.err.println(response.getRequest());
		} catch (ResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return  resultado;

	}

}