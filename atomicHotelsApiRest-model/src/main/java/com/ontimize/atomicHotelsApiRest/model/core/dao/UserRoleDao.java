package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository(value = "UserRoleDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/UserRoleDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class UserRoleDao extends OntimizeJdbcDaoSupport {
	public static final String ATTR_ID= "id_user_role";
	public static final String ATTR_ID_ROLENAME= "id_rolename";	
	public static final String ATTR_USER= "user_";
	
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_ID_ROLENAME,type.INTEGER);
		put(ATTR_USER,type.STRING);	
	}};
	

}
