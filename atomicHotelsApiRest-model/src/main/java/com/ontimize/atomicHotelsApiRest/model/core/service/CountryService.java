package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.ValidateException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICountryService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.CountryDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ControlFields;
import com.ontimize.atomicHotelsApiRest.model.core.tools.EntityResultWrong;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ErrorMessage;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("CountryService")
@Lazy
public class CountryService implements ICountryService {

	@Autowired
	private CountryDao countryDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	ControlFields cf;

	private Map<String, String> mapCountries = null;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult countryQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult resultado = new EntityResultWrong();
		try {
			cf.reset();
			cf.addBasics(CountryDao.fields);
			cf.validate(keyMap);
			cf.validate(attrList);
			resultado = this.daoHelper.query(this.countryDao, keyMap, attrList);
		} catch (ValidateException e) {
			resultado = e.getEntityResult();
		} catch (Exception e) {
			e.printStackTrace();
			resultado = new EntityResultWrong(ErrorMessage.ERROR);
		}

		return resultado;
	}

	public Map<String, String> mapCountries() {
//		System.err.println(mapCountries);
		if (mapCountries == null) {
			List<String> attrList = new ArrayList<>() {
				{
					add(CountryDao.ATTR_ISO);
					add(CountryDao.ATTR_NAME);
				}
			};

			EntityResult er;
//			try {
			er = this.daoHelper.query(this.countryDao, new HashMap<>(), attrList);
//			} catch (Exception e) {
//				e.printStackTrace();
//				er = new EntityResultWrong(ErrorMessage.ERROR);
//			}
			// si llamo a control fields, se carga el que este validando este campo country
//			EntityResult er = countryQuery(new HashMap<>(), attrList);

			this.mapCountries = new HashMap<>();
			for (int i = 0; i < er.calculateRecordNumber(); i++) {
				mapCountries.put((String) er.getRecordValues(i).get(CountryDao.ATTR_ISO),
						(String) er.getRecordValues(i).get(CountryDao.ATTR_NAME));
			}
		}
		return mapCountries;
	}

}
