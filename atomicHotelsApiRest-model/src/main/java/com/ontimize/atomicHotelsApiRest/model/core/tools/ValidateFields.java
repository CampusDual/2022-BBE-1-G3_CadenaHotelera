package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Component;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingColumnsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.service.ICountryService;
import com.ontimize.atomicHotelsApiRest.model.core.service.CountryService;


@Component
public class ValidateFields {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") {
		{
			setLenient(false);
		}
	};

	public ValidateFields() {
	}

	@Autowired
	CountryService countryService;

	/**
	 * Comprueba si existe las claves facilitadas en el HashMap, y lanza excepción
	 * si no existe o si es null.
	 * 
	 * @param keyMap HashMap de claves/valores
	 * @param fields claves a validar
	 * @throws MissingFieldsException Excepción lanzada ante la primera clave que no
	 *                                encuentre.
	 */
//	public static void required(Map<String, Object> keyMap, String... fields) throws MissingFieldsException {
//		for (String field : fields) {
//			if (!keyMap.containsKey(field)) {
//				throw new MissingFieldsException("Falta el campo " + field);
//			}
//			if (keyMap.get(field) == null) {
//				throw new MissingFieldsException("El campo " + field + " es nulo");
//			}
//		}
//	}

	/**
	 * Comprueba si de todas las claves facilitadas, al menos una se encuentra en el
	 * HashMap con su correspondiente valor
	 * 
	 * @param keyMap
	 * @param fields
	 * @throws MissingFieldsException
	 */
//	public static void atLeastOneRequired(Map<String, Object> keyMap, String... fields) throws MissingFieldsException {
//		int contador = 0;
//		for (String field : fields) {
//			if (keyMap.containsKey(field) && keyMap.get(field) != null) {
//				contador++;
//			}
//		}
//		if (contador <= 0) {
//			throw new MissingFieldsException();
//		}
//	}

	/**
	 * Comprueba si alguno de los elementos está en la lista
	 * 
	 * @param attrList
	 * @param fields
	 * @throws MissingFieldsException
	 */
//	public static void atLeastOneRequired(List<String> attrList, String... fields) throws MissingColumnsException {
//		int contador = 0;
//		for (int i = 0; i < attrList.size(); i++) {
//			for (String field : fields) {
//				if (attrList.get(i).equals(field)) {
//					contador++;
//				}
//			}
//		}
//		if (contador <= 0) {
//			throw new MissingColumnsException();
//		}
//
//	}

	/**
	 * Elimina campos restringidos del HashMap
	 * 
	 * @param keyMap HashMap a actualizar
	 * @param fields campos a eliminar
	 */
//	public static void restricted(Map<String, Object> keyMap, String... fields) {
//		for (String field : fields) {
//			keyMap.remove(field);
//		}
//	}

	/**
	 * Elimina todos los campos a excepción de las claves especificadas
	 * 
	 * @param keyMap HashMap donde se eliminan las claves
	 * @param fields Claves a persistir.
	 */
//	public static void onlyThis(Map<String, Object> keyMap, String... fields) {
//		keyMap.keySet().retainAll(Arrays.asList(fields));
//	}

	/**
	 * Elimina todos los elementos de una lista a excepción de los especificadas
	 * 
	 * @param list   List donde se eliminan las claves
	 * @param fields elementos a persistir.
	 */
//	public static void onlyThis(List<String> list, String... fields) {
//		list.retainAll(Arrays.asList(fields));
//	}

	/**
	 * Elimina campos restringidos de la lista
	 * 
	 * @param keyMap HashMap a actualizar
	 * @param fields campos a eliminar
	 */
