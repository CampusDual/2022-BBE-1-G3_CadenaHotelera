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
				value = "/test",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> query(@RequestBody Map<String,Object> req) {
			 	return this.getService().test((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
				value = "/incomeVsExpensesChart",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> aa(@RequestBody Map<String,Object> req) {
			 	return this.getService().incomeVsExpensesChart((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
				value = "/receipt",
				method = RequestMethod.POST,
				produces = MediaType.APPLICATION_JSON_VALUE)
				public ResponseEntity<EntityResult> b(@RequestBody Map<String,Object> req) {
			 	return this.getService().receipt((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
				}
	 
	 @RequestMapping(
			 value = "/plantilla",
			 method = RequestMethod.POST,
			 produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<EntityResult> plantilla(@RequestBody Map<String,Object> req) {
		 return this.getService().plantilla((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
	 }
}
