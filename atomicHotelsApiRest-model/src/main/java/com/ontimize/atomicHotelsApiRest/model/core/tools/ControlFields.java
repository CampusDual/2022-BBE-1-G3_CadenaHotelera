package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.InvalidFieldsValuesException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.LiadaPardaException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.MissingFieldsException;
import com.ontimize.atomicHotelsApiRest.api.core.exceptions.RestrictedFieldException;
import com.ontimize.atomicHotelsApiRest.model.core.tools.TypeCodes.type;

import kotlin.jvm.Throws;

public class ControlFields {

	private Map<String, type> fields = new HashMap<String, type>();
	private List<String> restricted = null;
	private List<String> required = null;
	private boolean optional = true;
	private boolean noEmptyList = true;
	private boolean noWildcard = true;

	public ControlFields() {

	}

	public void addBasics(Map<String, type> fields) {
		this.fields.putAll(fields);
	}

	public void setRequired(List<String> requiredFields) {
		this.required = requiredFields;
	}

	public void setRestricted(List<String> restrictedFields) {
		this.restricted = restrictedFields;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	
	
	public boolean isNoEmptyList() {
		return noEmptyList;
	}

	public void setNoEmptyList(boolean noEmptyList) {
		this.noEmptyList = noEmptyList;
	}

	public boolean isNoWildcard() {
		return noWildcard;
	}

	public void setNoWildcard(boolean noWildcard) {
		this.noWildcard = noWildcard;
	}

	/**
	 * 
	 * @param keyMap
	 * @throws MissingFieldsException
	 * @throws RestrictedFieldException
	 * @throws InvalidFieldsException
	 * @throws InvalidFieldsValuesException
	 * @throws LiadaPardaException
	 */
	public void validate(Map<String, Object> keyMap) throws MissingFieldsException, RestrictedFieldException,
			InvalidFieldsException, InvalidFieldsValuesException, LiadaPardaException {
		
			if(keyMap == null) {
				throw new MissingFieldsException(ErrorMessage.NO_NULL_DATA);
			}
		
		if (required != null) {
			for (String key : required) {
				if (!keyMap.containsKey(key)) {
					throw new MissingFieldsException(ErrorMessage.REQUIRED_FIELD);
				}
			}
		}

		if (restricted != null) {
			for (String key : restricted) {
				if (keyMap.containsKey(key)) {
					throw new RestrictedFieldException(ErrorMessage.INVALID_FIELD + key);
				}
			}
		}

		if (!optional && required == null) {
			throw new LiadaPardaException(ErrorMessage.INTERNAL_CAGADA);
		} else {
			if (!optional && (required.size() != keyMap.size())) {
				throw new InvalidFieldsException(ErrorMessage.ALLOWED_FIELDS + required.toString());
			}
		}

//validar typos y valores
		for (String key : keyMap.keySet()) {
			boolean validType = false;
			
			if (fields.containsKey(key)) {				
				if(keyMap.get(key) == null) {
					throw new MissingFieldsException(ErrorMessage.NO_NULL_VALUE + key);
				}
				
				switch (fields.get(key)) {
				case STRING:
					if ((keyMap.get(key) instanceof String)) {
						validType = true;
					}
					break;
				case INTEGER:
					if ((keyMap.get(key) instanceof Integer)) {
						validType = true;
					}
					break;

				case LONG:
					if (keyMap.get(key) instanceof Integer || keyMap.get(key) instanceof Long) {
						validType = true;
					}
					break;

				case DOUBLE:
					if (keyMap.get(key) instanceof Integer || keyMap.get(key) instanceof Long
							|| keyMap.get(key) instanceof Double) {
						validType = true;
					}
					break;

				case PRICE:
					if (keyMap.get(key) instanceof Integer || keyMap.get(key) instanceof Double) {
						System.err.println("TODO - revisar mensaje de error....");
						ValidateFields.formatprice(keyMap.get(key));
						validType = true;
					}
					break;

				case CREDIT_CARD:
					if (keyMap.get(key) instanceof Long) {
						System.err.println("TODO - revisar mensaje de error....");
						ValidateFields.invalidCreditCard((Long) keyMap.get(key));
						validType = true;
					}
					break;

				case EXPIRATION_DATE:
					if ((keyMap.get(key) instanceof String)) {
						System.err.println("TODO - comprobar....");
						ValidateFields.validDateExpiry((String) keyMap.get(key));
						validType = true;
					}
					break;

				case PHONE:
					if ((keyMap.get(key) instanceof String)) {
						System.err.println("TODO - validar phone de verdad....");
						validType = true;
					}
					break;

				case COUNTRY:
					if ((keyMap.get(key) instanceof String)) {
	System.err.println("TODO - validar countries de verdad....");
						ValidateFields.country(key);
						validType = true;
					}
					break;

				case DATE:
					if ((keyMap.get(key) instanceof String)) {
						System.err.println("TODO - comprobar....");
						keyMap.replace(key, ValidateFields.stringToDate((String) keyMap.get(key)));
						validType = true;
					}
					break;

				case EMAIL:
					if ((keyMap.get(key) instanceof String)) {
						ValidateFields.checkMail((String) keyMap.get(key));
						validType = true;
					}
					break;
				default:
					throw new InvalidFieldsValuesException(ErrorMessage.WRONG_TYPE + key);
				}

				if (!validType) {
					throw new InvalidFieldsValuesException(
							ErrorMessage.WRONG_TYPE + key + ErrorMessage.REQUIRED_TYPE + fields.get(key));
				}

				// STRING, INTEGER, INTEGER_UNSIGNED, LONG, LONG_UNSIGNED, DOUBLE,
				// DOUBLE_UNSIGNED, PRICE, EMAIL, CREDIT_CARD,
				// PHONE, DATE, DATETIME, ACTION, BOOLEAN, COUNTRY

			} else {
				throw new InvalidFieldsException(ErrorMessage.INVALID_FIELD + key);
			}
		}
	}

	/**
	 * Para validar atributos de vuelta. Ignora los valores.
	 * 
	 * @param columns Lista de camos a validar
	 * @throws MissingFieldsException
	 * @throws RestrictedFieldException
	 * @throws LiadaPardaException
	 * @throws InvalidFieldsException
	 */
	public void validate(List<String> columns)
			throws MissingFieldsException, RestrictedFieldException, LiadaPardaException, InvalidFieldsException {
		
		if(columns == null) {
			throw new MissingFieldsException(ErrorMessage.NO_NULL_DATA);
		}
	
		if (noEmptyList) {
			int minimuSize = 1;
			
			if (noWildcard && columns.contains("*")) {
				minimuSize++;
			}
					
			if (columns.size() < minimuSize) {
				throw new MissingFieldsException(ErrorMessage.REQUIRED_MINIMUM_COLUMS);
			}
		}

		if (required != null) {
			for (String key : required) {
				if (!columns.contains(key)) {
					throw new MissingFieldsException(ErrorMessage.REQUIRED_FIELD);
				}
			}
		}

		if (restricted != null) {
			for (String key : restricted) {
				if (columns.contains(key)) {
					throw new RestrictedFieldException(ErrorMessage.INVALID_FIELD + key);
				}
			}
		}

		if (!optional && required == null) {
			throw new LiadaPardaException(ErrorMessage.INTERNAL_CAGADA);
		} else {
			if (!optional && (required.size() != columns.size())) {
				throw new InvalidFieldsException(ErrorMessage.ALLOWED_FIELDS + required.toString());
			}
		}

		for (String key : columns) {
			if (!fields.containsKey(key)) {
				throw new InvalidFieldsException(ErrorMessage.INVALID_FIELD + key);
			}
		}
	}

//	public static void set(Map<String, Object> keyMap, String... fields) throws MissingFieldsException {
//		for (String field : fields) {
//			if (!keyMap.containsKey(field)) {
//				throw new MissingFieldsException("Falta el campo " + field);
//			}
//			if (keyMap.get(field) == null) {
//				throw new MissingFieldsException("El campo " + field + " es nulo");
//			}
//		}
//	}

}