//	public static void restricted(List<String> attrList, String... fields) {
//		for (String field : fields) {
//			attrList.remove(field);
//		}
//	}

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
	 * 
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
	 * @return 0: fechas correctas - 1: fechas correctas, fecha inicio inferior a
	 *         hoy (consulta a pasado)
	 * @throws InvalidFieldsValuesException error fecha inicio superior a fecha fin
	 */
	public static int dataRange(Date startDate, Date endDate) throws InvalidFieldsValuesException {
		if (startDate.compareTo(endDate) < 0) {
			if (startDate.compareTo(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())) < 0) {
				return 1;
			}
			return 0;
		} else {
			throw new InvalidFieldsValuesException(ErrorMessage.DATA_RANGE_REVERSE);
		}
	}
	/*
	 * public static void emptyFields(Map<String, Object> keyMap) throws
	 * InvalidFieldsValuesException{ for(String key:keyMap.keySet()) {
	 * if(key.isEmpty()) { throw new
	 * InvalidFieldsValuesException("No es posible guardar Strings vacíos"); } } }
	 */

	/**
	 * Comprueba que no existe ningún campo que sea nulo, no se encuentre, o se
	 * halle formado por espacios en blanco
	 * 
	 * @param keyMap -> Hashmap de clave/valor
	 * @param fields -> Dato o datos (de nuestra BD), que ingresamos para su
	 *               comprobación.
	 * @throws MissingFieldsException -> Mensaje de error arrojado según los
	 *                                criterios indicados
	 */
