package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.IStatisticsService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BillDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CustomerDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.DepartmentDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.EmployeeDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelServiceDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.ReceiptDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.RoomDao;
import com.ontimize.atomicHotelsApiRest.model.core.dao.UserRoleDao;
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
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.dao.IOntimizeDaoSupport;
import com.ontimize.jee.server.dao.ISQLQueryAdapter;

@Service("StatisticsService")
@Lazy
public class StatisticsService implements IStatisticsService {

	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	HotelService hotelService;

	@Autowired
	ControlFields cf;

	/**
	 * Devuelve la capacidad de un hotel (dado su id o su nombre) o de todos los
	 * hotels de la cadena
	 * 
	 * @param keyMap   (HotelDao.ATTR_ID,HotelDao.ATTR_NAME)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException
	 * @return EntityResult (HotelDao.ATTR_ID,HotelDao.ATTR_NAME,
	 *         HotelDao.ATTR_CITY, HotelDao.ATTR_MAXIMUN_CAPACITY)
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
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
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			cf.addBasics(fields);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			resultado = this.daoHelper.query(this.hotelDao, keyMap, attrList, "queryHotelMaximunCapacity");

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	/**
	 * Devuelve la capacidad de un hotel (dado su id o su nombre) o de todos los
	 * hotels de la cadena
	 * 
	 * @param keyMap   (HotelDao.ATTR_ID,HotelDao.ATTR_NAME)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException
	 * @return EntityResult ("occupancy_percentage_in_date_range",
	 *         "htl_id","capacity_in_date_range","occupancy_in_date_range","htl_city",
	 *         "htl_name")
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
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
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
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
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	/**
	 * Devuelve la capacidad de un hotel (dado su id) o de todos los hotels de la
	 * cadena en un rango de fechas
	 * 
	 * @param keyMap   (HotelDao.ATTR_ID,HotelDao.ATTR_FROM,HotelDao.ATTR_TO)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException
	 * @return EntityResult (HotelDao.ATTR_ID,HotelDao.ATTR_NAME,
	 *         HotelDao.ATTR_CITY, HotelDao.ATTR_CAPACITY_IN_DATE_RANGE)
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
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
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);	
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
					"queryCapacityInRange", new ISQLQueryAdapter() {

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
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	/**
	 * Devuelve ocupación de un hotel (dado su id) o de todos los hotels de la
	 * cadena por nacionalidad de los clientes en un rango de fechas
	 * 
	 * @param keyMap   (HotelDao.ATTR_ID,HotelDao.ATTR_FROM,HotelDao.ATTR_TO)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException
	 * @return EntityResult (CustomerDao.ATTR_COUNTRY,
	 *         HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE)
	 */
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
			List<String> required = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_FROM, HotelDao.ATTR_TO);
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

			Map<String, Object> idHotel = new HashMap<String, Object>() {
				{
					put(RoomDao.ATTR_HOTEL_ID, keyMap.get(HotelDao.ATTR_ID));
				}
			}; 
			

			resultado = this.daoHelper.query(this.hotelDao, idHotel, attrList, "queryOccupancyByNationality",
					new ISQLQueryAdapter() {

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
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	/**
	 * Devuelve el porcentaje de ocupación de un hotel en función de la nacionalidad
	 * de los clientes en un rango de fechas
	 * 
	 * @param keyMap   (HotelDao.ATTR_ID,HotelDao.ATTR_FROM,HotelDao.ATTR_TO)
	 * @param attrList (anyList())
	 * @throws OntimizeJEERuntimeException
	 * @return EntityResult
	 *         (CustomerDao.ATTR_COUNTRY,HotelDao.ATTR_CAPACITY_IN_DATE_RANGE,HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE,HotelDao.ATTR_OCCUPANCY_PERCENTAGE_IN_DATE_RANGE)
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
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
			List<String> required = Arrays.asList(HotelDao.ATTR_ID, HotelDao.ATTR_FROM, HotelDao.ATTR_TO);
			cf.reset();
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			Date checkinData = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date checkoutData = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(checkinData, checkoutData);

			Map<String, Object> idHotel = new HashMap<String, Object>() {
				{
					put(RoomDao.ATTR_HOTEL_ID, keyMap.get(HotelDao.ATTR_ID));
				}
			};

			EntityResult capacidad = this.hotelCapacityInDateRangeQuery(keyMap, new ArrayList<String>());
			EntityResult ocupacion = this.hotelOccupancyByNationalityQuery(keyMap, new ArrayList<String>());

			BigDecimal cap = new BigDecimal(0);
			cap = (BigDecimal) capacidad.getRecordValues(0).get(HotelDao.ATTR_CAPACITY_IN_DATE_RANGE);

			if (cap != null) {

				long capacity = cap.longValue();

				resultado = new EntityResultMapImpl();

				Map<String, Object> map = new HashMap<String, Object>();
				List<Object> porcentajesOcupacion = new ArrayList<Object>();

				for (int i = 0; i < ocupacion.calculateRecordNumber(); i++) {

					BigDecimal oc = new BigDecimal(0);
					oc = (BigDecimal) ocupacion.getRecordValues(i).get(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE);

					long occupancy = oc.longValue();

					double perc = Math.round((((double) occupancy / (double) capacity) * 100d) * 10000) / 10000d;
					int j = i;// !?!? pero necesario

					Map<String, Object> total = new HashMap<String, Object>() {
						{
							put(CustomerDao.ATTR_COUNTRY, ocupacion.getRecordValues(j).get(CustomerDao.ATTR_COUNTRY));
							put(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE,
									ocupacion.getRecordValues(j).get(HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE));
							put(HotelDao.ATTR_OCCUPANCY_PERCENTAGE_IN_DATE_RANGE, perc);
						}
					};

					porcentajesOcupacion.add(total);

				}

				map.putAll(capacidad.getRecordValues(0));
				map.put("occupancy", porcentajesOcupacion);

				resultado.addRecord(map);

			} else {

				resultado = new EntityResultWrong("El hotel no es apto para alojar clientes");
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	/**
	 * Dado el hotel y un rango de fechas, devuelve el total de gastos (los gastos y
	 * los gastos en salario por departamento)
	 * 
	 * @param keyMap   (HotelDao.ATTR_FROM, HOtelDao.ATTR_TO,HotelDao.ATTR_ID)
	 * @param attrList (anyList())
	 * @return EntityResult (DepartmentDao.ATTR_ID,"total_expenses")
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult departmentExpensesByHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_FROM);
					add(HotelDao.ATTR_TO);
					add(HotelDao.ATTR_ID);
				}
			};

			Map<String, type> fields = new HashMap<String, type>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_FROM, type.DATE); // fechas a comparar
					put(HotelDao.ATTR_TO, type.DATE);
				}
			};

			cf.reset();
			cf.addBasics(fields);
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			Date from = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date to = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(from, to);

			resultado = this.daoHelper.query(this.hotelDao, new HashMap<String, Object>(), attrList,
					"queryTotalDepartmentExpensesByHotel", new ISQLQueryAdapter() {

						@Override
						public SQLStatement adaptQuery(SQLStatement sqlStatement, IOntimizeDaoSupport dao,
								Map<?, ?> keysValues, Map<?, ?> validKeysValues, List<?> attributes,
								List<?> validAttributes, List<?> sort, String queryId) {

							Date init_date = from;
							Date end_date = to;

							String init_s = new SimpleDateFormat("yyyy-MM-dd").format(init_date);
							String end_s = new SimpleDateFormat("yyyy-MM-dd").format(end_date);

							String gen_series = "generate_series('" + init_s + "', '" + end_s + "', '1 day'::interval)";
							String hotel_empl = "AND aux.emp_htl_id = " + (int) keyMap.get(HotelDao.ATTR_ID);
							String dep_hotel_dates = "bll_htl_id=" + (int) keyMap.get(HotelDao.ATTR_ID)
									+ " AND DATE(bll_date) >='" + init_s + "' AND DATE(bll_date) <= '" + end_s + "'";

							SQLStatement result = new SQLStatement(sqlStatement.getSQLStatement()
									.replaceAll("#GEN_SERIES#", gen_series).replaceAll("#HOTEL_EMPL#", hotel_empl)
									.replaceAll(" #DEP_HOTEL_DATE#", dep_hotel_dates), sqlStatement.getValues());

							return result;
						}
					});

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomsIncomeByHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_FROM);
					add(HotelDao.ATTR_TO);
				}
			};

			Map<String, type> fields = new HashMap<String, type>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_FROM, type.DATE); // fechas a comparar
					put(HotelDao.ATTR_TO, type.DATE);
				}
			};

			cf.reset();
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			Date from = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date to = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(from, to);

			resultado = this.daoHelper.query(this.hotelDao, new HashMap<String, Object>(), attrList,
					"queryBookingsIncomeByHotel", new ISQLQueryAdapter() {

						@Override
						public SQLStatement adaptQuery(SQLStatement sqlStatement, IOntimizeDaoSupport dao,
								Map<?, ?> keysValues, Map<?, ?> validKeysValues, List<?> attributes,
								List<?> validAttributes, List<?> sort, String queryId) {

							Date init_date = from;
							Date end_date = to;

							String init_s = new SimpleDateFormat("yyyy-MM-dd").format(init_date);
							String end_s = new SimpleDateFormat("yyyy-MM-dd").format(end_date);

							String gen_series = " generate_series('" + init_s + "', '" + end_s
									+ "', '1 day'::interval)";

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
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	/**
	 * Dado el hotel y un rango de fechas, devuelve el total de ingresos recibidos
	 * por los servicios extra abonados
	 * 
	 * @param keyMap   (HotelDao.ATTR_FROM, HotelDao.ATTR_TO, HotelDao.ATTR_ID)
	 * @param attrList (anyList())
	 * @return EntityResult (DepartmentDao.ATTR_ID,"total_income")
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult servicesExtraIncomeByHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_FROM); // fechas a comparar, filtro postman
					add(HotelDao.ATTR_TO);
				}
			};

			Map<String, type> fields = new HashMap<String, type>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_FROM, type.DATE);
					put(HotelDao.ATTR_TO, type.DATE);
				}
			};

			cf.reset();
			cf.setCPHtlColum(HotelDao.ATTR_ID);
			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);

			Date from = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date to = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(from, to);

			resultado = this.daoHelper.query(this.hotelDao, new HashMap<String, Object>(), attrList,
					"queryServicesExtrasIncomeByHotel");

			if (keyMap.get(HotelDao.ATTR_ID) != null) {
				resultado = EntityResultTools.dofilter(resultado,
						EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)));
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

	/**
	 * Dado el hotel y un rango de fechas, devuelven los beneficios netos, los
	 * gastos y los ingresos
	 * 
	 * @param keyMap   (HotelDao.ATTR_FROM, HotelDao.ATTR_TO, HotelDao.ATTR_ID)
	 * @param attrList (anyList())
	 * @return EntityResult ("benefits","total_income","htl_id","total_expenses")
	 * @throws OntimizeJEERuntimeException
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult incomeVsExpensesByHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_FROM);
					add(HotelDao.ATTR_TO);
				}
			};

			Map<String, type> fields = new HashMap<String, type>() {
				{
					put(HotelDao.ATTR_ID, type.INTEGER);
					put(HotelDao.ATTR_FROM, type.DATE);
					put(HotelDao.ATTR_TO, type.DATE);
				}
			};

			cf.reset();
//			cf.setCPHtlColum(HotelDao.ATTR_ID);
//			cf.setCPRoleUsersRestrictions(UserRoleDao.ROLE_MANAGER);
			cf.addBasics(fields);
			cf.setRequired(required);
			cf.validate(keyMap);

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList); 

			Date from = (Date) keyMap.get(HotelDao.ATTR_FROM);
			Date to = (Date) keyMap.get(HotelDao.ATTR_TO);

			ValidateFields.dataRange(from, to);

			List<String> idHoteles = new ArrayList<String>() {
				{
					add(HotelDao.ATTR_ID);
					add(HotelDao.ATTR_NAME);
					add(HotelDao.ATTR_CITY);
				}
			};

			EntityResult hoteles = hotelService.hotelQuery(new HashMap<String, Object>(), idHoteles);

			Map<String, Object> finalResult = new HashMap<String, Object>();
			resultado = new EntityResultMapImpl();

			for (int j = 0; j < hoteles.calculateRecordNumber(); j++) {
				int h = (int) hoteles.getRecordValues(j).get(HotelDao.ATTR_ID);
				String name = (String) hoteles.getRecordValues(j).get(HotelDao.ATTR_NAME);
				String city = (String) hoteles.getRecordValues(j).get(HotelDao.ATTR_CITY);

				Map<String, Object> cadaHotel = new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_ID, h);
						put(HotelDao.ATTR_FROM, keyMap.get(HotelDao.ATTR_FROM));
						put(HotelDao.ATTR_TO, keyMap.get(HotelDao.ATTR_TO));
					}
				};

				EntityResult gastos = this.departmentExpensesByHotelQuery(cadaHotel, new ArrayList<String>());
				BigDecimal total_expenses = new BigDecimal(0);

				for (int i = 0; i < gastos.calculateRecordNumber(); i++) {
					total_expenses = total_expenses.add((BigDecimal) gastos.getRecordValues(i).get("total_expenses"));
				}

				EntityResult ingresosBookings = this.roomsIncomeByHotelQuery(cadaHotel, new ArrayList<String>());

				EntityResult ingresosServicesExtra = this.servicesExtraIncomeByHotelQuery(cadaHotel,
						new ArrayList<String>());

				BigDecimal incomeBooking = (BigDecimal) ingresosBookings.getRecordValues(0).get("rooms_income");
				BigDecimal incomeServiceExtra = (BigDecimal) ingresosServicesExtra.getRecordValues(0)
						.get("services_extra_income");

				if (incomeBooking == null) {
					incomeBooking = new BigDecimal(0);
				}

				if (incomeServiceExtra == null) {
					incomeServiceExtra = new BigDecimal(0);
				}

				BigDecimal total_income = incomeBooking.add(incomeServiceExtra);

				BigDecimal benefits = total_income.subtract(total_expenses);

				BigDecimal a = total_expenses;

				finalResult = new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_ID, h);
						put(HotelDao.ATTR_NAME, name);
						put(HotelDao.ATTR_CITY, city);
						put("total_income", total_income);
						put("total_expenses", a);
						put("benefits", benefits);
					}
				};
				resultado.addRecord(finalResult);
			}

			if (keyMap.get(HotelDao.ATTR_ID) != null) {
				resultado = EntityResultTools.dofilter(resultado,
						EntityResultTools.keysvalues(HotelDao.ATTR_ID, keyMap.get(HotelDao.ATTR_ID)));
			}

		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
		}
		return resultado;

	}

//	/**
//	 * Dado el hotel y un rango de fechas, devuelven los beneficios netos, los gastos y los ingresos
//	 * 
//	 * @param keyMap   (HotelDao.ATTR_FROM, HotelDao.ATTR_TO, HotelDao.ATTR_ID)
//	 * @param attrList (anyList())
//	 * @return EntityResult ("benefits","total_income","htl_id","total_expenses")
//	 * @throws OntimizeJEERuntimeException
//	 * */
//	@Override
//	public EntityResult incomeVsExpensesByHotelQuery(Map<String, Object> keyMap, List<String> attrList)
//			throws OntimizeJEERuntimeException {
//
//		EntityResult resultado = new EntityResultWrong();
//		try {
//			List<String> required = new ArrayList<String>() {
//				{
//					add(HotelDao.ATTR_FROM); // fechas a comparar, filtro postman
//					add(HotelDao.ATTR_TO);
//					add(HotelDao.ATTR_ID);
//				}
//			};
//
//			Map<String, type> fields = new HashMap<String, type>() {
//				{
//					put(HotelDao.ATTR_ID, type.INTEGER);
//					put(HotelDao.ATTR_FROM, type.DATE); 
//					put(HotelDao.ATTR_TO, type.DATE);
//				}
//			};
//
//			cf.reset();
//			cf.addBasics(fields);
//			cf.setRequired(required);
//			cf.validate(keyMap);
//
//			cf.reset();
//			cf.setNoEmptyList(false);
//			cf.validate(attrList);
//
//			Date from = (Date) keyMap.get(HotelDao.ATTR_FROM);
//			Date to = (Date) keyMap.get(HotelDao.ATTR_TO);
//
//			ValidateFields.dataRange(from, to);
//			
//			EntityResult gastos = this.departmentExpensesByHotelQuery(keyMap, new ArrayList<String>());
//			BigDecimal total_expenses=new BigDecimal(0);
//			
//			for(int i=0; i<gastos.calculateRecordNumber(); i++) {	
//				total_expenses=total_expenses.add((BigDecimal) gastos.getRecordValues(i).get("total_expenses"));
//			}
//			
//			EntityResult ingresosBookings = this.bookingsIncomeByHotelQuery(keyMap, new ArrayList<String>());
//			
//			EntityResult ingresosServicesExtra = this.servicesExtraIncomeByHotelQuery(keyMap, new ArrayList<String>());
//			
//			BigDecimal incomeBooking = (BigDecimal)ingresosBookings.getRecordValues(0).get("rooms_income");
//			BigDecimal incomeServiceExtra = (BigDecimal)ingresosServicesExtra.getRecordValues(0).get("services_extra_income");
//			
//			if(incomeBooking==null) {
//				incomeBooking=new BigDecimal(0);
//			}
//			
//			if(incomeServiceExtra==null) {
//				incomeServiceExtra=new BigDecimal(0);
//			}
//			
//			BigDecimal total_income = incomeBooking.add(incomeServiceExtra);
//			
//			BigDecimal benefits = total_income.subtract(total_expenses);
//			
//			BigDecimal a = total_expenses;
//			
//			Map<String,Object> finalResult = new HashMap<String,Object>(){{
//				put(HotelDao.ATTR_ID,keyMap.get(HotelDao.ATTR_ID));
//				put("total_income",total_income);
//				put("total_expenses",a);
//				put("benefits",benefits);
//			}};
//			
//			resultado.addRecord(finalResult);
//	
//
//		} catch (ValidateException e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(e.getMessage());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
//		}
//		return resultado;
//
//	}
}
