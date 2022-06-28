package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.rest.ORestController;

@RestController // indica que esta clase trabaja como un controlador, que responderá a las peticiones cuya URL tenga el path indicado en la anotación
@RequestMapping("/roomTypes") //(en este caso, roomTypes)
public class RoomTypesController extends ORestController<IRoomTypeService> {

 @Autowired //permite que los DAO se enlacen correctamente a las variables donde las hemos definido, evitando el uso de métodos getter y setter.
 private IRoomTypeService roomTypesService;

 @Override
 public IRoomTypeService getService() {
  return this.roomTypesService;
 }
}