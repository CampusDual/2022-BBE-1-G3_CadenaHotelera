package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;

public class ValidateFields {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"){{setLenient(false);}};;

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
	 * Elimina campos restringidos de la lista
	 * @param keyMap HashMap a actualizar
	 * @param fields campos a eliminar
	 */
	public static void restricted(List<String> attrList, String... fields){
		for (String field : fields) {
			attrList.remove(field);
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
		} catch (ParseException | NullPointerException e) {
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
	 * @return 0: fechas correctas - 1:
	 *         fechas correctas, fecha inicio inferior a hoy (consulta a pasado) 
	 * @throws InvalidFieldsValuesException  error fecha inicio superior a fecha fin 
	 */
	public static int dataRange(Date startDate, Date endDate) throws InvalidFieldsValuesException {
		if (startDate.compareTo(endDate) < 0) {
			if (startDate.compareTo(new Date()) < 0) {
				return 1;
			}
			return 0;
		} else {
			throw new InvalidFieldsValuesException(ErrorMessage.DATA_RANGE_REVERSE);
		}
	}
	/*
	public static void emptyFields(Map<String, Object> keyMap) throws InvalidFieldsValuesException{
		for(String key:keyMap.keySet()) {
			if(key.isEmpty()) {
				throw new InvalidFieldsValuesException("No es posible guardar Strings vacíos");
			}
		}
	}
*/
	
	/**Comprueba que no existe ningún campo que sea nulo, no se encuentre, o se halle formado por espacios en blanco
	 * 
	 * @param keyMap	-> Hashmap de clave/valor
	 * @param fields    -> Dato o datos (de nuestra BD), que ingresamos para su comprobación.
	 * @throws MissingFieldsException -> Mensaje de error arrojado según los criterios indicados
	 */
	public static void emptyFields(Map<String, Object> keyMap, String... fields) throws MissingFieldsException {
		for (String field : fields) {
			emptyField(keyMap, field);
		}
	}

	public static void emptyField(Map<String, Object> keyMap, String field) throws MissingFieldsException {
		if (!keyMap.containsKey(field)) {
			throw new MissingFieldsException("Falta el campo " + field);
		}
		if (keyMap.get(field) == null) {
			throw new MissingFieldsException("El campo " + field + " es nulo");
		}
		if (keyMap.get(field).toString().trim().length() == 0) {
			throw new MissingFieldsException("El campo " + field + " no puede contener sólo espacios en blanco.");
		}
	}
	
	/*
	 * comprueba que un formato de precio tenga como maximo 2 decimales
	 */

	public static void formatprice(Object object) throws NumberFormatException {
		String pre = ""+object;
		String comprueba = pre.substring(pre.indexOf(".")+1);
		if(comprueba.length()>2) {
			throw new NumberFormatException("Introduce solo 2 decimales");
		}
		
	}
	
	/**
	 * comprueba que un numero sea negativo o mayor que 0 si lo es lanza excepcion
	 * @param numero
	 * @throws NumberFormatException  error si es menor o igual  0
	 */
	 
	
	public static void NegativeNotAllowed(int n) throws NumberFormatException{
		if(n<=0) {
			throw new NumberFormatException();
		}
	}
	


	
public static void invalidCreditCard(long n) throws NumberFormatException{
	  try {
		  final int LONGITUD_MAXIMA_TARJETA_CREDITO = 16;
		  final int LONGITUD_MINIMA_TARJETA_CREDITO = 13;
		  String numero=Long.toString(n);
		 if( numero.length() < LONGITUD_MINIMA_TARJETA_CREDITO || numero.length() > LONGITUD_MAXIMA_TARJETA_CREDITO){
      	  throw new NumberFormatException();
        }
	  	}catch(java.lang.ClassCastException e) {
	  		throw new NumberFormatException();
	  	}
    }
}



