package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("HotelServiceDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/HotelServiceDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class HotelServiceDao extends OntimizeJdbcDaoSupport {
	
	
	public static final String ATTR_ID_HTL ="htl_id";
	public static final String ATTR_ID_SRV="srv_id";
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID_HTL,type.INTEGER);
		put(ATTR_ID_SRV,type.INTEGER);			
	}};

}
