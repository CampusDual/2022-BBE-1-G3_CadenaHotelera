package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.jee.common.db.SQLStatementBuilder.SQLStatement;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IHotelService {

	 public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
//	 public EntityResult hotelInfoQuery(Map<String, Object> keysValues, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult hotelMaximumCapacityQuery(Map<String, Object> keyMap, List<String> attrList)
				throws OntimizeJEERuntimeException;
	 public EntityResult hotelOccupancyPercentageQuery(Map<String, Object> keyMap, List<String> attrList)
				throws OntimizeJEERuntimeException;
	public EntityResult poiQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

	public EntityResult hotelCapacityInDateRangeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	public EntityResult hotelOccupancyByNationalityQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	public EntityResult hotelOccupancyByNationalityPercentageQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	public EntityResult gastosDepartamentoPersonalHotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult expensesDepartmentsInRangeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult expensesEmployeesInRangeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;


}
