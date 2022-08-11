package com.ontimize.atomicHotelsApiRest.api.core.service;

import java.util.List;
import java.util.Map;


import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface ICountryService {
	public EntityResult countryQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	//Map<String, String> mapCountries();
}