//	public static void emptyFields(Map<String, Object> keyMap, String... fields) throws MissingFieldsException {
//		for (String field : fields) {
//			emptyField(keyMap, field);
//		}
//	}
//
//	public static void emptyField(Map<String, Object> keyMap, String field) throws MissingFieldsException {
//		if (!keyMap.containsKey(field)) {
//			throw new MissingFieldsException("Falta el campo " + field);
//		}
//		if (keyMap.get(field) == null) {
//			throw new MissingFieldsException("El campo " + field + " es nulo");
//		}
//		if (keyMap.get(field).toString().trim().length() == 0) {
//			throw new MissingFieldsException("El campo " + field + " no puede contener sólo espacios en blanco.");
//		}
//	}

	/*
	 * comprueba que un formato de precio tenga como maximo 2 decimales
	 */

	public static void formatprice(Object object) throws InvalidFieldsValuesException {
		String pre = "" + object;
		String comprueba = pre.substring(pre.indexOf(".") + 1);
		if (comprueba.length() > 2) {
			throw new InvalidFieldsValuesException("Introduce solo 2 decimales");
		} else {
			BigDecimal comprobado = new BigDecimal(pre);
		}

	}

	/**
	 * comprueba que un numero sea negativo o mayor que 0 si lo es lanza excepcion
	 * 
	 * @param numero
	 * @throws NumberFormatException error si es menor o igual 0
	 */

	public static void NegativeNotAllowed(long n) throws InvalidFieldsValuesException {
		if (n <= 0) {
			throw new InvalidFieldsValuesException(ErrorMessage.NEGATIVE_OR_CERO_NOT_ALLOWED);
		}
	}

	public static void NegativeNotAllowed(int n) throws InvalidFieldsValuesException {
		if (n <= 0) {
			throw new InvalidFieldsValuesException(ErrorMessage.NEGATIVE_OR_CERO_NOT_ALLOWED);
		}
	}

	/**
	 * comprueba que un numero tenga 13 a 16 digitos si no lo es lanza excepcion
	 * 
	 * @param numero
	 * @throws NumberFormatException error si es menor a 13 o mayor de 16 digitos
	 */

	public static void invalidCreditCard(long n) throws InvalidFieldsValuesException {
		try {
			final int LONGITUD_MAXIMA_TARJETA_CREDITO = 16;
			final int LONGITUD_MINIMA_TARJETA_CREDITO = 13;
			String numero = Long.toString(n);
			if (numero.length() < LONGITUD_MINIMA_TARJETA_CREDITO || numero.length() > LONGITUD_MAXIMA_TARJETA_CREDITO
					|| n <= 0) {
				throw new InvalidFieldsValuesException(ErrorMessage.INVALID_NUMBER_CREDITCARD);
			}
		} catch (java.lang.ClassCastException e) {
			throw new InvalidFieldsValuesException(ErrorMessage.INVALID_NUMBER_CREDITCARD);
		}
	}

	/**
	 * 
	 * @param expirey mes y dia de
	 * @return 0: fechas correctas
	 * @throws InvalidFieldsValuesException error hoy superior a fecha fin
	 */
	public static int validDateExpiry(String expiry) throws InvalidFieldsValuesException {
		Date expirity = stringToDate(expiry);
		if (expirity.compareTo(new Date()) > 0) {
			return 0;
		} else {
			throw new InvalidFieldsValuesException(ErrorMessage.DATA_EXPIRY_BEFORE_TODAY);
		}
	}
	/*
	 * public static void checkMail(String mail)throws InvalidFieldsValuesException
	 * { String regex=
	 * "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(-[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	 * 
	 * 
	 * 
	 * Pattern pattern = Pattern.compile(
	 * "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(-[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
	 * );
	 * 
	 * Matcher mather = pattern.matcher(mail);
	 * 
	 * if (mather.find() == true) { // if (mail.matches(regex)) { // } else { throw
	 * new InvalidFieldsValuesException(ErrorMessage.INVALID_MAIL); }
	 * 
	 * 
	 * Pattern pat = Pattern.compile(regex); Matcher mat = pat.matcher(mail); if
	 * (mat.matches()){​​​​​​ System.out.print(""); }​​​​​​else{
	 * System.out.print(""); }
	 * 
	 * }
	 */

	public static boolean checkMail(String mail) throws InvalidFieldsValuesException {
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(-[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(mail);
//		if (mat.matches()) {
//
//		} else {
//			throw new InvalidFieldsValuesException(ErrorMessage.INVALID_MAIL);
//		}
		return mat.matches();
	}

	/**
	 * Castea los valores de un keyMap a int si es posible.
	 * 
	 * @param keyMap
	 * @param fields
	 * @throws ClassCastException
	 */
	public static void isInt(Map<String, Object> keyMap, String... fields) throws InvalidFieldsValuesException {
		for (String field : fields) {
			try {
				if (keyMap.containsKey(field)) {
					int i = (int) keyMap.get(field);
				}
			} catch (ClassCastException | NullPointerException e) {
				throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + field);
			}
		}
	}

	// TODO validador pendiente
	/**
	 * Castea los valores de un keyMap a String si es posible.
	 * 
	 * @param keyMap
	 * @param fields
	 * @throws ClassCastException
	 */
	public static void isString(Map<String, Object> keyMap, String... fields) throws InvalidFieldsValuesException {
		for (String field : fields) {
			try {
				if (keyMap.containsKey(field)) {
					String i = (String) keyMap.get(field);// No salta nunca el NullPointerException
				}
			} catch (ClassCastException e) {
				throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + field);
			}
		}
	}

	// TODO validador pendiente
	/**
	 * Castea los valores de un keyMap a Date si es posible.
	 * 
	 * @param keyMap
	 * @param fields
	 * @throws ClassCastException
	 */
	public static void isDate(Map<String, Object> keyMap, String... fields) throws InvalidFieldsValuesException {
		for (String field : fields) {
			try {
				if (keyMap.containsKey(field)) {
					Date i = (Date) keyMap.get(field);
				}
			} catch (ClassCastException | NullPointerException e) {
				throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + field);
			}
		}
	}

	// TODO validador pendiente
	/**
	 * Castea los valores de un keyMap a long si es posible.
	 * 
	 * @param keyMap
	 * @param fields
	 * @throws ClassCastException
	 */
	public static void islong(Map<String, Object> keyMap, String... fields) throws InvalidFieldsValuesException {
		for (String field : fields) {
			try {
				if (keyMap.containsKey(field)) {
					long i = (long) keyMap.get(field);
				}
			} catch (ClassCastException | NullPointerException e) {
				throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + field);
			}
		}
	}

	// TODO validador pendiente
	/**
	 * Castea los valores de un keyMap a BigDecimal si es posible.
	 * 
	 * @param keyMap
	 * @param fields
	 * @throws ClassCastException
	 */
	public static void isBigDecimal(Map<String, Object> keyMap, String... fields) throws InvalidFieldsValuesException {
		for (String field : fields) {
			try {
				if (keyMap.containsKey(field)) {
					BigDecimal i = (BigDecimal) keyMap.get(field);
				}
			} catch (ClassCastException | NullPointerException e) {
				throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + field);
			}
		}
	}

	/**
	 * Castea los valores de un keyMap a double si es posible.
	 * 
	 * @param keyMap
	 * @param fields
	 * @throws ClassCastException
	 */
	public static void isDouble(Map<String, Object> keyMap, String... fields) throws InvalidFieldsValuesException {
		for (String field : fields) {
			try {
				if (keyMap.containsKey(field)) {
					double i = (double) keyMap.get(field);
				}
			} catch (ClassCastException | NullPointerException e) {
				throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + field);
			}
		}
	}

	/**
	 * Comprueba si el numero de entrada es 1 o 0
	 * 
	 * @param booleano en formato numero
	 * @throws InvalidFieldsValuesException
	 */
	public static boolean isBoolean(Integer value) throws InvalidFieldsValuesException {
		return (value == 0 || value == 1);		
	}
