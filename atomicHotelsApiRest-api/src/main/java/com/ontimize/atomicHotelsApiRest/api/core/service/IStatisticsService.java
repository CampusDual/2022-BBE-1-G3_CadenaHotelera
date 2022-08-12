package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IStatisticsService {
	
	public EntityResult hotelMaximumCapacityQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
 public EntityResult hotelOccupancyPercentageQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	public EntityResult hotelCapacityInDateRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	public EntityResult hotelOccupancyByNationalityQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	public EntityResult hotelOccupancyByNationalityPercentageQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult gastosDepartamentoPersonalHotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult expensesDepartmentsInRangeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult expensesEmployeesInRangeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult departmentExpensesByHotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult bookingsIncomeByHotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult servicesExtraIncomeByHotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult incomeVsExpensesByHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	


}
