package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("CountryDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/CountryDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class CountryDao extends OntimizeJdbcDaoSupport {
	
	public static final String TAG = "cnt_";
	public static final String ATTR_ISO = TAG+"iso";
	public static final String ATTR_NAME = TAG+"name";
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ISO,type.COUNTRY);
		put(ATTR_NAME,type.NO_EMPTY_SMALL_STRING);				
	}};
}
