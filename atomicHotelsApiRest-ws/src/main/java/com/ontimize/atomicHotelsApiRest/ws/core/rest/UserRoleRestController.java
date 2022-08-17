package com.ontimize.atomicHotelsApiRest.ws.core.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.atomicHotelsApiRest.api.core.service.IUserRoleService;
import com.ontimize.atomicHotelsApiRest.api.core.service.IUserService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.rest.ORestController;


@RestController
@RequestMapping("/usersRoles")
@ComponentScan(basePackageClasses={com.ontimize.atomicHotelsApiRest.api.core.service.IUserService.class})
public class UserRoleRestController extends ORestController<IUserRoleService> {

	@Autowired
	private IUserRoleService userRoleSrv;

	@Override
	public IUserRoleService getService() {
		return this.userRoleSrv;
	}
}
