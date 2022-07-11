package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IServicesXtraService;
import com.ontimize.jee.server.rest.ORestController;

@RestController // indica que esta clase trabaja como un controlador, que responderá a las peticiones cuya URL tenga el path indicado en la anotación
@RequestMapping("/servicesXtra") //(en este caso, servicesXtra)(primera del postman)

public class ServicesXtraRestController extends ORestController<IServicesXtraService>{

	 @Autowired //permite que los DAO se enlacen correctamente a las variables donde las hemos definido, evitando el uso de métodos getter y setter.
	 private IServicesXtraService servicesXtraService;

	 @Override
	 public IServicesXtraService getService() {
	  return this.servicesXtraService;
	 }

}
