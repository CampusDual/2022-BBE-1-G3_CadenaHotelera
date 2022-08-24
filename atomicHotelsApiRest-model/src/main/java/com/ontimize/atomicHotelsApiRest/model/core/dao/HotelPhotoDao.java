package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import java.util.*;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("HotelPhotoDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/HotelPhotoDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class HotelPhotoDao extends OntimizeJdbcDaoSupport {
	
	public static final String TAG = "htl_pct_";
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_NAME = TAG+"name";
	public static final String ATTR_DESCRIPTION = TAG+"description";
	public static final String ATTR_FILE=TAG+"file";
	public static final String ATTR_PRIORITY=TAG+"priority";
	public static final String ATTR_HTL_ID=TAG+"htl_id";
	
	
	public static final String ATTR_FILE_URL=TAG+"file_url";
	public static final String ATTR_FILE_BYTE=TAG+"file_byte";
	public static final String ATTR_FILE_PATH=TAG+"file_path";
	
	public static final Map<String,type> fields=new HashMap<>()
	{
	{
			put(ATTR_ID, type.INTEGER);
			put(ATTR_NAME, type.STRING);
			put(ATTR_DESCRIPTION, type.STRING);
			put(ATTR_FILE, type.TEXT);
			put(ATTR_PRIORITY,type.FLOAT);
			put(ATTR_HTL_ID, type.INTEGER);
			
			put(ATTR_FILE_URL, type.TEXT);
			put(ATTR_FILE_BYTE, type.TEXT);
			put(ATTR_FILE_PATH, type.TEXT);
	}
	};
}
