package com.ontimize.atomicHotelsApiRest.model.core.service;

import static org.mockito.ArgumentMatchers.anyDouble;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import com.amadeus.Location;
import com.amadeus.Params;
import com.amadeus.Response;
import com.amadeus.exceptions.NetworkException;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.ScoredLocation;
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
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
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
import com.ontimize.jee.common.db.SQLStatementBuilder.SQLStatement;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.dao.ISQLQueryAdapter;

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
	 * Devuelve la capacidad de un hotel (dado su id o su nombre) o de todos los hotels de la cadena
	 * 
	 * @param keyMap (HotelDao.ATTR_ID,HotelDao.ATTR_NAME)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException 
	 * @return EntityResult (HotelDao.ATTR_ID,HotelDao.ATTR_NAME, HotelDao.ATTR_CITY, HotelDao.ATTR_MAXIMUN_CAPACITY)
	 */
	@Override
	public EntityResult hotelMaximumCapacityQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();

		try {
			
			Map<String, type> fields = new HashMap<>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_NAME, type.STRING);
				}
			};
			cf.reset();
			cf.addBasics(fields);
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


	/**
	 * Devuelve la capacidad de un hotel (dado su id o su nombre) o de todos los hotels de la cadena
	 * 
	 * @param keyMap (HotelDao.ATTR_ID,HotelDao.ATTR_NAME)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException 
	 * @return EntityResult ("occupancy_percentage_in_date_range", "htl_id","capacity_in_date_range","occupancy_in_date_range","htl_city", "htl_name")
	 */
	@Override
	public EntityResult hotelOccupancyPercentageQuery(Map<String, Object> keyMap, List<String> attrList)
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

			Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(checkinData, checkoutData);

			resultado = this.daoHelper.query(this.hotelDao, new HashMap<String, Object>(), attrList,
					"queryOccupancyPercentage", new ISQLQueryAdapter() {

						@Override
						public SQLStatement adaptQuery(SQLStatement sqlStatement, IOntimizeDaoSupport dao,
								Map<?, ?> keysValues, Map<?, ?> validKeysValues, List<?> attributes,
								List<?> validAttributes, List<?> sort, String queryId) {

							Date init_date = checkinData;
							Date end_date = checkoutData;

							String init_s = new SimpleDateFormat("yyyy-MM-dd").format(init_date);
							String end_s = new SimpleDateFormat("yyyy-MM-dd").format(end_date);

							String gen_series = "generate_series('" + init_s + "', '" + end_s + "', '1 day'::interval)";

							SQLStatement result = new SQLStatement(
									sqlStatement.getSQLStatement().replaceAll("#GEN_SERIES#", gen_series),
									sqlStatement.getValues());
							return result;
						}
					});

			if (keyMap.get(HotelDao.ATTR_ID) != null) {
				resultado = EntityResultTools.dofilter(resultado,
						EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)));
			}

		} catch (ValidateException e) {
			e.printStackTrace();
			return resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			return resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}
	
	/**
	 * Devuelve la capacidad de un hotel (dado su id) o de todos los hotels de la cadena en un rango de fechas
	 * 
	 * @param keyMap (HotelDao.ATTR_ID,HotelDao.ATTR_FROM,HotelDao.ATTR_TO)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException 
	 * @return EntityResult (HotelDao.ATTR_ID,HotelDao.ATTR_NAME, HotelDao.ATTR_CITY, HotelDao.ATTR_CAPACITY_IN_DATE_RANGE)
	 */
	@Override
	public EntityResult hotelCapacityInDateRangeQuery(Map<String, Object> keyMap, List<String> attrList)
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

			Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(checkinData, checkoutData);

			resultado = this.daoHelper.query(this.hotelDao, new HashMap<String, Object>(), attrList, "queryCapacityInRange",new ISQLQueryAdapter() {

				@Override
				public SQLStatement adaptQuery(SQLStatement sqlStatement, IOntimizeDaoSupport dao,
						Map<?, ?> keysValues, Map<?, ?> validKeysValues, List<?> attributes,
						List<?> validAttributes, List<?> sort, String queryId) {

					Date init_date = checkinData;
					Date end_date = checkoutData;

					String init_s = new SimpleDateFormat("yyyy-MM-dd").format(init_date);
					String end_s = new SimpleDateFormat("yyyy-MM-dd").format(end_date);

					String gen_series = "generate_series('" + init_s + "', '" + end_s + "', '1 day'::interval)";

					SQLStatement result = new SQLStatement(
							sqlStatement.getSQLStatement().replaceAll("#GEN_SERIES#", gen_series),
							sqlStatement.getValues());
					return result;
				}
			});
			
			if (keyMap.get(HotelDao.ATTR_ID) != null) {
				resultado = EntityResultTools.dofilter(resultado,
						EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)));
			}

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
	 * Devuelve ocupación de un hotel (dado su id) o de todos los hotels de la cadena por nacionalidad de los clientes en un rango de fechas
	 * 
	 * @param keyMap (HotelDao.ATTR_ID,HotelDao.ATTR_FROM,HotelDao.ATTR_TO)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException 
	 * @return EntityResult (CustomerDao.ATTR_COUNTRY, HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE)
	 */
	@Override
	public EntityResult hotelOccupancyByNationalityQuery(Map<String, Object> keyMap, List<String> attrList)
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
			List<String> required = Arrays.asList(HotelDao.ATTR_ID,HotelDao.ATTR_FROM, HotelDao.ATTR_TO);
			cf.reset();
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(checkinData, checkoutData);
			
			Map<String,Object> idHotel = new HashMap<String,Object>(){{
				put(RoomDao.ATTR_HOTEL_ID,keyMap.get(HotelDao.ATTR_ID));
			}};

			resultado = this.daoHelper.query(this.hotelDao, idHotel, attrList, "queryOccupancyByNationality",new ISQLQueryAdapter() {

				@Override
				public SQLStatement adaptQuery(SQLStatement sqlStatement, IOntimizeDaoSupport dao,
						Map<?, ?> keysValues, Map<?, ?> validKeysValues, List<?> attributes,
						List<?> validAttributes, List<?> sort, String queryId) {

					Date init_date = checkinData;
					Date end_date = checkoutData;

					String init_s = new SimpleDateFormat("yyyy-MM-dd").format(init_date);
					String end_s = new SimpleDateFormat("yyyy-MM-dd").format(end_date);

					String gen_series = "generate_series('" + init_s + "', '" + end_s + "', '1 day'::interval)";

					SQLStatement result = new SQLStatement(
							sqlStatement.getSQLStatement().replaceAll("#GEN_SERIES#", gen_series),
							sqlStatement.getValues());
					return result;
				}
			});
			

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
	 * Devuelve el porcentaje de ocupación de un hotel en función de la nacionalidad de los clientes en un rango de fechas
	 * 
	 * @param keyMap (HotelDao.ATTR_ID,HotelDao.ATTR_FROM,HotelDao.ATTR_TO)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException 
	 * @return EntityResult (CustomerDao.ATTR_COUNTRY, HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE)
	 */
	@Override
	public EntityResult hotelOccupancyByNationalityPercentageQuery(Map<String, Object> keyMap, List<String> attrList)
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
			List<String> required = Arrays.asList(HotelDao.ATTR_ID,HotelDao.ATTR_FROM, HotelDao.ATTR_TO);
			cf.reset();
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(checkinData, checkoutData);
			
			Map<String,Object> idHotel = new HashMap<String,Object>(){{
				put(RoomDao.ATTR_HOTEL_ID,keyMap.get(HotelDao.ATTR_ID));
			}};

			
			EntityResult capacidad = this.hotelCapacityInDateRangeQuery(keyMap, new ArrayList<String>());
			EntityResult ocupacion = this.hotelOccupancyByNationalityQuery(keyMap, new ArrayList<String>());

			
			BigDecimal cap = new BigDecimal(0);
			cap = (BigDecimal) capacidad.getRecordValues(0).get(HotelDao.ATTR_CAPACITY_IN_DATE_RANGE);
			
			long capacity =cap.longValue();
			
			resultado = new EntityResultMapImpl();
			
			for(int i=0; i<ocupacion.calculateRecordNumber();i++) {
				
				BigDecimal oc = new BigDecimal(0);
				oc = (BigDecimal) ocupacion.getRecordValues(i).get(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE);
				
				long occupancy = oc.longValue();
				
				double perc = Math.round((((double)occupancy/(double)capacity)*100d)*10000)/10000d;
				int j =i;//?? pero necesario
				
				Map<String,Object> total = new HashMap<String,Object>(){{
					put(CustomerDao.ATTR_COUNTRY,ocupacion.getRecordValues(j).get(CustomerDao.ATTR_COUNTRY));
					put(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE,ocupacion.getRecordValues(j).get(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE));
					put(HotelDao.ATTR_CAPACITY_IN_DATE_RANGE,capacidad.getRecordValues(0).get(HotelDao.ATTR_CAPACITY_IN_DATE_RANGE));
					put(HotelDao.ATTR_OCCUPANCY_PERCENTAGE_IN_DATE_RANGE,perc);
				}};
				
				resultado.addRecord(total);
			}
			
			

			

		} catch (ValidateException e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

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
		String city = (String) resultado.getRecordValues(0).get(HotelDao.ATTR_CITY);
		String urlEnpoint = "https://nominatim.openstreetmap.org/search?street="
				+ URLEncoder.encode(street, StandardCharsets.UTF_8) + "&city="
				+ URLEncoder.encode(city, StandardCharsets.UTF_8) + "&format=json";
		System.err.println(urlEnpoint);

		try {
			URL url = new URL(urlEnpoint);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				throw new RuntimeException("Ocurrio un error " + responseCode);
			} else {
				StringBuilder infor = new StringBuilder();
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					infor.append(sc.nextLine());
				}
				sc.close();
				System.err.println(infor);
				JSONArray jsonarray = new JSONArray(infor.toString());
				JSONObject jsonObject = jsonarray.getJSONObject(0);
				String latitude = jsonObject.getString("lat");
				String longitude = jsonObject.getString("lon");

				Map<String, Object> attrMapco = new HashMap<>() {
					{
						put(HotelDao.ATTR_LAT, latitude);
						put(HotelDao.ATTR_LON, longitude);
					}
				};
				System.out.println(latitude);
				System.out.println(longitude);
				EntityResult coorUpdate = hotelUpdate(attrMapco, keyMap);
				System.err.println(attrMapco.entrySet());
				System.err.println(coorUpdate);
				Amadeus amadeus = Amadeus.builder("h3nxa8Fz2gDyhWAhSY8nhlAGaZ43tGHv", "yTjGtt92Ww2ezfAT").build();
				ScoredLocation[] locationPois = amadeus.location.analytics.categoryRatedAreas
						.get(Params.with("latitude", latitude).and("longitude", longitude));
				System.err.println(locationPois.toString());

				// System.err.println(consulta);
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
		 * 
		 * Response response = null; try { response =
		 * amadeus.post("test.api.amadeus.com", city.toString());
		 * System.err.println(response.getRequest()); } catch (ResponseException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		return resultado;

	}

}