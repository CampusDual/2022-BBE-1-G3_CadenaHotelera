package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("RoomTypeFeatureDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/RoomTypeFeatureDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class RoomTypeFeatureDao extends OntimizeJdbcDaoSupport {
	public static final String ATTR_ROOM_ID = "rmt_id";
	public static final String ATTR_FEATURE_ID = "ftr_id";
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ROOM_ID,type.INTEGER);
		put(ATTR_FEATURE_ID,type.INTEGER);
				
	}};
	
}
