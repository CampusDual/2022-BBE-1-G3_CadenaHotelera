package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;

public class ValidateFields {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public ValidateFields() {		
	}

	public static void required(Map<String, Object> keyMap, String...fields) throws MissingFieldsException {
		for(String field: fields) {
			if(!keyMap.containsKey(field)) {
				throw new MissingFieldsException("Falta el campo " + field);
			}
			if(keyMap.get(field) == null) {
				throw new MissingFieldsException("El campo " + field + " es nulo");
			}
		}		
	}

	/**
	 * Convierte un fecha en string a Date con formato "yyyy-MM-dd".
	 * @param fecha
	 * @return
	 * @throws InvalidFieldsValuesException
	 */
	public static Date stringToDate(String fecha) throws InvalidFieldsValuesException {
		Date resultado;
		try {
			resultado = dateFormat.parse(fecha);
		} catch (ParseException e) {
			throw new InvalidFieldsValuesException("Formato de fecha no válido");
		}		
		return resultado;
	}
	
	/**
	 * 
	 * @param startDate fecha de inicio en tipo String
	 * @param endDate fecha de fin en tipo String
	 * @return 0: fechas correctas -  1: error fecha inicio superior a fecha fin - 2: fechas correctas, fecha inicio inferior a hoy	 * 
	 */
	public static int dataRange(String startDate, String endDate) throws InvalidFieldsValuesException {
		return dataRange(stringToDate(startDate), stringToDate(endDate));
	}
	
	/**
	 * 
	 * @param startDate fecha de inicio
	 * @param endDate fecha de fin
	 * @return 0: fechas correctas -  1: error fecha inicio superior a fecha fin - 2: fechas correctas, fecha inicio inferior a hoy	 * 
	 */
	public static int dataRange(Date startDate, Date endDate) {		
		if(startDate.compareTo(endDate) < 0) {
			if(startDate.compareTo(new Date()) < 0) {
				return 2;
			}
			return 0;
		}else {
			return 1; //error
		}				
	}
		
}
