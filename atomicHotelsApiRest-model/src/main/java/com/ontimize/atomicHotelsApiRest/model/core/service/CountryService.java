package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICountryService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CountryDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("CountryService")
@Lazy
public class CountryService implements ICountryService {

	@Autowired
	private CountryDao countryDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;


	
	@Override
	public EntityResult countryQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.query(this.countryDao, keyMap, attrList);
	}

}