//	public static void isBoolean(Integer value) throws InvalidFieldsValuesException {
//		if (!(value == 0 || value == 1)) {
//			throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + value);
//		}
//	}

	/**
	 * Valida que el codigo iso de pais es válido
	 * 
	 * @param country Codigo iso, por ejemplo ES o GB
	 * @throws InvalidFieldsValuesException Si el valor es incorrecto.
	 */
	public boolean isCountry(String country) throws InvalidFieldsValuesException {
//		if (!countryService.mapCountries().containsKey(country)) {
//			throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + country);
//		}
//		if (country.length() != 2 || country.compareTo(country.toUpperCase()) != 0) {
//			throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + " - " + country);
//		}
		return countryService.mapCountries().containsKey(country);
	}

	/*
	 * comprueba los números de telefono con + o sin el delante, no permite otro
	 * caracter.
	 */

	public static boolean isPhone(String phone) throws InvalidFieldsValuesException {
		String regex = "^[+]?[\\s\\d]{8,20}$";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(phone.toString());
//		if (!mat.matches()) {
//			throw new InvalidFieldsValuesException(ErrorMessage.INVALID_PHONE);
//		}
		return mat.matches();
	}
	private static boolean isNumeric(String str) { 
	    try {  
	      Double.parseDouble(str);  
	      return true;
	    } catch(NumberFormatException e){  
	      return false;  
	    }  
	  }
	/*
	 * comprueba el dni tenga 8 dígitos y la letra sea la correcta.
	 * caracter.
	 */
	
	 public static boolean  isDNI(String itDNI) throws InvalidFieldsValuesException {
		 		final String dniChars="TRWAGMYFPDXBNJZSQVHLCKE";
		 		if (itDNI.length()!= 9) {
//	            	throw new InvalidFieldsValuesException(ErrorMessage.INVALID_DNI);
		 			return false;
	            }
		 		
	            String intPartDNI = itDNI.trim().replaceAll(" ", "").substring(0, 8);
	            char ltrDNI = Character.toUpperCase(itDNI.charAt(8));
	            int valNumDni = Integer.parseInt(intPartDNI) % 23;
	            
//	            if ( isNumeric(intPartDNI) == false || dniChars.charAt(valNumDni)!= ltrDNI) {
//	            	throw new InvalidFieldsValuesException(ErrorMessage.INVALID_DNI);
//	            } 
	            return ( isNumeric(intPartDNI) && dniChars.charAt(valNumDni) == ltrDNI);
	        }

}
