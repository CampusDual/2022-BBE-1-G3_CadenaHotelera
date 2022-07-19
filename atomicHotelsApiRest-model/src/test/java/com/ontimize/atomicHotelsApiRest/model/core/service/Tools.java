package com.ontimize.atomicHotelsApiRest.model.core.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.EntityResultRequiredException;
import com.ontimize.atomicHotelsApiRest.model.core.dao.BookingDao;
import com.ontimize.atomicHotelsApiRest.model.core.tools.ValidateFields;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;


/**
 * Metodos para teastear
 * @author Ar
 *
 */
public class Tools {


//	public static void main(String[] args) {
//		System.out.println(getNowString());
//		System.out.println(getTomorrowString());
//		System.out.println(getYesterdayString());
//		System.out.println(getNow());
//		System.out.println(getTomorrow());
//		System.out.println(getYesterday());
//	}
	static SimpleDateFormat dateFormat = ValidateFields.dateFormat;
	static DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern(dateFormat.toPattern());
	
	/**
	 *Fecha actual de HOY (sin horas/minutos)  
	 * @return String
	 */
	static String getNowString() {
		return LocalDate.now().format(localDateFormat);
	}
	
	/**
	 *Fecha actual de MAÑANA (sin horas/minutos)  
	 * @return String
	 */
	static String getTomorrowString() {
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		return tomorrow.format(localDateFormat);
	}
	/**
	 *Fecha actual de MAÑANA (sin horas/minutos)  
	 * @return String
	 */
	static String getYesterdayString() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		return yesterday.format(localDateFormat);
	}
	
	/**
	 * Fecha actual de HOY al principio del día 
	 * @return Date
	 */
	static Date getNow() {        
		return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * Fecha actual de MAÑANA al principio del día 
	 * @return Date
	 */
	static Date getTomorrow() {
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		return Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * Fecha actual de AYER al principio del día 
	 * @return Date
	 */
	static Date getYesterday() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		return Date.from(yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
