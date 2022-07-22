package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICountryService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CountryDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("CountryService")
@Lazy
public class CountryService implements ICountryService {

	@Autowired
	private CountryDao countryDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	private Map<String, String> mapCountries = null;

	@Override
	public EntityResult countryQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		ControlFields cf = new ControlFields();
		cf.addBasics(CountryDao.fields);
		try {
			cf.validate(keyMap);
			cf.validate(attrList);
			resultado = this.daoHelper.query(this.countryDao, keyMap, attrList);
		} catch (MissingFieldsException | RestrictedFieldException | LiadaPardaException | InvalidFieldsException | InvalidFieldsValuesException e) {
			resultado =  new EntityResultWrong(e.getMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public Map<String,String> mapCountries(){
		if(mapCountries == null) {
			
		}
		return mapCountries;
	}

	
	
}

