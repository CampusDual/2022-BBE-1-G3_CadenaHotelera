package com.ontimize.atomicHotelsApiRest.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import java.util.*;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("EmployeePhotoDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/EmployeePhotoDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class EmployeePhotoDao extends OntimizeJdbcDaoSupport {
	
	public static final String TAG = "pct_";
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_NAME = TAG+"name";
	public static final String ATTR_DESCRIPTION = TAG+"description";
	public static final String ATTR_FILE=TAG+"file";
	public static final String ATTR_EMPLOYEE_DNI=TAG+"emp_dni";
	
	public static final Map<String,type> fields=new HashMap<>()
	{
	{
			put(ATTR_ID, type.INTEGER);
			put(ATTR_NAME, type.STRING);
			put(ATTR_DESCRIPTION, type.STRING);
			put(ATTR_FILE, type.TEXT);
			put(ATTR_EMPLOYEE_DNI,type.DNI);
	}
	};
}
