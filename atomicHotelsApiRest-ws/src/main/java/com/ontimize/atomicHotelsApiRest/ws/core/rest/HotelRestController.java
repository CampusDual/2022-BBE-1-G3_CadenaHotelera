package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.rest.ORestController;

@RestController // indica que esta clase trabaja como un controlador, que responderá a las peticiones cuya URL tenga el path indicado en la anotación
@RequestMapping("/hotels") //(en este caso, hotels)
public class HotelRestController extends ORestController<IHotelService> {

 @Autowired //permite que los DAO se enlacen correctamente a las variables donde las hemos definido, evitando el uso de métodos getter y setter.
 private IHotelService hotelService;

 @Override
 public IHotelService getService() {
  return this.hotelService;
 }
 
	@RequestMapping(
			value = "/login",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<EntityResult> login() {
			return new ResponseEntity<>(HttpStatus.OK);
		}
}