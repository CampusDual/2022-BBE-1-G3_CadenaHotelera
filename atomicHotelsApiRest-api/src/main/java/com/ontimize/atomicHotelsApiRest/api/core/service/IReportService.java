package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IReportService {

//	public EntityResult reportPruebaQuery(Map<String, Object> keyMap, List<String> attrList)
//			throws OntimizeJEERuntimeException;
	public ResponseEntity hotels() throws OntimizeJEERuntimeException;

//	public ResponseEntity testChar(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public ResponseEntity incomeVsExpensesChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public ResponseEntity receipt(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

//	public ResponseEntity plantilla(Map<String, Object> keyMap, List<String> attrList);

	public ResponseEntity occupancyChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public ResponseEntity occupancyByNationalityChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public ResponseEntity listAllEmployeeReport(Map<String, Object> keyMap, List<String> attrList);

	public ResponseEntity employeesByHotel(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public ResponseEntity departmentExpensesByHotelChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public ResponseEntity employeePieCostByDepartament(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

}
