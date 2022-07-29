package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IBookingGuestService;
import com.ontimize.jee.server.rest.ORestController;

@RestController 
@RequestMapping("/bookingsGuests") 
public class BookingGuestRestController extends ORestController<IBookingGuestService>{

	 @Autowired 
	 private IBookingGuestService bookingGuestService;

	 @Override
	 public IBookingGuestService getService() {
	  return this.bookingGuestService;
	 }

}
