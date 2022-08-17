package com.ontimize.atomicHotelsApiRest.ws.core.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.ontimize.atomicHotelsApiRest.api.core.service.IPictureService;
import com.ontimize.jee.common.dto.EntityResult;
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
//		@RequestMapping(
//				value = "/getPicture",
//				method = RequestMethod.POST,
//				produces = MediaType.APPLICATION_JSON_VALUE)
//				public ResponseEntity<EntityResult> query(@RequestBody Map<String,Object> req) { 
//				return this.getService().((Map<String,Object>)req.get("filter"),(List<String>)req.get("columns"));
//				}
//			
//			}
}