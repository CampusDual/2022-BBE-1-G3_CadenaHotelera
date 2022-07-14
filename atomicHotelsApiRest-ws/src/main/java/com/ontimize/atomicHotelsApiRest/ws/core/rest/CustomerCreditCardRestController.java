package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICustomerCreditCardService;
import com.ontimize.jee.server.rest.ORestController;

@RestController 
@RequestMapping("/customercreditcard") 
public class CustomerCreditCardRestController extends ORestController<ICustomerCreditCardService>{

	 @Autowired 
	 private ICustomerCreditCardService customerCreditCardService;

	 @Override
	 public ICustomerCreditCardService getService() {
	  return this.customerCreditCardService;
	 }

}
