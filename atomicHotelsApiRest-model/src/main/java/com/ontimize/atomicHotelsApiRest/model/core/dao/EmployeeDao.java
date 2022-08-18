package com.ontimize.atomicHotelsApiRest.model.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("EmployeeDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/EmployeeDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class EmployeeDao extends OntimizeJdbcDaoSupport {
	
	public static final String TAG = "emp_";	
	public static final String ATTR_ID = TAG+"id";
	public static final String ATTR_NAME = TAG+"name";
	public static final String ATTR_SURNAME = TAG+"surname";
	public static final String ATTR_IDEN_DOC = TAG +"identity_document";
	public static final String ATTR_SOCIAL_DOC = TAG +"social_security_number";
	public static final String ATTR_SALARY = TAG+"salary";
	public static final String ATTR_PHONE = TAG+"phone_number";
	public static final String ATTR_ACCOUNT = TAG+"account_number";
	public static final String ATTR_EMAIL = TAG+"email";
	public static final String ATTR_CITY = TAG +"city";
	public static final String ATTR_STATE = TAG +"state";
	public static final String ATTR_ADDRESS = TAG+"address";
	public static final String ATTR_ZIP_CODE = TAG +"zip_code";
	public static final String ATTR_COUNTRY = TAG +"cnt_iso";//Clave foránea de countries,
	public static final String ATTR_DEPARTMENT=TAG+"dpt_id";//Clave foránead de departamento.
	public static final String ATTR_HOTEL=TAG+"htl_id";//Clave foránea de hotel.
	public static final String ATTR_HIRING= TAG+"hiring";
	public static final String ATTR_FIRED= TAG+"fired";



	public static final Map<String,type> fields = new HashMap<>() {
		{
		put(ATTR_ID,type.INTEGER);
		put(ATTR_NAME,type.NO_EMPTY_STRING);
		put(ATTR_SURNAME,type.STRING);
		put(ATTR_IDEN_DOC,type.DNI);//cambiar tipo
		put(ATTR_SOCIAL_DOC,type.NO_EMPTY_STRING);
		put(ATTR_SALARY,type.PRICE);
		put(ATTR_PHONE,type.PHONE);
		put(ATTR_ACCOUNT,type.NO_EMPTY_STRING);//cambiar tipo
		put(ATTR_EMAIL,type.EMAIL);
		put(ATTR_CITY,type.STRING);	
		put(ATTR_STATE,type.STRING);
		put(ATTR_ADDRESS,type.STRING);	
		put(ATTR_ZIP_CODE,type.STRING);	
		put(ATTR_COUNTRY,type.COUNTRY);
		put(ATTR_DEPARTMENT,type.INTEGER);
		put(ATTR_HOTEL,type.INTEGER);
		put(ATTR_HIRING,type.DATETIME);
		put(ATTR_FIRED,type.DATETIME);	
	}
	};

}

