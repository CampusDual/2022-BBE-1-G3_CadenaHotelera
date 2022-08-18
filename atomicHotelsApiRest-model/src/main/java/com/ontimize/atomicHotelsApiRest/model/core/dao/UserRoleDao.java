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
	public static final String NON_ATTR_ROLE= "role";
	
	//nombre de los roles en la DB
	public static final String ROLE_CEO = "ceo";
	public static final String ROLE_MANAGER= "hotelManager";
	public static final String ROLE_STAFF = "staff";
	public static final String ROLE_CUSTOMER= "customer";
	public static final String ROLE_USER= "user";
	public enum UserRole {CEO, HOTEL_MANAGER, STAFF, CUSTOMER, USER};
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_ID_ROLENAME,type.INTEGER);
		put(NON_ATTR_ROLE,type.USER_ROLE);	
		put(ATTR_USER,type.NO_EMPTY_SMALL_STRING);	
	}};
	

}
