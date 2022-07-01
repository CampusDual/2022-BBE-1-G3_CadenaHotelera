package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("RoomTypeFeatureDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/RoomTypeFeatureDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class RoomTypeFeatureDao extends OntimizeJdbcDaoSupport {
	public static final String ATTR_ROOM_ID = "rmt_id";
	public static final String ATTR_FEATURE_ID = "ft_id";
}
