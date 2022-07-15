package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingServiceExtraService;
import com.ontimize.jee.server.rest.ORestController;

@RestController 
@RequestMapping("/bookingserviceextra") 
public class BookingServiceExtraRestController extends ORestController<IBookingServiceExtraService>{

	 @Autowired //permite que los DAO se enlacen correctamente a las variables donde las hemos definido, evitando el uso de métodos getter y setter.
	 private IBookingServiceExtraService bookingServiceextraService;

	 @Override
	 public IBookingServiceExtraService getService() {
	  return this.bookingServiceextraService;
	 }

}