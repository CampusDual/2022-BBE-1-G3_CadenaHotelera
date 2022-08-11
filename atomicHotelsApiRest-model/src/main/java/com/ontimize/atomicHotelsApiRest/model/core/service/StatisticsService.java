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

@Service("StatisticsService")
@Lazy
public class StatisticsService implements IStatisticsService{
	
	@Autowired
	private HotelDao hotelDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

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
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

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
			e.printStackTrace();
			resultado = new EntityResultWrong(e.getMessage());

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
	 * @return EntityResult (CustomerDao.ATTR_COUNTRY,HotelDao.ATTR_CAPACITY_IN_DATE_RANGE,HotelDao.ATTR_OCCUPANCY_IN_DATE_RANGE,HotelDao.ATTR_OCCUPANCY_PERCENTAGE_IN_DATE_RANGE)
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
				map.put("occupancy",porcentajesOcupacion);
				
				resultado.addRecord(map);

			} else {
				
				resultado = new EntityResultWrong("El hotel no es apto para alojar clientes");
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
	 * Devuelve una lista de gastos de departamento, asociados a empleados y bills, tras pasarle un htl_id
	 * 
	 */
	@Override
	public EntityResult gastosDepartamentoPersonalHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		
		EntityResult resultado = new EntityResultWrong();
		try {
			List<String> required = new ArrayList<String>() {
				{
					add(hotelDao.ATTR_ID);					
				}
			};			
			cf.reset();
			cf.addBasics(HotelDao.fields);
			cf.addBasics(BillDao.fields);
			cf.addBasics(DepartmentDao.fields);
			cf.addBasics(EmployeeDao.fields);
			cf.addBasics(ReceiptDao.fields);
//			cf.setRequired(required);
//			cf.setOptional(false);
			cf.validate(keyMap);

/*			//Si no quisiéramos aceptar columnas:

			cf.reset();
			cf.setNoEmptyList(false);
			cf.validate(attrList);
*/			
			resultado = this.daoHelper.query(this.hotelDao, keyMap, attrList, "queryGastosDepartamentoPersonalHotel");
			
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
	 * Devuelve una lista de gastos de departamento, asociados a empleados y bills,
	 * dentro de un rango de fechas tras filtrarlo por un htl_id y dicho rango
	 * 
	 */
//	bll_htl_id = 4 
//			and DATE (bll_date) <= '2022-01-01' 
//			and  DATE (bll_date) >= '2019-01-01'
	
	/**
	 * Metodo para obtener una lista de las habitaciones ocupadas total o
	 * parcialmente, en un rango de fechas y filtros (attributos BookingDao). Puede
	 * contener duplicados.
	 * 
	 * @param startDate fecha de inicio (tiene en cuenta las salidas el mismo día
	 *                  que ya son antes de las entradas)
	 * @param endDate   fecha de fin de rango
	 * @return List<Object> relación de ID de habitación que tiene reservas en esas
	 *         fechas. Puede contener duplicados.
	 * @throws OntimizeJEERuntimeException
	 * @throws EntityResultRequiredException
	 * @throws InvalidFieldsValuesException
	 */

	
		public EntityResult expensesDepartmentsInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
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
						put(HotelDao.ATTR_FROM, type.DATE);	//fechas a comparar
						put(HotelDao.ATTR_TO, type.DATE);
					}
				};		
		
				cf.reset();
				cf.addBasics(fields);
				cf.setRequired(required);
				cf.validate(keyMap);

				cf.reset();
				cf.setNoEmptyList(false);
				cf.validate(attrList);

				Date from = (Date) keyMap.get(HotelDao.ATTR_FROM);
				Date to = (Date) keyMap.get(HotelDao.ATTR_TO);

				ValidateFields.dataRange(from, to);			
/*
				bll_htl_id = 4 
						and DATE (bll_date) >= '2019-01-01' 
						and  DATE (bll_date) <= '2022-01-01'
*/						
			
				BasicField hotelId = new BasicField(BillDao.ATTR_ID_HTL);			
				BasicExpression expresion1 = new BasicExpression(hotelId, BasicOperator.EQUAL_OP, keyMap.get(HotelDao.ATTR_ID));

				BasicField date = new BasicField(BillDao.ATTR_DATE);			
				BasicExpression expresion2 = new BasicExpression(date, BasicOperator.MORE_EQUAL_OP, from);
				
	//			BasicField date2 = new BasicField(BillDao.ATTR_DATE);			
				BasicExpression expresion3 = new BasicExpression(date, BasicOperator.LESS_EQUAL_OP, to);
				
				//Ahora juntamos las 3 expresiones q forman el filtro de postman
					
				BasicExpression exp12 = new BasicExpression(expresion1, BasicOperator.AND_OP, expresion2);
				BasicExpression exp123 = new BasicExpression(exp12, BasicOperator.AND_OP, expresion3);
				
				Map<String, Object> bkeyMap = new HashMap<>();
				
				EntityResultExtraTools.putBasicExpression(bkeyMap, exp123);
				
				resultado = this.daoHelper.query(this.hotelDao, bkeyMap, attrList, "queryPrueba");
				
			} catch (ValidateException e) {
				e.printStackTrace();
				resultado = new EntityResultWrong(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
			}
			return resultado;
		}


		public EntityResult expensesEmployeesInRangeQuery(Map<String, Object> keyMap, List<String> attrList)
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
						put(HotelDao.ATTR_FROM, type.DATE);	//fechas a comparar
						put(HotelDao.ATTR_TO, type.DATE);
					}
				};		
		
				cf.reset();
				cf.addBasics(fields);
				cf.setRequired(required);
				cf.validate(keyMap);

				cf.reset();
				cf.setNoEmptyList(false);
				cf.validate(attrList);

				Date from = (Date) keyMap.get(HotelDao.ATTR_FROM);
				Date to = (Date) keyMap.get(HotelDao.ATTR_TO);

				ValidateFields.dataRange(from, to);			
/*
				bll_htl_id = 4 
						and DATE (bll_date) >= '2019-01-01' 
						and  DATE (bll_date) <= '2022-01-01'
*/						
			
				BasicField hotelId = new BasicField(BillDao.ATTR_ID_HTL);			
				BasicExpression expresion1 = new BasicExpression(hotelId, BasicOperator.EQUAL_OP, keyMap.get(HotelDao.ATTR_ID));

				BasicField date = new BasicField(BillDao.ATTR_DATE);			
				BasicExpression expresion2 = new BasicExpression(date, BasicOperator.MORE_EQUAL_OP, from);
				
	//			BasicField date2 = new BasicField(BillDao.ATTR_DATE);			
				BasicExpression expresion3 = new BasicExpression(date, BasicOperator.LESS_EQUAL_OP, to);
				
				//Ahora juntamos las 3 expresiones q forman el filtro de postman
					
				BasicExpression exp12 = new BasicExpression(expresion1, BasicOperator.AND_OP, expresion2);
				BasicExpression exp123 = new BasicExpression(exp12, BasicOperator.AND_OP, expresion3);
				
				Map<String, Object> bkeyMap = new HashMap<>();
				
				EntityResultExtraTools.putBasicExpression(bkeyMap, exp123);
				
				resultado = this.daoHelper.query(this.hotelDao, bkeyMap, attrList, "queryPrueba");
				
			} catch (ValidateException e) {
				e.printStackTrace();
				resultado = new EntityResultWrong(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				resultado = new EntityResultWrong(ErrorMessage.UNKNOWN_ERROR);
			}
			return resultado;
		}

}
