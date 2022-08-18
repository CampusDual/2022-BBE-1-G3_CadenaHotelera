package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Lazy
@Repository(value = "UserDao")
@ConfigurationFile(configurationFile = "dao/UserDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class UserDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_USER= "user_";
	public static final String ATTR_PASSWORD = "password";

	public static final String ATTR_NAME = "name";
	public static final String ATTR_SURNAME = "surname";
	public static final String ATTR_EMAIL = "email";
	public static final String ATTR_NIF = "nif";

	public static final String ATTR_BLOCKED = "userblocked";
	public static final String ATTR_LAST_PASSWORD_UPDATE = "lastpasswordupdate";

	public static final String ATTR_HTL = "htl_restriction";
	public static final String NON_ATTR_ACTION = "action";
	public enum Action{CANCEL}

	
//	public static final String PASSWORD = "user_password";
//	public static final String SCHEMA = "db_schema";
//	public static final String CREATION_DATE = "user_creation_date";
//	public static final String DOWN_DATE = "user_down_date";

	
	
	public static final Map<String,type> fields = new HashMap<>() {{
		put(ATTR_USER,type.NO_EMPTY_SMALL_STRING);
		put(ATTR_PASSWORD,type.NO_EMPTY_SMALL_STRING);
		put(ATTR_NAME,type.SMALL_STRING);
		put(ATTR_SURNAME,type.SMALL_STRING);
		put(ATTR_EMAIL,type.SMALL_STRING);
		put(ATTR_NIF,type.SMALL_STRING);	
		put(ATTR_BLOCKED,type.DATE);	
		put(ATTR_LAST_PASSWORD_UPDATE,type.DATE);	
		put(ATTR_HTL,type.INTEGER);	
		put(NON_ATTR_ACTION,type.USER_ACTION);

	}};
	
	

}
