package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ontimize.atomicHotelsApiRest.api.core.service.IBedComboService;
import com.ontimize.jee.server.rest.ORestController;

@RestController 
@RequestMapping("/bedcombo") 
public class BedComboRestController extends ORestController<IBedComboService>{

	 @Autowired 
	 private IBedComboService bedComboService;

	 @Override
	 public IBedComboService getService() {
	  return this.bedComboService;
	 }

}
