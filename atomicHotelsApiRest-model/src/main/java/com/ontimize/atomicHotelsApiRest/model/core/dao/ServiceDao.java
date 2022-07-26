package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("ServiceDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/ServiceDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class ServiceDao extends OntimizeJdbcDaoSupport {
	
	public static final String TAG = "srv_";
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_NAME = TAG+"name";
	public static final String ATTR_DESCRIPTION = TAG+"description";
	
	public static final Map<String,type> fields= new HashMap<>() 
	{
		{
		put(ServiceDao.ATTR_ID,type.INTEGER);
		put(ServiceDao.ATTR_NAME,type.STRING);
		put(ServiceDao.ATTR_DESCRIPTION,type.STRING);
	}
	};
}
