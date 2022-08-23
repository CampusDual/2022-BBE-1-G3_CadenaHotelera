package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IReportService {
	
//	public EntityResult reportPruebaQuery(Map<String, Object> keyMap, List<String> attrList)
//			throws OntimizeJEERuntimeException;
	public ResponseEntity test(Map<String, Object> filter, List<String> columns) throws OntimizeJEERuntimeException;
//	public ResponseEntity testChar(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	ResponseEntity incomeVsExpensesChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	public ResponseEntity receipt(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	ResponseEntity plantilla(Map<String, Object> keyMap, List<String> attrList);
	public ResponseEntity occupancyChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;
	public ResponseEntity occupancyByNationalityChart(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

}
