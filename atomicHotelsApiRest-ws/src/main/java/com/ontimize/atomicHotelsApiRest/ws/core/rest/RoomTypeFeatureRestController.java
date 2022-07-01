package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IFeatureService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IRoomTypeFeatureService;
import com.ontimize.jee.server.rest.ORestController;

@RestController // indica que esta clase trabaja como un controlador, que responderá a las peticiones cuya URL tenga el path indicado en la anotación
@RequestMapping("/roomTypesFeatures") //(en este caso, features)
public class RoomTypeFeatureRestController extends ORestController<IRoomTypeFeatureService>{

	 @Autowired //permite que los DAO se enlacen correctamente a las variables donde las hemos definido, evitando el uso de métodos getter y setter.
	 private IRoomTypeFeatureService roomTypeFeatureService;

	 @Override
	 public IRoomTypeFeatureService getService() {
	  return this.roomTypeFeatureService;
	 }

}
