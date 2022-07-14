package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICreditCardService;
import com.ontimize.jee.server.rest.ORestController;

@RestController 
@RequestMapping("/creditcard") 
public class CreditCardRestController extends ORestController<ICreditCardService>{

	 @Autowired 
	 private ICreditCardService creditCardService;

	 @Override
	 public ICreditCardService getService() {
	  return this.creditCardService;
	 }

}
