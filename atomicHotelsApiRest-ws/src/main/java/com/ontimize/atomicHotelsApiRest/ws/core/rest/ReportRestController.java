package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IReportService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.rest.ORestController;

@RestController // indica que esta clase trabaja como un controlador, que responderá a las peticiones cuya URL tenga el path indicado en la anotación
@RequestMapping("/reports") //(en este caso, report)(primera del postman)

public class ReportRestController extends ORestController<IReportService>{

	 @Autowired //permite que los DAO se enlacen correctamente a las variables donde las hemos definido, evitando el uso de métodos getter y setter.
	 private IReportService reportService;

	 @Override
	 public IReportService getService() {
	  return this.reportService;
	 
	 }
	 
	 @RequestMapping(
				value = "/hotels",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> hotels() {
			 	return this.getService().hotels();
//			 	return this.getService().hotels((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
				value = "/employeePieCostByDepartament",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> employeePieCostByDepartament(@RequestBody Map<String,Object> req) {
			 	return this.getService().employeePieCostByDepartament((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
				value = "/incomeVsExpensesChart",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> incomeVsExpensesChart(@RequestBody Map<String,Object> req) {
			 	return this.getService().incomeVsExpensesChart((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
				value = "/receipt",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> receipt(@RequestBody Map<String,Object> req) {
			 	return this.getService().receipt((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
				value = "/occupancyChart",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> occupancyChart(@RequestBody Map<String,Object> req) {
			 	return this.getService().occupancyChart((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
				value = "/occupancyByNationalityChart",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> occupancyByNationalityChart(@RequestBody Map<String,Object> req) {
			 	return this.getService().occupancyByNationalityChart((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
//	 @RequestMapping(
//			 value = "/plantilla",
//			 method = RequestMethod.POST,
//			 produces = MediaType.APPLICATION_JSON_VALUE)
//	 public ResponseEntity<EntityResult> plantilla(@RequestBody Map<String,Object> req) {
//		 return this.getService().plantilla((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
//	 }
	 @RequestMapping(
			 value = "/listAllEmployeeReport",
			 method = RequestMethod.POST,
			 produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<EntityResult> listAllEmployeeReport(@RequestBody Map<String,Object> req) {
		 return this.getService().listAllEmployeeReport((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
	 }
	 
	 @RequestMapping(
				value = "/employeesByHotel",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> employeesByHotel(@RequestBody Map<String,Object> req) {
			 	return this.getService().employeesByHotel((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
				value = "/departmentExpensesByHotelChart",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> departmentExpensesByHotelChart(@RequestBody Map<String,Object> req) {
			 	return this.getService().departmentExpensesByHotelChart((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
}
