package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ontimize.atomicHotelsApiRest.api.core.service.IPictureService;
import com.ontimize.jee.server.rest.ORestController;

@RestController 
@RequestMapping("/pictures") 
public class PictureRestController extends ORestController<IPictureService>{

	 @Autowired 
	 private IPictureService pictureService;

	 @Override
	 public IPictureService getService() {
	  return this.pictureService;
	 }

}