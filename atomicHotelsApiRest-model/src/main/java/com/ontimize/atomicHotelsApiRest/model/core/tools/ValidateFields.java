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
	
	/**
	 * Comprueba si existe las claves facilitadas en el HashMap, y lanza excepción si no existe o si es null.
	 * @param keyMap HashMap de claves/valores 
	 * @param fields claves a validar
	 * @throws MissingFieldsException Excepción lanzada ante la primera clave que no encuentre.
	 */
	public static void required(Map<String, Object> keyMap, String... fields) throws MissingFieldsException {
		for (String field : fields) {
			if (!keyMap.containsKey(field)) {
				throw new MissingFieldsException("Falta el campo " + field);
			}
			if (keyMap.get(field) == null) {
				throw new MissingFieldsException("El campo " + field + " es nulo");
			}
		}
	}
	
	/**
	 * Elimina campos restringidos del HashMap
	 * @param keyMap HashMap a actualizar
	 * @param fields campos a eliminar
	 */
	public static void restricted(Map<String, Object> keyMap, String... fields){
		for (String field : fields) {
			keyMap.remove(field);
		}
	}
	
	/**
	 * Convierte un fecha en string a Date con formato "yyyy-MM-dd".
	 * 
	 * @param fecha
	 * @return
	 * @throws InvalidFieldsValuesException
	 */
	public static Date stringToDate(String fecha) throws InvalidFieldsValuesException {
		Date resultado;
		try {
			resultado = dateFormat.parse(fecha);
		} catch (ParseException e) {
			throw new InvalidFieldsValuesException(
					"Formato de fecha no válido (" + fecha + ") se requiere " + dateFormat.getDateFormatSymbols());
		}
		return resultado;
	}

	/**
	 * 
	 * @param startDate fecha de inicio en tipo String
	 * @param endDate   fecha de fin en tipo String
	 * @return 0: fechas correctas - 1: error fecha inicio superior a fecha fin - 2:
	 *         fechas correctas, fecha inicio inferior a hoy *
	 */
	public static int dataRange(String startDate, String endDate) throws InvalidFieldsValuesException {
		return dataRange(stringToDate(startDate), stringToDate(endDate));
	}

	/**
	 * Castea los Object a String o Date para usar el metodo dataRang adecuado
	 * @param startDate fecha de inicio en tipo Object
	 * @param endDate   fecha de fin en tipo Object
	 * @return 0: fechas correctas - 1: error fecha inicio superior a fecha fin - 2:
	 *         fechas correctas, fecha inicio inferior a hoy *
	 */
	public static int dataRange(Object startDate, Object endDate) throws InvalidFieldsValuesException {
		if (startDate instanceof String && endDate instanceof String) {
			return dataRange(stringToDate((String) startDate), stringToDate((String) endDate));
		} else if (startDate instanceof Date && endDate instanceof Date) {
			return dataRange((Date) startDate, (Date) endDate);
		}
		throw new InvalidFieldsValuesException("Los tipos de datos no son String ni Date");
	}

	/**
	 * 
	 * @param startDate fecha de inicio
	 * @param endDate   fecha de fin
	 * @return 0: fechas correctas - 1: error fecha inicio superior a fecha fin - 2:
	 *         fechas correctas, fecha inicio inferior a hoy *
	 */
	public static int dataRange(Date startDate, Date endDate) {
		if (startDate.compareTo(endDate) < 0) {
			if (startDate.compareTo(new Date()) < 0) {
				return 2;
			}
			return 0;
		} else {
			return 1; // error
		}
	}

}
