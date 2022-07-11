package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelServiceService;
import com.ontimize.jee.server.rest.ORestController;

@RestController 
@RequestMapping("/hotelserviceextra") 
public class HotelServiceExtraRestController extends ORestController<IHotelServiceService>{

	 @Autowired //permite que los DAO se enlacen correctamente a las variables donde las hemos definido, evitando el uso de métodos getter y setter.
	 private IHotelServiceService hotelServiceService;

	 @Override
	 public IHotelServiceService getService() {
	  return this.hotelServiceService;
	 }

}