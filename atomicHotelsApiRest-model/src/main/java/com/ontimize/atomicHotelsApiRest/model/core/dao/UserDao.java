package com.ontimize.atomicHotelsApiRest.model.core.dao;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;


@Lazy
@Repository(value = "UserDao")
@ConfigurationFile(
	configurationFile = "dao/UserDao.xml",
	configurationFilePlaceholder = "dao/placeholders.properties")
public class UserDao extends OntimizeJdbcDaoSupport {

	public static final String USR_EMAIL    = "user_email";
	public static final String USR_PASSWORD = "user_password";

	public static final String ID            = "user_id";
	public static final String EMAIL         = "user_email";
	public static final String PASSWORD      = "user_password";
	public static final String NAME          = "user_name";
	public static final String SURNAME       = "user_surname";
	public static final String SCHEMA        = "db_schema";
	public static final String CREATION_DATE = "user_creation_date";
	public static final String DOWN_DATE     = "user_down_date";

}
